package com.rubenverg.moldraw;

import java.awt.*;
import java.math.BigInteger;
import java.util.Objects;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import org.jetbrains.annotations.Nullable;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.rubenverg.moldraw.molecule.Element;
import com.rubenverg.moldraw.molecule.MathUtils;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IOreMaterial;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class MoleculeColorize {

    public static int FALLBACK_COLOR = MathUtils.chatFormattingColor(ChatFormatting.YELLOW);

    public static int configColor(@Nullable String config) {
        final var str = Objects.requireNonNullElse(config, MolDrawConfig.color.defaultColor);
        if (str.length() == 2 && str.charAt(0) == '&') {
            final var formatting = ChatFormatting.getByChar(str.charAt(1));
            return Objects.isNull(formatting) ? FALLBACK_COLOR : MathUtils.chatFormattingColor(formatting);
        } else if (str.length() == 7 && str.charAt(0) == '#') {
            return Color.decode(str)
                .getRGB() | (0xff << 24);
        } else {
            return FALLBACK_COLOR;
        }
    }

    public static double brightness(int color) {
        final int red = color << 16 & 0xff, green = color << 8 & 0xff, blue = color << 0 & 0xff;
        return 0.21 * red + 0.72 * green + 0.07 * blue;
    }

    private static final Object2IntMap<IOreMaterial> COLOR_CACHE = new Object2IntOpenHashMap<>();

    public static void invalidateColorCache() {
        COLOR_CACHE.clear();
    }

    private static int doGetColorForMaterial(IOreMaterial material) {
        final var rgba = material.getRGBA();
        final var color = rgba[3] << 24 | rgba[0] << 16 | rgba[1] << 8 | rgba[2] << 0;
        if ((color & 0xffffff) == 0xffffff && material instanceof Materials gt
            && Materials.FLUID_MAP.containsValue(gt)) {
            final var fluid = Materials.FLUID_MAP.entrySet()
                .stream()
                .filter(
                    e -> e.getValue()
                        .equals(gt))
                .findFirst()
                .orElseThrow()
                .getKey();
            if (fluid.getStillIcon() instanceof TextureAtlasSprite tas) {
                var red = BigInteger.ZERO;
                var green = BigInteger.ZERO;
                var blue = BigInteger.ZERO;
                var count = BigInteger.ZERO;
                for (int fr = 0; fr < tas.getFrameCount(); fr++) {
                    final var frame = tas.getFrameTextureData(fr);
                    for (final var row : frame) for (final var pixel : row) {
                        count = count.add(BigInteger.ONE);
                        red = red.add(BigInteger.valueOf(pixel >> 16 & 0xff));
                        green = green.add(BigInteger.valueOf(pixel >> 8 & 0xff));
                        blue = blue.add(BigInteger.valueOf(pixel >> 0 & 0xff));
                    }
                }
                return red.divide(count)
                    .intValue() << 16
                    | green.divide(count)
                        .intValue() << 8
                    | blue.divide(count)
                        .intValue();
            }
        }
        return color;
    }

    public static int getColorForMaterial(IOreMaterial material) {
        if (!COLOR_CACHE.containsKey(material)) {
            COLOR_CACHE.put(material, doGetColorForMaterial(material));
        }
        return COLOR_CACHE.getInt(material);
    }

    public static int lightenColor(int color) {
        final var arr = new float[3];
        Color.RGBtoHSB(color >> 16 & 0xff, color >> 8 & 0xff, color >> 0 & 0xff, arr);
        arr[2] = Math.max(arr[2], MolDrawConfig.color.minimumBrightness);
        return Color.HSBtoRGB(arr[0], arr[1], arr[2]);
    }

    public static int colorForMaterial(IOreMaterial material) {
        return lightenColor(getColorForMaterial(material));
    }

    public static int getColorForElement(Element element) {
        final var defaultColor = configColor(null);
        if (MolDrawConfig.color.useMaterialColors && Objects.nonNull(element.material))
            return colorForMaterial(element.material);
        else if (element.color instanceof Element.Color.None) return defaultColor;
        else if (element.color instanceof Element.Color.Always always) return always.color();
        else if (element.color instanceof Element.Color.Optional optional)
            return MolDrawConfig.color.colors ? optional.color() : defaultColor;
        return defaultColor;
    }

    public static int colorForElement(Element element) {
        return lightenColor(getColorForElement(element));
    }
}
