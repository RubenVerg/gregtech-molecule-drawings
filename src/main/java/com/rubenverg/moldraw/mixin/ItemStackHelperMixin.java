package main.java.com.rubenverg.moldraw.mixin;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.mojang.datafixers.util.Either;
import com.rubenverg.moldraw.CustomMaterialLookup;
import com.rubenverg.moldraw.MolDraw;
import com.rubenverg.moldraw.MolDrawConfig;
import com.rubenverg.moldraw.MoleculeTooltipComponent;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.common.gui.JeiTooltip;
import mezz.jei.forge.platform.ItemStackHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.IntSummaryStatistics;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * 把 moldraw 加到 JEI 的 Item tooltip（与 FluidHelperMixin 对称）
 */
@Mixin(value = ItemStackHelper.class, priority = 1000000)
public class ItemStackHelperMixin {

    @Inject(method = "getTooltip(Lmezz/jei/api/gui/builder/ITooltipBuilder;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/TooltipFlag;)V",
            at = @At("TAIL"),
            remap = false,
            require = 0)
    private void moldraw$injectItemTooltips(ITooltipBuilder tooltip, ItemStack ingredient, TooltipFlag tooltipFlag,
                                            CallbackInfo ci) {
        if (!(tooltip instanceof JeiTooltip jeiTooltip)) return;
        if (!MolDrawConfig.INSTANCE.enabled) return;

        final Optional<MaterialStack> materialStackOpt = CustomMaterialLookup.getMaterialEntry(ingredient);
        if (materialStackOpt.isEmpty()) return;
        final var material = materialStackOpt.get().material();
        if (MaterialHelper.isNull(material)) return;

        final var mol = MolDraw.getMolecule(material);
        final var tooltipElements = ((JeiTooltipMixin) jeiTooltip).getLines();

        final OptionalInt idx = IntStream.range(0, tooltipElements.size())
                .filter(i -> tooltipElements.get(i).left()
                        .map(tt -> tt.getString().equals(material.getChemicalFormula()))
                        .orElse(false))
                .reduce((a, b) -> b);

        // 先尝试用彩色公式替换/插入
        MolDraw.tryColorizeFormula(material, idx, tooltipElements);

        // 若存在分子图且配置允许（或按 Shift）
        if (!Objects.isNull(mol) &&
                (!MolDrawConfig.INSTANCE.onlyShowOnShift || GTUtil.isShiftDown())) {
            final int ttIndex = idx.orElse(1) + 1;
            tooltipElements.add(ttIndex, Either.left(FormattedText.of(Component.translatable("tooltip.moldraw.shift_view").getString())));
        }
    }
}