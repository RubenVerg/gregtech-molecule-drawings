package com.rubenverg.moldraw;

import java.util.*;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;
import com.rubenverg.moldraw.component.AlloyTooltipHandler;
import com.rubenverg.moldraw.component.MoleculeTooltipHandler;
import com.rubenverg.moldraw.data.Alloys;
import com.rubenverg.moldraw.data.Molecules;
import com.rubenverg.moldraw.molecule.*;

import akka.japi.Pair;
import bartworks.system.material.BWMetaGeneratedItems;
import bartworks.system.material.Werkstoff;
import codechicken.lib.gui.GuiDraw;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.item.base.BaseItemComponent;

@Mod(
    modid = MolDraw.MOD_ID,
    version = Tags.VERSION,
    name = "GregTech Molecule Drawings",
    acceptedMinecraftVersions = "[1.7.10]")
public class MolDraw {

    public MolDraw() {
        LOGGER.info("constructed");
    }

    @Mod.Instance
    public MolDraw INSTANCE;

    public static final String MOD_ID = "moldraw";
    public static final Logger LOGGER = LogManager.getLogger();

    @SidedProxy(clientSide = "com.rubenverg.moldraw.ClientProxy", serverSide = "com.rubenverg.moldraw.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("preInit");
        ConfigurationManager.registerConfig(MolDrawConfig.class);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void itemTooltip(ItemTooltipEvent event) {
        if (!MolDrawConfig.enabled) return;
        Molecule mol = null;
        List<Pair<IOreMaterial, Long>> alloy = null;
        String formula = null;
        if (event.itemStack.getItem() instanceof BaseItemComponent bic
            && bic.componentType == BaseItemComponent.ComponentTypes.CELL
            && Objects.isNull(bic.componentMaterial)) {
            if (FluidRegistry.getFluid(
                bic.unlocalName.replaceFirst("^itemCell", "")
                    .toLowerCase()) instanceof Fluid fluid)
                mol = MolDraw.getMolecule(fluid);
            else if (FluidRegistry.getFluid(
                bic.unlocalName.replaceFirst("^itemCell", "fluid.")
                    .toLowerCase()) instanceof Fluid fluid)
                mol = MolDraw.getMolecule(fluid);
        } else {
            IOreMaterial material = null;
            if (Objects.isNull(material) && event.itemStack.getItem() instanceof BWMetaGeneratedItems)
                material = Werkstoff.werkstoffHashMap.get((short) event.itemStack.getItemDamage());
            if (Objects.isNull(material) && event.itemStack.getItem() instanceof BaseItemComponent bic)
                material = bic.componentMaterial;
            if (Objects.isNull(material))
                material = Optional.ofNullable(GTOreDictUnificator.getItemData(event.itemStack))
                    .map(d -> d.mMaterial.mMaterial)
                    .orElse(null);
            if (!Objects.isNull(material)) {
                mol = MolDraw.getMolecule(material);
                alloy = MolDraw.getAlloy(material);
                formula = AlloyTooltipHandler.getFormula(material);
            }
        }
        Integer index = null;
        MolDraw.LOGGER.info("There are {} tooltips to check: {}", event.toolTip.size(), event.toolTip);
        for (var i = 0; i < event.toolTip.size(); i++) {
            MolDraw.LOGGER.info("Comparing {} against {}", event.toolTip.get(i), formula);
            if (event.toolTip.get(i)
                .replaceAll("§.", "")
                .equals(formula)) {
                index = i;
                break;
            }
        }
        if (Objects.nonNull(mol) && MolDrawConfig.molecule.showMolecules) {
            if (MolDrawConfig.onlyShowOnShift && !GuiScreen.isShiftKeyDown()) {
                if (Objects.isNull(index)) event.toolTip.add(1, "§7Press Shift to view the structure");
                else event.toolTip.add(index + 1, "§7Press Shift to view the structure");
            } else {
                if (Objects.isNull(index)) event.toolTip
                    .add(1, GuiDraw.TOOLTIP_HANDLER + GuiDraw.getTipLineId(new MoleculeTooltipHandler(mol)));
                else event.toolTip
                    .set(index, GuiDraw.TOOLTIP_HANDLER + GuiDraw.getTipLineId(new MoleculeTooltipHandler(mol)));
            }
        } else if (Objects.nonNull(alloy) && MolDrawConfig.alloy.showAlloys) {
            if (MolDrawConfig.onlyShowOnShift && !GuiScreen.isShiftKeyDown()) {
                if (Objects.isNull(index)) event.toolTip.add(1, "§7Press Shift to view the composition");
                else event.toolTip.add(index + 1, "§7Press Shift to view the composition");
            } else {
                if (Objects.isNull(index)) event.toolTip
                    .add(1, GuiDraw.TOOLTIP_HANDLER + GuiDraw.getTipLineId(new AlloyTooltipHandler(alloy)));
                else event.toolTip
                    .set(index, GuiDraw.TOOLTIP_HANDLER + GuiDraw.getTipLineId(new AlloyTooltipHandler(alloy)));
            }
        }
    }

    public static @Nullable Molecule getMolecule(IOreMaterial material) {
        final var molecules = Molecules.molecules();
        var mol = molecules.get(material);
        if (Objects.nonNull(mol)) return mol;
        if (material instanceof Werkstoff bw && Objects.nonNull(bw.getBridgeMaterial()))
            mol = molecules.get(bw.getBridgeMaterial());
        return mol;
    }

    public static @Nullable Molecule getMolecule(Fluid fluid) {
        return Molecules.fluidMolecules()
            .get(fluid);
    }

    public static @Nullable List<Pair<IOreMaterial, Long>> getAlloy(IOreMaterial material) {
        return Optional.ofNullable(
            Alloys.alloys()
                .get(material))
            .map(opt -> opt.orElseGet(() -> AlloyTooltipHandler.deriveComponents(material)))
            .orElse(null);
    }

    /*
     * @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
     * public static void tryColorizeFormula(IOreMaterial material, OptionalInt idx,
     * List<Either<FormattedText, TooltipComponent>> tooltipElements) {
     * if (!MolDrawConfig.INSTANCE.color.colors) return;
     * if (Objects.nonNull(material.getMaterialComponents()) && !material.getMaterialComponents().isEmpty() ||
     * material.isElement()) {
     * final var coloredFormula = MoleculeColorize.coloredFormula(new MaterialStack(material, 1), true);
     * if (idx.isPresent()) tooltipElements.set(idx.getAsInt(), Either.left(coloredFormula));
     * else tooltipElements.add(1, Either.left(coloredFormula));
     * }
     * }
     * @SubscribeEvent
     * public void itemTooltip(ItemTooltipEvent event) {
     * if (!MolDrawConfig.INSTANCE.enabled) return;
     * // event.getTooltipElements().add(0, Either.right(new MoleculeTooltipComponent(new Molecule()
     * // )));
     * IOreMaterial material = Optional.ofNullable(GTOreDictUnificator.getItemData(event.itemStack)).map(d ->
     * d.mMaterial.mMaterial).orElse(null);
     * if (Objects.isNull(material)) return;
     * final var mol = getMolecule(material);
     * final var alloy = getAlloy(material);
     * final var tooltipElements = event.getTooltipElements();
     * final var idx = IntStream.range(0, tooltipElements.size())
     * .filter(i -> tooltipElements.get(i).left()
     * .map(tt -> tt.getString().equals(material.getChemicalFormula()))
     * .orElse(false))
     * .reduce((a, b) -> b);
     * if (!MolDrawConfig.INSTANCE.onlyShowOnShift || GTUtil.isShiftDown()) {
     * if (!Objects.isNull(mol) && MolDrawConfig.INSTANCE.molecule.showMolecules) {
     * if (idx.isPresent())
     * tooltipElements.set(idx.getAsInt(), Either.right(new MoleculeTooltipHandler(mol)));
     * else tooltipElements.add(1, Either.right(new MoleculeTooltipHandler(mol)));
     * } else if (!Objects.isNull(alloy) && MolDrawConfig.INSTANCE.alloy.showAlloys) {
     * if (idx.isPresent())
     * tooltipElements.set(idx.getAsInt(), Either.right(new AlloyTooltipComponent(alloy)));
     * else tooltipElements.add(1, Either.right(new AlloyTooltipComponent(alloy)));
     * // } else if (material.getResourceLocation().getNamespace().equals(MOD_ID)) {
     * // if (idx.isPresent()) tooltipElements.set(idx.getAsInt(), Either.right(new
     * // AlloyTooltipComponent(AlloyTooltipComponent.deriveComponents(material))));
     * // else tooltipElements.add(1, Either.right(new
     * // AlloyTooltipComponent(AlloyTooltipComponent.deriveComponents(material))));
     * } else {
     * tryColorizeFormula(material, idx, tooltipElements);
     * }
     * } else {
     * tryColorizeFormula(material, idx, tooltipElements);
     * if (MolDrawConfig.INSTANCE.onlyShowOnShift) {
     * final int ttIndex = idx.orElse(1) + 1;
     * if (Objects.nonNull(mol) && MolDrawConfig.INSTANCE.molecule.showMolecules) {
     * tooltipElements.add(ttIndex, Either.left(FormattedText
     * .of(Component.translatable("tooltip.moldraw.shift_view_molecule").getString())));
     * } else if (Objects.nonNull(alloy) && MolDrawConfig.INSTANCE.alloy.showAlloys) {
     * tooltipElements.add(ttIndex, Either.left(
     * FormattedText.of(Component.translatable("tooltip.moldraw.shift_view_alloy").getString())));
     * }
     * }
     * }
     * }
     */
}
