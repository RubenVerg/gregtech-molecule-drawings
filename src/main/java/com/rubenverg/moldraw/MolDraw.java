package com.rubenverg.moldraw;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
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
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Either;
import com.rubenverg.moldraw.data.MoleculesData;
import com.rubenverg.moldraw.molecule.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
            .registerTypeAdapter(Element.Counted.class, Element.Counted.Json.INSTANCE)
            .registerTypeAdapter(Atom.class, Atom.Json.INSTANCE)
            .registerTypeAdapter(Bond.class, Bond.Json.INSTANCE)
            .registerTypeAdapter(Bond.Type.class, Bond.Type.Json.INSTANCE)
            .registerTypeAdapter(Parens.class, Parens.Json.INSTANCE)
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
                final var pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "molecules");
                return new DataProvider() {

                    @Override
                    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
                        for (final var entry : MoleculesData.molecules().entrySet()) {
                            final var json = gson.toJson(entry.getValue(), Molecule.class);
                            try {
                                cachedOutput.writeIfNeeded(pathProvider.json(entry.getKey()),
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
    }

    private static final Map<Material, Molecule> molecules = new HashMap<>();

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
    }

    public static @Nullable Molecule getMolecule(Material material) {
        return molecules.get(material);
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
        if (Objects.isNull(mol)) return;
        final var tooltipElements = event.getTooltipElements();
        final var idx = IntStream.range(0, tooltipElements.size())
                .filter(i -> tooltipElements.get(i).left()
                        .map(tt -> tt.getString().equals(material.getChemicalFormula()))
                        .orElse(false))
                .findFirst();
        if (idx.isPresent()) tooltipElements.set(idx.getAsInt(), Either.right(new MoleculeTooltipComponent(mol)));
        else tooltipElements.add(1, Either.right(new MoleculeTooltipComponent(mol)));
    }
}
