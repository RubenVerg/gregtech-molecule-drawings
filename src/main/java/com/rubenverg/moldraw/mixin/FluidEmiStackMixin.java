package com.rubenverg.moldraw.mixin;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.material.Fluid;

import com.llamalad7.mixinextras.sugar.Local;
import com.rubenverg.moldraw.*;
import com.rubenverg.moldraw.component.AlloyTooltipComponent;
import com.rubenverg.moldraw.component.MoleculeTooltipComponent;
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

    @Unique
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static void moldraw$tryColorizeFormula(List<ClientTooltipComponent> list, Material material,
                                                   OptionalInt idx, OptionalInt quantityIdx) {
        if (Objects.nonNull(material.getMaterialComponents()) && !material.getMaterialComponents().isEmpty() ||
                material.isElement()) {
            final var coloredFormula = MoleculeColorize.coloredFormula(new MaterialStack(material, 1), true);

            if (idx.isPresent())
                list.set(idx.getAsInt(), ClientTooltipComponent.create(coloredFormula.getVisualOrderText()));
            else
                list.add(quantityIdx.stream().map(i -> i + 1).findFirst().orElse(1),
                        ClientTooltipComponent.create(coloredFormula.getVisualOrderText()));
        }
    }

    @Inject(method = "getTooltip",
            at = @At(value = "INVOKE",
                     target = "Ldev/emi/emi/api/render/EmiTooltipComponents;appendModName(Ljava/util/List;Ljava/lang/String;)V"),
            remap = false,
            require = 0)
    private void moldraw$addFluidTooltip(CallbackInfoReturnable<List<ClientTooltipComponent>> cir,
                                         @Local(name = "list") List<ClientTooltipComponent> list) {
        if (!MolDrawConfig.INSTANCE.enabled) return;
        final var material = ChemicalHelper.getMaterial(fluid);
        if (material.isNull()) return;
        if (Objects.isNull(material.getMaterialComponents())) return;

        final var mol = MolDraw.getMolecule(material);
        final var alloy = MolDraw.getAlloy(material);
        final var idx = IntStream.range(0, list.size()).filter(i -> list.get(i) instanceof ClientTextTooltip ctt &&
                moldraw$simpleGetText(((ClientTextTooltipMixin) ctt).getText()).equals(material.getChemicalFormula()))
                .reduce((a, b) -> b);
        final var quantityIdx = IntStream.range(0, list.size())
                .filter(i -> list.get(i) instanceof ClientTextTooltip ctt &&
                        moldraw$simpleGetText(((ClientTextTooltipMixin) ctt).getText()).endsWith("mB"))
                .findFirst();
        final var insertAt = quantityIdx.stream().map(i -> i + 1).findFirst().orElse(1);

        if (!MolDrawConfig.INSTANCE.onlyShowOnShift || GTUtil.isShiftDown()) {
            if (!Objects.isNull(mol) && MolDrawConfig.INSTANCE.molecule.showMolecules) {
                if (idx.isPresent())
                    list.set(idx.getAsInt(), ClientTooltipComponent.create(new MoleculeTooltipComponent(mol)));
                else list.add(insertAt, ClientTooltipComponent.create(new MoleculeTooltipComponent(mol)));
            } else if (!Objects.isNull(alloy) && MolDrawConfig.INSTANCE.alloy.showAlloys) {
                if (idx.isPresent())
                    list.set(idx.getAsInt(), ClientTooltipComponent.create(new AlloyTooltipComponent(alloy)));
                else list.add(insertAt, ClientTooltipComponent.create(new AlloyTooltipComponent(alloy)));
                // } else if (material.getResourceLocation().getNamespace().equals(MOD_ID)) {
                // if (idx.isPresent()) list.set(idx.getAsInt(), ClientTooltipComponent.create(new
                // AlloyTooltipComponent(AlloyTooltipComponent.deriveComponents(material))));
                // else list.add(insertAt, ClientTooltipComponent.create(new
                // AlloyTooltipComponent(AlloyTooltipComponent.deriveComponents(material))));
            } else {
                moldraw$tryColorizeFormula(list, material, idx, quantityIdx);
            }
        } else {
            moldraw$tryColorizeFormula(list, material, idx, quantityIdx);

            if (MolDrawConfig.INSTANCE.onlyShowOnShift) {
                final int ttIndex = idx.orElse(insertAt) + 1;

                if (Objects.nonNull(mol) && MolDrawConfig.INSTANCE.molecule.showMolecules) {
                    list.add(ttIndex, ClientTooltipComponent.create(
                            Component.translatable("tooltip.moldraw.shift_view_molecule").getVisualOrderText()));
                } else if (Objects.nonNull(alloy) && MolDrawConfig.INSTANCE.alloy.showAlloys) {
                    list.add(ttIndex, ClientTooltipComponent
                            .create(Component.translatable("tooltip.moldraw.shift_view_alloy").getVisualOrderText()));
                }
            }
        }
    }
}
