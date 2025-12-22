package com.rubenverg.moldraw.mixin;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.material.Fluid;

import com.llamalad7.mixinextras.sugar.Local;
import com.rubenverg.moldraw.MolDraw;
import com.rubenverg.moldraw.MolDrawConfig;
import com.rubenverg.moldraw.MoleculeTooltipComponent;
import dev.emi.emi.api.stack.FluidEmiStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.IntStream;

@Mixin(value = FluidEmiStack.class, remap = false)
public class FluidEmiStackMixin {

    @Shadow
    @Final
    private Fluid fluid;

    @Unique
    private static String moldraw$simpleGetText(FormattedCharSequence seq) {
        final var builder = new StringBuilder();
        seq.accept((_pos, _style, codepoint) -> {
            builder.append(Character.toString(codepoint));
            return true;
        });
        return builder.toString();
    }

    @Inject(method = "getTooltip",
            at = @At(value = "INVOKE",
                     target = "Ldev/emi/emi/api/render/EmiTooltipComponents;appendModName(Ljava/util/List;Ljava/lang/String;)V"),
            remap = false,
            require = 0)
    private void moldraw$addFluidTooltip(CallbackInfoReturnable<List<ClientTooltipComponent>> cir,
                                         @Local(name = "list") List<ClientTooltipComponent> list) {
        if (!MolDrawConfig.INSTANCE.enabled) return;

        final Material material = ChemicalHelper.getMaterial(fluid);
        if (Objects.isNull(material)) return;
        if (Objects.isNull(material.getMaterialComponents())) return;

        final var mol = MolDraw.getMolecule(material);
        final OptionalInt idx = IntStream.range(0, list.size())
                .filter(i -> list.get(i) instanceof ClientTextTooltip ctt &&
                        moldraw$simpleGetText(((ClientTextTooltipMixin) ctt).getText()) // <-- 使用 mixin 访问器
                                .equals(material.getChemicalFormula()))
                .reduce((a, b) -> b);
        final OptionalInt quantityIdx = IntStream.range(0, list.size())
                .filter(i -> list.get(i) instanceof ClientTextTooltip ctt &&
                        moldraw$simpleGetText(((ClientTextTooltipMixin) ctt).getText()).endsWith("mB")) // <-- 使用 mixin
                                                                                                        // 访问器
                .findFirst();

        if (mol != null && (!MolDrawConfig.INSTANCE.onlyShowOnShift || GTUtil.isShiftDown())) {
            final ClientTooltipComponent comp = ClientTooltipComponent.create(new MoleculeTooltipComponent(mol));
            if (idx.isPresent()) {
                list.set(idx.getAsInt(), comp);
            } else {
                final int insertIdx = quantityIdx.isPresent() ? quantityIdx.getAsInt() + 1 : 1;
                list.add(Math.min(insertIdx, list.size()), comp);
            }
        }
    }
}
