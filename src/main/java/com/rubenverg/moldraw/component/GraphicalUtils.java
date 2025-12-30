package com.rubenverg.moldraw.component;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.common.util.TriPredicate;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.IntBinaryOperator;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GraphicalUtils {

    public static boolean alwaysDraw(int _x, int _y, int _count) {
        return true;
    }

    public static void plotLine(int x0, int y0, int x1, int y1,
                                TriPredicate<@NotNull Integer, @NotNull Integer, @NotNull Integer> shouldDraw,
                                BiConsumer<Integer, Integer> doDraw) {
        final int dx = Math.abs(x1 - x0), dy = -Math.abs(y1 - y0);
        final int sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1;
        int error = dx + dy;
        int count = 0;
        while (true) {
            if (shouldDraw.test(x0, y0, count++)) doDraw.accept(x0, y0);
            final int e2 = 2 * error;
            if (e2 >= dy) {
                if (x0 == x1) break;
                error += dy;
                x0 += sx;
            }
            if (e2 <= dx) {
                if (y0 == y1) break;
                error += dx;
                y0 += sy;
            }
        }
    }

    public static void plotLine(int x0, int y0, int x1, int y1,
                                TriPredicate<@NotNull Integer, @NotNull Integer, @NotNull Integer> shouldDraw,
                                IntBinaryOperator color,
                                GuiGraphics graphics) {
        plotLine(x0, y0, x1, y1, shouldDraw,
                (xp, yp) -> graphics.fill(xp, yp, xp + 1, yp + 1, color.applyAsInt(xp, yp)));
    }

    public static void plotCircle(int xm, int ym, int r,
                                  TriPredicate<@NotNull Integer, @NotNull Integer, @NotNull Integer> shouldDraw,
                                  BiConsumer<Integer, Integer> doDraw) {
        int x0 = 0, y0 = r, d = 3 - 2 * r;
        while (y0 >= x0) {
            GraphicalUtils.plotLine(xm - y0, ym - x0, xm + y0, ym - x0, shouldDraw, doDraw);
            if (x0 > 0) GraphicalUtils.plotLine(xm - y0, ym + x0, xm + y0, ym + x0, shouldDraw, doDraw);
            if (d < 0) d += 4 * x0++ + 6;
            else {
                if (x0 != y0) {
                    GraphicalUtils.plotLine(xm - x0, ym - y0, xm + x0, ym - y0, shouldDraw, doDraw);
                    GraphicalUtils.plotLine(xm - x0, ym + y0, xm + x0, ym + y0, shouldDraw, doDraw);
                }
                d += 4 * (x0++ - y0--) + 10;
            }
        }
    }

    public static void plotCircle(int xm, int ym, int r,
                                  TriPredicate<@NotNull Integer, @NotNull Integer, @NotNull Integer> shouldDraw,
                                  IntBinaryOperator color, GuiGraphics graphics) {
        plotCircle(xm, ym, r, shouldDraw, (xp, yp) -> graphics.fill(xp, yp, xp + 1, yp + 1, color.applyAsInt(xp, yp)));
    }
}
