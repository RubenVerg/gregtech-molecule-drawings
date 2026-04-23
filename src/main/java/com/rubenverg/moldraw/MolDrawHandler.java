package com.rubenverg.moldraw;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.rubenverg.moldraw.component.AlloyTooltipHandler;
import com.rubenverg.moldraw.component.MoleculeTooltipHandler;
import com.rubenverg.moldraw.molecule.Molecule;

import akka.japi.Pair;
import bartworks.system.material.BWMetaGeneratedItems;
import bartworks.system.material.Werkstoff;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.guihook.IContainerTooltipHandler;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.item.base.BaseItemComponent;

public class MolDrawHandler implements IContainerTooltipHandler {

    @Override
    public List<String> handleItemTooltip(GuiContainer gui, ItemStack stack, int mouseX, int mouseY,
        List<String> currentTooltips) {
        if (Objects.nonNull(stack)) {
            Molecule mol = null;
            List<Pair<IOreMaterial, Long>> alloy = null;
            String formula = null;
            if (stack.getItem() instanceof BaseItemComponent bic
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
                if (Objects.isNull(material) && stack.getItem() instanceof BWMetaGeneratedItems)
                    material = Werkstoff.werkstoffHashMap.get((short) stack.getItemDamage());
                if (Objects.isNull(material) && stack.getItem() instanceof BaseItemComponent bic)
                    material = bic.componentMaterial;
                if (Objects.isNull(material)) material = Optional.ofNullable(GTOreDictUnificator.getItemData(stack))
                    .map(d -> d.mMaterial.mMaterial)
                    .orElse(null);
                if (!Objects.isNull(material)) {
                    mol = MolDraw.getMolecule(material);
                    alloy = MolDraw.getAlloy(material);
                    formula = AlloyTooltipHandler.getFormula(material);
                }
            }
            Integer index = null;
            for (var i = 0; i < currentTooltips.size(); i++) if (currentTooltips.get(i)
                .replaceAll("§.", "")
                .equals(formula)) {
                    index = i;
                    break;
                }
            if (Objects.nonNull(mol)) {
                if (Objects.isNull(index)) currentTooltips
                    .add(1, GuiDraw.TOOLTIP_HANDLER + GuiDraw.getTipLineId(new MoleculeTooltipHandler(mol)));
                else currentTooltips
                    .set(index, GuiDraw.TOOLTIP_HANDLER + GuiDraw.getTipLineId(new MoleculeTooltipHandler(mol)));
            } else if (Objects.nonNull(alloy)) {
                if (Objects.isNull(index)) currentTooltips
                    .add(1, GuiDraw.TOOLTIP_HANDLER + GuiDraw.getTipLineId(new AlloyTooltipHandler(alloy)));
                else currentTooltips
                    .set(index, GuiDraw.TOOLTIP_HANDLER + GuiDraw.getTipLineId(new AlloyTooltipHandler(alloy)));
            }
        }
        return currentTooltips;
    }
}
