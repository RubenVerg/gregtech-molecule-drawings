package com.rubenverg.moldraw.mixin;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.fluids.FluidStack;

import com.rubenverg.moldraw.MolDraw;
import com.rubenverg.moldraw.MolDrawConfig;
import com.rubenverg.moldraw.component.AlloyTooltipComponent;
import com.rubenverg.moldraw.component.MoleculeTooltipComponent;
import mezz.jei.neoforge.platform.FluidHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Mixin(value = FluidHelper.class, priority = 1000000) // for sure more than GregTech
public class FluidHelperMixin {

    @Inject(method = "getTooltip(Ljava/util/List;Lnet/neoforged/neoforge/fluids/FluidStack;Lnet/minecraft/world/item/TooltipFlag;)V",
            at = @At("TAIL"),
            remap = false,
            require = 0)
    private void moldraw$injectFluidTooltips(List<Component> tooltip, FluidStack ingredient, TooltipFlag tooltipFlag,
                                             CallbackInfo ci) {
        if (!MolDrawConfig.INSTANCE.enabled) return;

        final var material = ChemicalHelper.getMaterial(ingredient.getFluid());
        if (Objects.isNull(material)) return;

        final var mol = MolDraw.getMolecule(material);
        final var alloy = MolDraw.getAlloy(material);
        final var idx = IntStream.range(0, tooltip.size())
                .filter(i -> tooltip.get(i).getString().equals(material.getChemicalFormula()))
                .reduce((a, b) -> b);

        if (!MolDrawConfig.INSTANCE.onlyShowOnShift || GTUtil.isShiftDown()) {
            if (!Objects.isNull(mol) && MolDrawConfig.INSTANCE.molecule.showMolecules) {
                if (idx.isPresent())
                    tooltip.set(idx.getAsInt(),
                            new IIngredientRendererMixin.ShamComponent(new MoleculeTooltipComponent(mol)));
                else tooltip.add(1, new IIngredientRendererMixin.ShamComponent(new MoleculeTooltipComponent(mol)));
            } else if (!Objects.isNull(alloy) && MolDrawConfig.INSTANCE.alloy.showAlloys) {
                if (idx.isPresent())
                    tooltip.set(idx.getAsInt(),
                            new IIngredientRendererMixin.ShamComponent(new AlloyTooltipComponent(alloy)));
                else tooltip.add(1, new IIngredientRendererMixin.ShamComponent(new AlloyTooltipComponent(alloy)));
                // } else if (material.getResourceLocation().getNamespace().equals(MolDraw.MOD_ID)) {
                // if (idx.isPresent()) tooltipElements.set(idx.getAsInt(), Either.right(new
                // AlloyTooltipComponent(AlloyTooltipComponent.deriveComponents(material))));
                // else tooltipElements.add(1, Either.right(new
                // AlloyTooltipComponent(AlloyTooltipComponent.deriveComponents(material))));
            } else {
                MolDraw.tryColorizeFormulaComponents(material, idx, tooltip);
            }
        } else {
            MolDraw.tryColorizeFormulaComponents(material, idx, tooltip);

            if (MolDrawConfig.INSTANCE.onlyShowOnShift) {
                final int ttIndex = idx.orElse(1) + 1;

                if (Objects.nonNull(mol) && MolDrawConfig.INSTANCE.molecule.showMolecules) {
                    tooltip.add(ttIndex, Component.translatable("tooltip.moldraw.shift_view_molecule"));
                } else if (Objects.nonNull(alloy) && MolDrawConfig.INSTANCE.alloy.showAlloys) {
                    tooltip.add(ttIndex, Component.translatable("tooltip.moldraw.shift_view_alloy"));
                }
            }
        }
    }
}
