package com.rubenverg.moldraw.mixin;

import com.mojang.datafixers.util.Either;
import mezz.jei.common.gui.JeiTooltip;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(JeiTooltip.class)
public interface JeiTooltipMixin {
	@Accessor
	List<Either<FormattedText, TooltipComponent>> getLines();
}
