package com.rubenverg.moldraw.component;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;

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

        static boolean always(int x, int y, int count) {
            return true;
        }

        static boolean never(int x, int y, int count) {
            return false;
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
                (xp, yp) -> fill(RenderType.gui(), xp, yp, xp + 1, yp + 1,0, color.applyAsInt(xp, yp),graphics));

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
        plotCircle(xm, ym, r, shouldDraw, (xp, yp) -> fill(RenderType.gui(),xp, yp, xp + 1, yp + 1,0, color.applyAsInt(xp, yp),graphics));
    }


    public static void fill(RenderType renderType, int minX, int minY, int maxX, int maxY, int z, int color,GuiGraphics graphics) {
        Matrix4f matrix4f = graphics.pose().last().pose();
        int j;
        if (minX < maxX) {
            j = minX;
            minX = maxX;
            maxX = j;
        }

        if (minY < maxY) {
            j = minY;
            minY = maxY;
            maxY = j;
        }
        VertexConsumer vertexconsumer = graphics.bufferSource().getBuffer(renderType);
        vertexconsumer.defaultColor(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color),  FastColor.ARGB32.alpha(color));
        vertexconsumer.vertex(matrix4f, (float)minX, (float)minY, (float)z).endVertex();
        vertexconsumer.vertex(matrix4f, (float)minX, (float)maxY, (float)z).endVertex();
        vertexconsumer.vertex(matrix4f, (float)maxX, (float)maxY, (float)z).endVertex();
        vertexconsumer.vertex(matrix4f, (float)maxX, (float)minY, (float)z).endVertex();
        vertexconsumer.unsetDefaultColor();


    }

}
