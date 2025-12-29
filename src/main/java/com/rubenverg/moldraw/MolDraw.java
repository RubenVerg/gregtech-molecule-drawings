package com.rubenverg.moldraw;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BucketItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.google.common.hash.HashCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Either;
import com.rubenverg.moldraw.data.AlloysData;
import com.rubenverg.moldraw.data.MoleculesData;
import com.rubenverg.moldraw.molecule.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod(MolDraw.MOD_ID)
@SuppressWarnings("removal")
public class MolDraw {

    public static final String MOD_ID = "moldraw";
    public static final Logger LOGGER = LogManager.getLogger();

    public MolDraw() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

            modEventBus.addListener(this::modConstruct);
            modEventBus.addListener(this::gatherData);
            modEventBus.addListener(this::registerClientTooltipComponents);
            modEventBus.addListener(this::registerClientReloadListeners);

            MinecraftForge.EVENT_BUS.addListener(this::tooltipGatherComponents);
        });
    }

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Molecule.class, Molecule.Json.INSTANCE)
            .registerTypeAdapter(Element.class, Element.Json.INSTANCE)
            .registerTypeAdapter(Element.Color.class, Element.Color.Json.INSTANCE)
            .registerTypeAdapter(Element.Counted.class, Element.Counted.Json.INSTANCE)
            .registerTypeAdapter(Atom.class, Atom.Json.INSTANCE)
            .registerTypeAdapter(Bond.class, Bond.Json.INSTANCE)
            .registerTypeAdapter(Bond.Line.class, Bond.Line.Json.INSTANCE)
            .registerTypeAdapter(Parens.class, Parens.Json.INSTANCE)
            .registerTypeAdapter(CircleTransformation.class, CircleTransformation.Json.INSTANCE)
            .setPrettyPrinting()
            .create();

    public void modConstruct(FMLConstructModEvent event) {
        event.enqueueWork(MolDrawConfig::init);
    }

    public void gatherData(GatherDataEvent event) {
        final var gen = event.getGenerator();

        gen.addProvider(event.includeClient(), new DataProvider.Factory<>() {

            @Override
            public @NotNull DataProvider create(@NotNull PackOutput output) {
                final var moleculesPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK,
                        "molecules");
                final var alloysPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "alloys");
                return new DataProvider() {

                    @Override
                    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
                        for (final var entry : MoleculesData.molecules().entrySet()) {
                            final var json = gson.toJson(entry.getValue(), Molecule.class);
                            try {
                                cachedOutput.writeIfNeeded(moleculesPathProvider.json(entry.getKey()),
                                        json.getBytes(StandardCharsets.UTF_8), HashCode.fromInt(json.hashCode()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        for (final var entry : AlloysData.alloys().entrySet()) {
                            final var json = gson.toJson(AlloysData.write(entry.getValue()));
                            try {
                                cachedOutput.writeIfNeeded(alloysPathProvider.json(entry.getKey()),
                                        json.getBytes(StandardCharsets.UTF_8), HashCode.fromInt(json.hashCode()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return CompletableFuture.completedFuture(null);
                    }

                    @Override
                    public @NotNull String getName() {
                        return "Molecules provider";
                    }
                };
            }
        });
    }

    public void registerClientTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(MoleculeTooltipComponent.class, MoleculeTooltipComponent.ClientMoleculeTooltipComponent::new);
        event.register(AlloyTooltipComponent.class, AlloyTooltipComponent.ClientAlloyTooltipComponent::new);
    }

    private static final Map<Material, Molecule> molecules = new HashMap<>();
    private static final Map<Material, Optional<List<Pair<Material, Long>>>> alloys = new HashMap<>();

    public void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(

                new SimplePreparableReloadListener<Map<Material, Molecule>>() {

                    @MethodsReturnNonnullByDefault
                    @ParametersAreNonnullByDefault
                    @Override
                    protected Map<Material, Molecule> prepare(ResourceManager resourceManager,
                                                              ProfilerFiller profilerFiller) {
                        final Map<Material, Molecule> molecules = new HashMap<>();
                        for (final var id : resourceManager
                                .listResources("molecules", path -> path.toString().endsWith(".json")).keySet()) {
                            try (final var stream = resourceManager.open(id)) {
                                final var file = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                                final var material = GTCEuAPI.materialManager
                                        .getMaterial(id.toString().replace(".json", "").replace("molecules/", ""));
                                if (Objects.isNull(material)) {
                                    continue;
                                }
                                final var molecule = gson.fromJson(file, Molecule.class);
                                molecules.put(material, molecule);
                            } catch (IOException | JsonSyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return molecules;
                    }

                    @MethodsReturnNonnullByDefault
                    @ParametersAreNonnullByDefault
                    @Override
                    protected void apply(Map<Material, Molecule> prepareResult, ResourceManager resourceManager,
                                         ProfilerFiller profilerFiller) {
                        molecules.clear();
                        molecules.putAll(prepareResult);
                    }
                });
        event.registerReloadListener(
                new SimplePreparableReloadListener<Map<Material, Optional<List<Pair<Material, Long>>>>>() {

                    @MethodsReturnNonnullByDefault
                    @ParametersAreNonnullByDefault
                    @Override
                    protected Map<Material, Optional<List<Pair<Material, Long>>>> prepare(ResourceManager resourceManager,
                                                                                          ProfilerFiller profilerFiller) {
                        final Map<Material, Optional<List<Pair<Material, Long>>>> alloys = new HashMap<>();
                        for (final var id : resourceManager
                                .listResources("alloys", path -> path.toString().endsWith(".json")).keySet()) {
                            try (final var stream = resourceManager.open(id)) {
                                final var file = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                                final var material = GTCEuAPI.materialManager
                                        .getMaterial(id.toString().replace(".json", "").replace("alloys/", ""));
                                if (Objects.isNull(material)) {
                                    continue;
                                }
                                final var alloy = AlloysData.read(gson.fromJson(file, JsonElement.class));
                                if (alloy.isEmpty()) {
                                    alloys.put(material, Optional.empty());
                                } else {
                                    alloys.put(material, Optional.of(alloy.get().stream().map(pair -> {
                                        final var subMat = GTCEuAPI.materialManager.getMaterial(pair.getA().toString());
                                        if (Objects.isNull(subMat) || subMat.isNull()) throw new RuntimeException(
                                                "Alloy JSON contains a material that doesn't exist");
                                        return new Pair<>(subMat, pair.getB());
                                    }).toList()));
                                }
                            } catch (IOException | JsonSyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return alloys;
                    }

                    @MethodsReturnNonnullByDefault
                    @ParametersAreNonnullByDefault
                    @Override
                    protected void apply(Map<Material, Optional<List<Pair<Material, Long>>>> prepareResult,
                                         ResourceManager resourceManager, ProfilerFiller profilerFiller) {
                        alloys.clear();
                        alloys.putAll(prepareResult);
                    }
                });
    }

    public static @Nullable Molecule getMolecule(Material material) {
        return molecules.get(material);
    }

    public static @Nullable List<Pair<Material, Long>> getAlloy(Material material) {
        return Optional.ofNullable(alloys.get(material))
                .map(opt -> opt.orElseGet(() -> AlloyTooltipComponent.deriveComponents(material))).orElse(null);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static void tryColorizeFormula(Material material, OptionalInt idx,
                                          List<Either<FormattedText, TooltipComponent>> tooltipElements) {
        if (Objects.nonNull(material.getMaterialComponents()) && !material.getMaterialComponents().isEmpty() ||
                material.isElement()) {
            final var coloredFormula = MoleculeColorize.coloredFormula(new MaterialStack(material, 1), true);
            if (idx.isPresent()) tooltipElements.set(idx.getAsInt(), Either.left(coloredFormula));
            else tooltipElements.add(1, Either.left(coloredFormula));
        }
    }

    @SubscribeEvent
    public void tooltipGatherComponents(RenderTooltipEvent.GatherComponents event) {
        if (!MolDrawConfig.INSTANCE.enabled) return;
        // event.getTooltipElements().add(0, Either.right(new MoleculeTooltipComponent(new Molecule()
        // )));

        Material material;
        if (event.getItemStack().getItem() instanceof BucketItem bi) {
            material = ChemicalHelper.getMaterial(bi.getFluid());
        } else {
            final var materialStack = ChemicalHelper.getMaterialEntry(event.getItemStack().getItem());
            if (materialStack.isEmpty()) return;
            material = materialStack.material();
        }
        if (material.isNull()) return;
        final var mol = getMolecule(material);
        final var alloy = getAlloy(material);
        final var tooltipElements = event.getTooltipElements();

        final var idx = IntStream.range(0, tooltipElements.size())
                .filter(i -> tooltipElements.get(i).left()
                        .map(tt -> tt.getString().equals(material.getChemicalFormula()))
                        .orElse(false))
                .reduce((a, b) -> b);

        if (!MolDrawConfig.INSTANCE.onlyShowOnShift || GTUtil.isShiftDown()) {
            if (!Objects.isNull(mol)) {
                if (idx.isPresent())
                    tooltipElements.set(idx.getAsInt(), Either.right(new MoleculeTooltipComponent(mol)));
                else tooltipElements.add(1, Either.right(new MoleculeTooltipComponent(mol)));
            } else if (!Objects.isNull(alloy)) {
                if (idx.isPresent())
                    tooltipElements.set(idx.getAsInt(), Either.right(new AlloyTooltipComponent(alloy)));
                else tooltipElements.add(1, Either.right(new AlloyTooltipComponent(alloy)));
                // } else if (material.getResourceLocation().getNamespace().equals(MOD_ID)) {
                // if (idx.isPresent()) tooltipElements.set(idx.getAsInt(), Either.right(new
                // AlloyTooltipComponent(AlloyTooltipComponent.deriveComponents(material))));
                // else tooltipElements.add(1, Either.right(new
                // AlloyTooltipComponent(AlloyTooltipComponent.deriveComponents(material))));
            } else {
                tryColorizeFormula(material, idx, tooltipElements);
            }
        } else {
            tryColorizeFormula(material, idx, tooltipElements);

            if (MolDrawConfig.INSTANCE.onlyShowOnShift) {
                final int ttIndex = idx.orElse(1) + 1;

                if (Objects.nonNull(mol)) {
                    tooltipElements.add(ttIndex, Either.left(FormattedText
                            .of(Component.translatable("tooltip.moldraw.shift_view_molecule").getString())));
                } else if (Objects.nonNull(alloy)) {
                    tooltipElements.add(ttIndex, Either.left(
                            FormattedText.of(Component.translatable("tooltip.moldraw.shift_view_alloy").getString())));
                }
            }
        }
    }
}
