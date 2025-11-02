package com.rubenverg.moldraw;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.fluids.GTFluid;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

import com.mojang.blaze3d.platform.NativeImage;
import com.rubenverg.moldraw.molecule.Element;
import com.rubenverg.moldraw.molecule.MathUtils;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

public class MoleculeColorize {

    public static int FALLBACK_COLOR = MathUtils.chatFormattingColor(ChatFormatting.YELLOW);

    public static int configColor(@Nullable String config) {
        final var str = Objects.requireNonNullElse(config, MolDrawConfig.INSTANCE.defaultColor);
        if (str.length() == 2 && str.charAt(0) == '§') {
            final var formatting = ChatFormatting.getByCode(str.charAt(1));
            return Objects.isNull(formatting) ? FALLBACK_COLOR : MathUtils.chatFormattingColor(formatting);
        } else if (str.length() == 7 && str.charAt(0) == '#') {
            return Color.decode(str).getRGB() | (0xff << 24);
        } else {
            return FALLBACK_COLOR;
        }
    }

    public static int colorForElement(Element element) {
        final var defaultColor = configColor(null);
        if (MolDrawConfig.INSTANCE.useMaterialColors && !element.material.isNull()) {
            if (element.material.getMaterialARGB() == 0xffffffff &&
                    element.material.getFluid() instanceof GTFluid gtFluid) {
                final var texturePath = IClientFluidTypeExtensions.of(gtFluid.getFluidType()).getStillTexture();
                try {
                    final var resource = Minecraft.getInstance().getResourceManager()
                            .getResourceOrThrow(texturePath.withSuffix(".png").withPrefix("textures/"));
                    NativeImage image;
                    try (final var stream = resource.open()) {
                        image = NativeImage.read(stream);
                    }
                    var red = BigInteger.ZERO;
                    var green = BigInteger.ZERO;
                    var blue = BigInteger.ZERO;
                    for (final var pixel : image.getPixelsRGBA()) {
                        red = red.add(BigInteger.valueOf(FastColor.ABGR32.red(pixel)));
                        green = green.add(BigInteger.valueOf(FastColor.ABGR32.green(pixel)));
                        blue = blue.add(BigInteger.valueOf(FastColor.ABGR32.blue(pixel)));
                    }
                    final var size = BigInteger.valueOf((long) image.getWidth() * image.getHeight());
                    return FastColor.ARGB32.color(0xff, red.divide(size).intValue(), green.divide(size).intValue(),
                            blue.divide(size).intValue());
                } catch (IOException ignored) {

                }
            } else return element.material.getMaterialARGB();
        } else if (element.color instanceof Element.Color.None) return defaultColor;
        else if (element.color instanceof Element.Color.Always always) return always.color();
        else if (element.color instanceof Element.Color.Optional optional)
            return MolDrawConfig.INSTANCE.coloredAtoms ? optional.color() : defaultColor;
        return defaultColor;
    }

    public static Component coloredFormula(MaterialStack stack) {
        if (stack.material().isElement()) {
            final var element = Element.forMaterial(stack.material());
            if (element.isEmpty()) return Component.literal(stack.toString());
            return Component.literal(stack.toString()).withStyle(Style.EMPTY.withColor(colorForElement(element.get())));
        }
        final var components = stack.material().getMaterialComponents();
        if (components.isEmpty()) return Component.literal(stack.toString());
        final var text = Component.empty();
        for (final var component : components) {
            text.append(coloredFormula(component));
        }
        if (stack.amount() == 1) return text;
        final var countedText = Component.empty();
        if (components.size() > 1) countedText.append("(");
        countedText.append(text);
        if (components.size() > 1) countedText.append(")");
        countedText.append(FormattingUtil.toSmallDownNumbers(Long.toString(stack.amount())));
        return countedText;
    }
}
