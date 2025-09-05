package com.rubenverg.moldraw;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import com.mojang.datafixers.util.Pair;
import com.rubenverg.moldraw.molecule.*;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import java.lang.Math;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import javax.annotation.ParametersAreNonnullByDefault;

public record MoleculeTooltipComponent(
                                       Molecule molecule)
        implements TooltipComponent {

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    public static class ClientMoleculeTooltipComponent implements ClientTooltipComponent {

        public static int SCALE = 20;
        public static int COLOR = Objects.requireNonNull(ChatFormatting.YELLOW.getColor()) | (0xff << 24);
        public static int DEBUG_COLOR = Objects.requireNonNull(ChatFormatting.RED.getColor()) | (0xff << 24);

        private final Molecule molecule;
        private final Vector2i xySize;
        private final Vector2f xyStart;
        private final boolean atomAtTop;
        private final Map<Element, Integer> elementWidths = new HashMap<>();

        private Vector2i toScreen(int lineHeight, Vector2f xy) {
            var result = new Vector2f();
            xy.sub(xyStart, result);
            result.mul(SCALE);
            return new Vector2i((int) result.x + 8, -(int) result.y + (atomAtTop ? lineHeight / 2 : 3));
        }

        public ClientMoleculeTooltipComponent(MoleculeTooltipComponent component) {
            this.molecule = component.molecule();
            final var bounds = molecule.bounds();
            final Vector2f diff = new Vector2f();
            bounds.getSecond().sub(bounds.getFirst(), diff);
            diff.mul(SCALE);
            diff.ceil();
            this.xySize = new Vector2i((int) diff.x, (int) diff.y);
            this.xyStart = new Vector2f(bounds.getFirst().x, bounds.getSecond().y);
            this.atomAtTop = molecule.atoms().stream().anyMatch(atom -> {
                final var distanceFromTop = Math.abs(xyStart.y - atom.position().y);
                final var invisible = atom.isInvisible();
                return distanceFromTop < 0.1 && !invisible;
            });
        }

        @Override
        public int getWidth(Font font) {
            return xySize.x + 32;
        }

        @Override
        public int getHeight() {
            return xySize.y + 20;
        }

        @Override
        public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix,
                               MultiBufferSource.BufferSource bufferSource) {
            COLOR = Objects.requireNonNull(ChatFormatting.YELLOW.getColor()) | (0xff << 24);

            elementWidths.clear();
            var mat = new Matrix4f(matrix);
            for (final var elem : this.molecule.contents()) {
                if (elem instanceof Atom atom) {
                    final var xyPosition = toScreen(font.lineHeight, atom.position());
                    final var translation = new Vector3f(xyPosition.x, xyPosition.y, 0);
                    mat.translate(translation);
                    final var width = font.width(atom.element().symbol);
                    final var centerTranslation = new Vector3f(Mth.floor(-(float) width / 2) + 1, 1, 0);
                    mat.translate(centerTranslation);
                    font.drawInBatch(atom.element().symbol, (float) mouseX, (float) mouseY, COLOR, false, mat,
                            bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
                    mat.translate(centerTranslation.negate());
                    mat.translate(translation.negate());
                    elementWidths.put(atom.element(), width);
                    if (MolDrawConfig.INSTANCE.debugMode) {
                        final var debugTranslation = new Vector3f(xyPosition.x - 5, xyPosition.y - 2, 3);
                        mat.translate(debugTranslation);
                        font.drawInBatch(Integer.toString(atom.index()), (float) mouseX, (float) mouseY, DEBUG_COLOR,
                                false, mat, bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
                        mat.translate(debugTranslation.negate());
                    }
                } else if (elem instanceof Parens pp) {
                    final var bounds = this.molecule.subset(pp.atoms()).bounds();
                    final var xySub = toScreen(font.lineHeight,
                            new Vector2f(bounds.getSecond().x, bounds.getFirst().y));
                    xySub.add(9, font.lineHeight - 4);
                    final var subTranslation = new Vector3f(xySub.x, xySub.y, 0);
                    mat.translate(subTranslation);
                    font.drawInBatch(pp.sub(), (float) mouseX, (float) mouseY, COLOR, false, mat, bufferSource,
                            Font.DisplayMode.NORMAL, 0, 15728880);
                    mat.translate(subTranslation.negate());
                    final var xySup = toScreen(font.lineHeight,
                            new Vector2f(bounds.getSecond().x, bounds.getSecond().y));
                    xySup.add(9, -4);
                    final var supTranslation = new Vector3f(xySup.x, xySup.y, 0);
                    mat.translate(supTranslation);
                    font.drawInBatch(pp.sup(), (float) mouseX, (float) mouseY, COLOR, false, mat, bufferSource,
                            Font.DisplayMode.NORMAL, 0, 15728880);
                    mat.translate(supTranslation.negate());
                }
            }
        }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
            for (final var elem : this.molecule.contents()) {
                if (elem instanceof Bond bond) {
                    final var atomA = this.molecule.getAtom(bond.a()).orElseThrow();
                    final var atomAWidth = elementWidths.get(atomA.element());
                    final var atomAInvisible = atomA.isInvisible();
                    final var atomB = this.molecule.getAtom(bond.b()).orElseThrow();
                    final var atomBWidth = elementWidths.get(atomB.element());
                    final var atomBInvisible = atomB.isInvisible();
                    final var start = toScreen(font.lineHeight, atomA.position());
                    start.add(x, y);
                    start.add(0, font.lineHeight / 2);
                    final var end = toScreen(font.lineHeight, atomB.position());
                    end.add(x, y);
                    end.add(0, font.lineHeight / 2);
                    final BiPredicate<@NotNull Integer, @NotNull Integer> isCloseToAtom = (xt, yt) -> {
                        if (atomAInvisible && atomBInvisible)
                            return true;
                        Vector2ic t = new Vector2i(xt, yt);
                        var diff = new Vector2i();
                        start.sub(t, diff);
                        diff.absolute();
                        if (diff.x < atomAWidth * 2 / 3 && (!atomAInvisible && diff.y < font.lineHeight * 2 / 3))
                            return false;
                        end.sub(t, diff);
                        diff.absolute();
                        if (diff.x < atomBWidth * 2 / 3 && (!atomBInvisible && diff.y < font.lineHeight * 2 / 3))
                            return false;
                        return true;
                    };
                    double dy = end.y - start.y, dx = end.x - start.x;
                    double length = Math.hypot(dx, dy);
                    final BiPredicate<@NotNull Integer, @NotNull Integer> isCloseToAtomAndOnLine = (xt, yt) -> {
                        if (!isCloseToAtom.test(xt, yt)) return false;
                        return Math.hypot(xt - start.x, yt - start.y) % 3 < 1;
                    };
                    int addX = (int) Math.round(dy / length * 2), addY = (int) -Math.round(dx / length * 2);
                    int addHX = (int) Math.round(dy / length), addHY = (int) -Math.round(dx / length);
                    List<Pair<Integer, Integer>> allTargets = new ArrayList<>();
                    plotLine(addX, addY, -addX, -addY, (_xt, _yt) -> true,
                            (xp, yp) -> allTargets.add(new Pair<>(xp, yp)));
                    switch (bond.type()) {
                        case SINGLE:
                            plotLine(start.x, start.y, end.x, end.y, isCloseToAtom, COLOR, guiGraphics);
                            break;
                        case DOUBLE:
                            plotLine(start.x, start.y, end.x, end.y, isCloseToAtom, COLOR, guiGraphics);
                            plotLine(start.x + addX, start.y + addY, end.x + addX, end.y + addY, isCloseToAtom,
                                    COLOR, guiGraphics);
                            break;
                        case DOUBLE_CENTERED:
                            plotLine(start.x + addHX, start.y + addHY, end.x + addHX, end.y + addHY, isCloseToAtom,
                                    COLOR, guiGraphics);
                            plotLine(start.x - addHX, start.y - addHY, end.x - addHX, end.y - addHY, isCloseToAtom,
                                    COLOR, guiGraphics);
                            break;
                        case TRIPLE:
                            plotLine(start.x, start.y, end.x, end.y, isCloseToAtom, COLOR, guiGraphics);
                            plotLine(start.x + addX, start.y + addY, end.x + addX, end.y + addY, isCloseToAtom,
                                    COLOR, guiGraphics);
                            plotLine(start.x - addX, start.y - addY, end.x - addX, end.y - addY, isCloseToAtom,
                                    COLOR, guiGraphics);
                            break;
                        case INWARD:
                        case OUTWARD:
                            for (final var pair : allTargets) {
                                plotLine(start.x, start.y, end.x + pair.getFirst(), end.y + pair.getSecond(),
                                        bond.type() == Bond.Type.INWARD ? isCloseToAtomAndOnLine : isCloseToAtom, COLOR,
                                        guiGraphics);
                            }
                            break;
                    }
                } else if (elem instanceof Parens pp) {
                    final var bounds = this.molecule.subset(pp.atoms()).bounds();
                    final var xyMin = toScreen(font.lineHeight, bounds.getFirst());
                    xyMin.add(x, y);
                    xyMin.add(-4, font.lineHeight + 1);
                    final var xyMax = toScreen(font.lineHeight, bounds.getSecond());
                    xyMax.add(x, y);
                    xyMax.add(4, -2);
                    guiGraphics.hLine(xyMin.x - 2, xyMin.x + 2, xyMin.y, COLOR);
                    guiGraphics.hLine(xyMin.x - 2, xyMin.x + 2, xyMax.y, COLOR);
                    guiGraphics.hLine(xyMax.x + 2, xyMax.x - 2, xyMin.y, COLOR);
                    guiGraphics.hLine(xyMax.x + 2, xyMax.x - 2, xyMax.y, COLOR);
                    guiGraphics.vLine(xyMin.x - 2, xyMin.y, xyMax.y, COLOR);
                    guiGraphics.vLine(xyMax.x + 2, xyMin.y, xyMax.y, COLOR);
                }
            }
        }

        public static void plotLine(int x0, int y0, int x1, int y1,
                                    BiPredicate<@NotNull Integer, @NotNull Integer> shouldDraw,
                                    BiConsumer<Integer, Integer> doDraw) {
            final int dx = Math.abs(x1 - x0), dy = -Math.abs(y1 - y0);
            final int sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1;
            int error = dx + dy;
            while (true) {
                // This is horribly unoptimized
                if (shouldDraw.test(x0, y0)) doDraw.accept(x0, y0);
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
                                    BiPredicate<@NotNull Integer, @NotNull Integer> shouldDraw, int color,
                                    GuiGraphics graphics) {
            plotLine(x0, y0, x1, y1, shouldDraw, (xp, yp) -> graphics.fill(xp, yp, xp + 1, yp + 1, color));
        }
    }
}
