package com.rubenverg.moldraw.molecule;

import net.minecraft.ChatFormatting;

import org.joml.*;

import java.lang.Math;
import java.util.Objects;

public class MathUtils {

    public static Matrix2dc UVtoXY = new Matrix2d(
            Math.sqrt(3) / 2, 0.5,
            Math.sqrt(3) / 2, -0.5);

    public static double COS18 = Math.cos(Math.toRadians(18));
    public static double SIN18 = Math.sin(Math.toRadians(18));
    public static float COS18f = (float) COS18;
    public static float SIN18f = (float) SIN18;

    public static double COS30 = Math.cos(Math.toRadians(30));
    public static double SIN30 = Math.sin(Math.toRadians(30));
    public static float COS30f = (float) COS30;
    public static float SIN30f = (float) SIN30;

    public static double COS54 = Math.cos(Math.toRadians(54));
    public static double SIN54 = Math.sin(Math.toRadians(54));
    public static float COS54f = (float) COS54;
    public static float SIN54f = (float) SIN54;

    public static double PHI = (1 + Math.sqrt(5)) / 2;
    public static float PHIf = (float) PHI;

    public static int chatFormattingColor(ChatFormatting formatting) {
        return Objects.requireNonNull(formatting.getColor()) | (0xff << 24);
    }
}
