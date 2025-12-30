package com.rubenverg.moldraw.component;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.IntBinaryOperator;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GraphicalUtils {

    @FunctionalInterface
    public interface DrawPixel {

        void draw(int x, int y);
    }

    @FunctionalInterface
    public interface PixelPredicate {

        boolean test(int x, int y, int count);

        default PixelPredicate not() {
            return (x, y, count) -> !this.test(x, y, count);
        }

        default PixelPredicate and(PixelPredicate that) {
            return (x, y, count) -> this.test(x, y, count) && that.test(x, y, count);
        }

        default PixelPredicate or(PixelPredicate that) {
            return (x, y, count) -> this.test(x, y, count) || that.test(x, y, count);
        }

        default PixelPredicate xor(PixelPredicate that) {
            return (x, y, count) -> this.test(x, y, count) ^ that.test(x, y, count);
        }
    }

    public static boolean alwaysDraw(int _x, int _y, int _count) {
        return true;
    }

    public static void plotLine(int x0, int y0, int x1, int y1, PixelPredicate shouldDraw, DrawPixel doDraw) {
        final int dx = Math.abs(x1 - x0), dy = -Math.abs(y1 - y0);
        final int sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1;
        int error = dx + dy;
        int count = 0;
        while (true) {
            if (shouldDraw.test(x0, y0, count++)) doDraw.draw(x0, y0);
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

    public static void plotLine(int x0, int y0, int x1, int y1, PixelPredicate shouldDraw, IntBinaryOperator color,
                                GuiGraphics graphics) {
        plotLine(x0, y0, x1, y1, shouldDraw,
                (xp, yp) -> graphics.fill(xp, yp, xp + 1, yp + 1, color.applyAsInt(xp, yp)));
    }

    public static void plotCircle(int xm, int ym, int r, PixelPredicate shouldDraw, DrawPixel doDraw) {
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

    public static void plotCircle(int xm, int ym, int r, PixelPredicate shouldDraw, IntBinaryOperator color,
                                  GuiGraphics graphics) {
        plotCircle(xm, ym, r, shouldDraw, (xp, yp) -> graphics.fill(xp, yp, xp + 1, yp + 1, color.applyAsInt(xp, yp)));
    }
}
