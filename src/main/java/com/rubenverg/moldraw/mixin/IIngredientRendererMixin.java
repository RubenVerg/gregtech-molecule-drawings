package com.rubenverg.moldraw.mixin;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.TooltipFlag;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = IIngredientRenderer.class, remap = false)
public interface IIngredientRendererMixin<T> {

    @Shadow
    List<Component> getTooltip(T ingredient, TooltipFlag tooltipFlag);

    /**
     * @author RubenVerg
     * @reason No apparent good way to introduce nontextual components
     */
    @Overwrite
    default void getTooltip(ITooltipBuilder tooltip, T ingredient, TooltipFlag tooltipFlag) {
        for (final var component : getTooltip(ingredient, tooltipFlag)) {
            if (component instanceof ShamComponent(var actual)) tooltip.add(actual);
            else tooltip.add(component);
        }
    }

    @MethodsReturnNonnullByDefault
    record ShamComponent(TooltipComponent actual) implements Component {

        @Override
        public Style getStyle() {
            return Style.EMPTY;
        }

        @Override
        public ComponentContents getContents() {
            return null;
        }

        @Override
        public List<Component> getSiblings() {
            return List.of();
        }

        @Override
        public FormattedCharSequence getVisualOrderText() {
            return FormattedCharSequence.EMPTY;
        }
    }
}
