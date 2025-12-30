package com.rubenverg.moldraw.mixin;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;

import com.mojang.datafixers.util.Either;
import com.rubenverg.moldraw.MolDraw;
import com.rubenverg.moldraw.MolDrawConfig;
import com.rubenverg.moldraw.component.AlloyTooltipComponent;
import com.rubenverg.moldraw.component.MoleculeTooltipComponent;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.common.gui.JeiTooltip;
import mezz.jei.forge.platform.FluidHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.stream.IntStream;

@Mixin(value = FluidHelper.class, priority = 1000000) // for sure more than GregTech
public class FluidHelperMixin {

    @Inject(method = "getTooltip(Lmezz/jei/api/gui/builder/ITooltipBuilder;Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraft/world/item/TooltipFlag;)V",
            at = @At("TAIL"),
            remap = false,
            require = 0)
    private void moldraw$injectFluidTooltips(ITooltipBuilder tooltip, FluidStack ingredient, TooltipFlag tooltipFlag,
                                             CallbackInfo ci) {
        if (!(tooltip instanceof JeiTooltip jeiTooltip)) return;
        if (!MolDrawConfig.INSTANCE.enabled) return;

        final var material = ChemicalHelper.getMaterial(ingredient.getFluid());
        if (material.isNull()) return;

        final var mol = MolDraw.getMolecule(material);
        final var alloy = MolDraw.getAlloy(material);
        final var tooltipElements = ((JeiTooltipMixin) jeiTooltip).getLines();
        final var idx = IntStream.range(0, tooltipElements.size())
                .filter(i -> tooltipElements.get(i).left()
                        .map(tt -> tt.getString().equals(material.getChemicalFormula()))
                        .orElse(false))
                .reduce((a, b) -> b);

        if (!MolDrawConfig.INSTANCE.onlyShowOnShift || GTUtil.isShiftDown()) {
            if (!Objects.isNull(mol) && MolDrawConfig.INSTANCE.molecule.showMolecules) {
                if (idx.isPresent())
                    tooltipElements.set(idx.getAsInt(), Either.right(new MoleculeTooltipComponent(mol)));
                else tooltipElements.add(1, Either.right(new MoleculeTooltipComponent(mol)));
            } else if (!Objects.isNull(alloy) && MolDrawConfig.INSTANCE.alloy.showAlloys) {
                if (idx.isPresent())
                    tooltipElements.set(idx.getAsInt(), Either.right(new AlloyTooltipComponent(alloy)));
                else tooltipElements.add(1, Either.right(new AlloyTooltipComponent(alloy)));
                // } else if (material.getResourceLocation().getNamespace().equals(MolDraw.MOD_ID)) {
                // if (idx.isPresent()) tooltipElements.set(idx.getAsInt(), Either.right(new
                // AlloyTooltipComponent(AlloyTooltipComponent.deriveComponents(material))));
                // else tooltipElements.add(1, Either.right(new
                // AlloyTooltipComponent(AlloyTooltipComponent.deriveComponents(material))));
            } else {
                MolDraw.tryColorizeFormula(material, idx, tooltipElements);
            }
        } else {
            MolDraw.tryColorizeFormula(material, idx, tooltipElements);

            if (MolDrawConfig.INSTANCE.onlyShowOnShift) {
                final int ttIndex = idx.orElse(1) + 1;

                if (Objects.nonNull(mol) && MolDrawConfig.INSTANCE.molecule.showMolecules) {
                    tooltipElements.add(ttIndex, Either.left(FormattedText
                            .of(Component.translatable("tooltip.moldraw.shift_view_molecule").getString())));
                } else if (Objects.nonNull(alloy) && MolDrawConfig.INSTANCE.alloy.showAlloys) {
                    tooltipElements.add(ttIndex, Either.left(
                            FormattedText.of(Component.translatable("tooltip.moldraw.shift_view_alloy").getString())));
                }
            }
        }
    }
}
