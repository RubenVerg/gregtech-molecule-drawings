package com.rubenverg.moldraw.mixin;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.util.FormattedCharSequence;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ClientTextTooltip.class, remap = false)
public interface ClientTextTooltipMixin {

    @Accessor
    FormattedCharSequence getText();
}
