package main.java.com.rubenverg.moldraw.mixin;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.rubenverg.moldraw.CustomMaterialLookup;
import com.rubenverg.moldraw.MolDraw;
import com.rubenverg.moldraw.MolDrawConfig;
import com.rubenverg.moldraw.MoleculeTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.item.Item;
import net.minecraft.world.item.ItemStack;
import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.api.stack.ItemEmiStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * 给 EMI 的 ItemEmiStack 加上分子渲染（参考 FluidEmiStackMixin）
 */
@Mixin(value = ItemEmiStack.class, remap = false)
public class ItemEmiStackMixin {

    @Shadow
    @Final
    private ItemStack stack;

    @Unique
    private static String moldraw$simpleGetText(net.minecraft.util.FormattedCharSequence seq) {
        return seq == null ? "" : seq.getString();
    }

    @Inject(method = "getTooltip",
            at = @At(value = "INVOKE",
                     target = "Ldev/emi/emi/api/render/EmiTooltipComponents;appendModName(Ljava/util/List;Ljava/lang/String;)V"),
            remap = false,
            require = 0)
    private void moldraw$addItemTooltip(CallbackInfoReturnable<List<ClientTooltipComponent>> cir,
                                        @Local(name = "list") List<ClientTooltipComponent> list) {
        if (!MolDrawConfig.INSTANCE.enabled) return;

        final Optional<MaterialStack> materialStackOpt = CustomMaterialLookup.getMaterialEntry(stack);
        if (materialStackOpt.isEmpty()) return;
        final var material = materialStackOpt.get().material();
        if (Objects.isNull(material)) return;
        if (Objects.isNull(material.getMaterialComponents()) && !material.isElement()) return;

        final var mol = MolDraw.getMolecule(material);

        final OptionalInt idx = IntStream.range(0, list.size())
                .filter(i -> list.get(i) instanceof ClientTextTooltip ctt &&
                        moldraw$simpleGetText(ctt.getText()).equals(material.getChemicalFormula()))
                .findFirst();

        if (mol != null && (!MolDrawConfig.INSTANCE.onlyShowOnShift || GTUtil.isShiftDown())) {
            final ClientTooltipComponent comp = ClientTooltipComponent.create(new MoleculeTooltipComponent(mol));
            list.add(idx.orElse(list.size() - 1) + 1, comp);
        }
    }
}