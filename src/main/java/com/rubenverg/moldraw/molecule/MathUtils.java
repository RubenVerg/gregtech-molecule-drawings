package com.rubenverg.moldraw.molecule;

import net.minecraft.util.Mth;

import org.joml.*;

import java.lang.Math;

public class MathUtils {

    public static Matrix2dc T2S = new Matrix2d(
            Math.sqrt(3) / 2, 0.5,
            Math.sqrt(3) / 2, -0.5);
    public static Matrix2dc S2T = new Matrix2d();

    static {
        T2S.invert((Matrix2d) S2T);
    }

    public static void triangleToSquare(Vector2fc vec, Vector2f result) {
        vec.mul(T2S, result);
        // MolDraw.LOGGER.info("{} -> {}", vec, result);
    }

    public static void triangleToSquare(Vector2f vec) {
        vec.mul(T2S);
        // MolDraw.LOGGER.info("{} -> {}", vec, result);
    }

    public static void squareToTriangle(Vector2f vec) {
        vec.mul(S2T);
    }

    public static double roundAway(double number) {
        if (number < 0) return Math.floor(number);
        return Math.ceil(number);
    }

    public static float roundAway(float number) {
        if (number < 0) return Mth.floor(number);
        return Mth.ceil(number);
    }

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
}
