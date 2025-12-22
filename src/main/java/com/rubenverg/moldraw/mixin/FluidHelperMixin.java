package com.rubenverg.moldraw.mixin;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;

import com.adsioho.gtm.compat.MaterialHelper;
import com.mojang.datafixers.util.Either;
import com.rubenverg.moldraw.MolDraw;
import com.rubenverg.moldraw.MolDrawConfig;
import com.rubenverg.moldraw.MoleculeTooltipComponent;
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
        if (MaterialHelper.isNull(material)) return; // <-- 使用 MaterialHelper 判空

        final var mol = MolDraw.getMolecule(material);
        final var tooltipElements = ((JeiTooltipMixin) jeiTooltip).getLines();
        final var idx = IntStream.range(0, tooltipElements.size())
                .filter(i -> tooltipElements.get(i).left()
                        .map(tt -> tt.getString().equals(material.getChemicalFormula()))
                        .orElse(false))
                .reduce((a, b) -> b);

        if (!Objects.isNull(mol) &&
                (!MolDrawConfig.INSTANCE.onlyShowOnShift || GTUtil.isShiftDown())) {
            if (idx.isPresent()) tooltipElements.set(idx.getAsInt(), Either.right(new MoleculeTooltipComponent(mol)));
            else tooltipElements.add(1, Either.right(new MoleculeTooltipComponent(mol)));
        } else {
            MolDraw.tryColorizeFormula(material, idx, tooltipElements);

            if (!Objects.isNull(mol) &&
                    MolDrawConfig.INSTANCE.onlyShowOnShift) {
                final int ttIndex = idx.orElse(1) + 1;

                tooltipElements.add(ttIndex, Either
                        .left(FormattedText.of(Component.translatable("tooltip.moldraw.shift_view").getString())));
            }
        }
    }
}
