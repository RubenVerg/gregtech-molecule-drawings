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
import net.minecraftforge.common.util.TriPredicate;

import com.mojang.datafixers.util.Pair;
import com.rubenverg.moldraw.molecule.*;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import java.lang.Math;
import java.util.*;
import java.util.List;
import java.util.function.*;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.rubenverg.moldraw.MoleculeColorize.*;

public record MoleculeTooltipComponent(
                                       Molecule molecule)
        implements TooltipComponent {

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    public static class ClientMoleculeTooltipComponent implements ClientTooltipComponent {

        public static int DEBUG_COLOR = MathUtils.chatFormattingColor(ChatFormatting.RED);

        private final Molecule molecule;
        private final Vector2i xySize;
        private final Vector2f xyStart;
        private final boolean atomAtTop;
        private final boolean atomAtTopTop;
        private final boolean atomAtBotBot;
        private final boolean atomAtLefLef;
        private final boolean parenAtLef;
        private final Map<Element.Counted, Integer> elementWidths = new HashMap<>();

        private UnaryOperator<Vector2f> toScaledFactory(int lineHeight) {
            return xy -> {
                var result = new Vector2f();
                xy.sub(xyStart, result);
                result.mul(MolDrawConfig.INSTANCE.scale);
                return new Vector2f(result.x + 8 + (atomAtLefLef ? 12 : 0) + (parenAtLef ? 6 : 0),
                        -result.y + (atomAtTopTop ? lineHeight * 3 / 2f : atomAtTop ? lineHeight / 2f : 3));
            };
        }

        private Function<Atom, Pair<Vector2f, Vector2f>> sizeOfAtomFactory(int lineHeight) {
            return atom -> {
                float x0 = 0, x1 = 0, y0 = 0, y1 = 0;
                x0 += elementWidths.getOrDefault(atom.element(), 0) / 2f;
                x1 += elementWidths.getOrDefault(atom.element(), 0) / 2f;
                y0 += 1;
                y1 += lineHeight + 1;
                if (atom.right().isPresent()) {
                    x1 += 1 + elementWidths.getOrDefault(atom.right().get(), 0);
                }
                if (atom.left().isPresent()) {
                    x0 += 1 + elementWidths.getOrDefault(atom.left().get(), 0);
                }
                if (atom.above().isPresent()) {
                    y0 += 1 + lineHeight;
                    x0 = Math.max(x0, elementWidths.getOrDefault(atom.above().get(), 0) / 2f);
                    x1 = Math.max(x1, elementWidths.getOrDefault(atom.above().get(), 0) / 2f);
                }
                if (atom.below().isPresent()) {
                    y1 += 1 + lineHeight;
                    x0 = Math.max(x0, elementWidths.getOrDefault(atom.below().get(), 0) / 2f);
                    x1 = Math.max(x1, elementWidths.getOrDefault(atom.below().get(), 0) / 2f);
                }
                return new Pair<>(new Vector2f(x0, y0), new Vector2f(x1, y1));
            };
        }

        /*
         * private Vector2i toScreen(int lineHeight, Vector2f xy) {
         * var result = new Vector2f();
         * xy.sub(xyStart, result);
         * result.mul(MolDrawConfig.INSTANCE.scale);
         * return new Vector2i((int) result.x + 8 + (atomAtLefLef ? 12 : 0),
         * -(int) result.y + (atomAtTopTop ? lineHeight * 3 / 2 : atomAtTop ? lineHeight / 2 : 3));
         * }
         */

        public Vector2i floored(Vector2fc vec) {
            return new Vector2i((int) vec.x(), (int) vec.y());
        }

        public ClientMoleculeTooltipComponent(MoleculeTooltipComponent component) {
            this.molecule = component.molecule();
            final var bounds = molecule.bounds();
            final Vector2f diff = new Vector2f();
            bounds.getSecond().sub(bounds.getFirst(), diff);
            diff.mul(MolDrawConfig.INSTANCE.scale);
            diff.ceil();
            this.xySize = new Vector2i((int) diff.x, (int) diff.y);
            this.xyStart = new Vector2f(bounds.getFirst().x, bounds.getSecond().y);
            this.atomAtTop = molecule.atoms().stream().anyMatch(atom -> {
                final var distanceFromTop = Math.abs(xyStart.y - atom.position().y);
                final var invisible = atom.isInvisible();
                return distanceFromTop < 0.1 && !invisible;
            });
            this.atomAtTopTop = molecule.atoms().stream().anyMatch(atom -> {
                final var distanceFromTop = Math.abs(xyStart.y - atom.position().y);
                final var invisible = atom.isInvisible();
                final var hasAbove = atom.above().isPresent();
                return distanceFromTop < 0.1 && !invisible && hasAbove;
            });
            this.atomAtBotBot = molecule.atoms().stream().anyMatch(atom -> {
                final var distanceFromBot = Math.abs(bounds.getFirst().y - atom.position().y);
                final var invisible = atom.isInvisible();
                final var hasBelow = atom.below().isPresent();
                return distanceFromBot < 0.1 && !invisible && hasBelow;
            });
            this.atomAtLefLef = molecule.atoms().stream().anyMatch(atom -> {
                final var distanceFromLef = Math.abs(bounds.getFirst().x - atom.position().x);
                final var invisible = atom.isInvisible();
                final var hasLef = atom.left().isPresent();
                return distanceFromLef < 0.1 && !invisible && hasLef;
            });
            this.parenAtLef = molecule.atoms().stream().anyMatch(atom -> {
                final var distanceFromLef = Math.abs(bounds.getFirst().x - atom.position().x);
                if (distanceFromLef > 0.1) return false;
                return molecule.contents().stream().anyMatch(el -> el instanceof Parens parens &&
                        Arrays.stream(parens.atoms()).anyMatch(d -> d == atom.index()));
            });
        }

        @Override
        public int getWidth(Font font) {
            return xySize.x + 32 + (atomAtLefLef ? 12 : 0) + (parenAtLef ? 6 : 0);
        }

        @Override
        public int getHeight() {
            return xySize.y + 20 + (atomAtBotBot ? 10 : 0) + (atomAtTopTop ? 10 : 0);
        }

        @Override
        public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix,
                               MultiBufferSource.BufferSource bufferSource) {
            final var defaultColor = configColor(null);
            elementWidths.clear();
            var mat = new Matrix4f(matrix);
            for (final var elem : this.molecule.contents()) {
                if (elem instanceof Atom atom) {
                    final var xyPosition = floored(toScaledFactory(font.lineHeight).apply(atom.position()));
                    final var translation = new Vector3f(xyPosition.x, xyPosition.y, 0);
                    mat.translate(translation);
                    final var width = font.width(atom.element().toString());
                    final var centerTranslation = new Vector3f(Mth.floor(-(float) width / 2) + 1, 1, 0);
                    mat.translate(centerTranslation);
                    if (!atom.element().element().invisible) font.drawInBatch(atom.element().toString(), (float) mouseX,
                            (float) mouseY, colorForElement(atom.element().element()), false, mat,
                            bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
                    mat.translate(centerTranslation.negate());
                    mat.translate(translation.negate());
                    elementWidths.put(atom.element(), atom.element().element().invisible ? 0 : width);
                    if (atom.right().isPresent()) {
                        final var rightTranslation = new Vector3f(xyPosition.x + Mth.floor((float) width / 2) + 1,
                                xyPosition.y + 1, 0);
                        mat.translate(rightTranslation);
                        if (!atom.right().get().element().invisible) font.drawInBatch(atom.right().get().toString(),
                                (float) mouseX, (float) mouseY, colorForElement(atom.right().get().element()), false,
                                mat, bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
                        elementWidths.put(atom.right().get(),
                                atom.right().get().element().invisible ? 0 : font.width(atom.right().get().toString()));
                        mat.translate(rightTranslation.negate());
                    }
                    if (atom.left().isPresent()) {
                        final var leftWidth = font.width(atom.left().get().toString());
                        final var leftTranslation = new Vector3f(
                                xyPosition.x - leftWidth + Mth.floor(-(float) width / 2), xyPosition.y + 1, 0);
                        mat.translate(leftTranslation);
                        if (!atom.left().get().element().invisible) font.drawInBatch(atom.left().get().toString(),
                                (float) mouseX, (float) mouseY, colorForElement(atom.left().get().element()), false,
                                mat, bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
                        elementWidths.put(atom.left().get(), atom.left().get().element().invisible ? 0 : leftWidth);
                        mat.translate(leftTranslation.negate());
                    }
                    if (atom.above().isPresent()) {
                        final var aboveWidth = font.width(atom.above().get().toString());
                        final var aboveTranslation = new Vector3f(xyPosition.x + Mth.floor(-(float) aboveWidth / 2) + 1,
                                xyPosition.y - font.lineHeight + 1, 0);
                        mat.translate(aboveTranslation);
                        if (!atom.above().get().element().invisible) font.drawInBatch(atom.above().get().toString(),
                                (float) mouseX, (float) mouseY, colorForElement(atom.above().get().element()), false,
                                mat, bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
                        elementWidths.put(atom.above().get(), atom.above().get().element().invisible ? 0 : aboveWidth);
                        mat.translate(aboveTranslation.negate());
                    }
                    if (atom.below().isPresent()) {
                        final var belowWidth = font.width(atom.below().get().toString());
                        final var belowTranslation = new Vector3f(xyPosition.x + Mth.floor(-(float) belowWidth / 2) + 1,
                                xyPosition.y + font.lineHeight + 1, 0);
                        mat.translate(belowTranslation);
                        if (!atom.below().get().element().invisible) font.drawInBatch(atom.below().get().toString(),
                                (float) mouseX, (float) mouseY, colorForElement(atom.below().get().element()), false,
                                mat, bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
                        elementWidths.put(atom.below().get(), atom.below().get().element().invisible ? 0 : belowWidth);
                        mat.translate(belowTranslation.negate());
                    }
                    if (MolDrawConfig.INSTANCE.debugMode) {
                        final var debugTranslation = new Vector3f(xyPosition.x - 5, xyPosition.y - 2, 3);
                        mat.translate(debugTranslation);
                        font.drawInBatch(Integer.toString(atom.index()), (float) mouseX, (float) mouseY, DEBUG_COLOR,
                                false, mat, bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
                        mat.translate(debugTranslation.negate());
                    }
                } else if (elem instanceof Parens pp) {
                    final var bounds = this.molecule.subset(pp.atoms()).boundsWithSize(toScaledFactory(font.lineHeight),
                            sizeOfAtomFactory(font.lineHeight));
                    final var xySub = new Vector2i((int) bounds.getSecond().x, (int) bounds.getSecond().y);
                    xySub.add(7, -2);
                    final var subTranslation = new Vector3f(xySub.x, xySub.y, 0);
                    mat.translate(subTranslation);
                    font.drawInBatch(pp.sub(), (float) mouseX, (float) mouseY, defaultColor, false, mat, bufferSource,
                            Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
                    mat.translate(subTranslation.negate());
                    final var xySup = new Vector2i((int) bounds.getSecond().x, (int) bounds.getFirst().y);
                    xySup.add(7, -4);
                    final var supTranslation = new Vector3f(xySup.x, xySup.y, 0);
                    mat.translate(supTranslation);
                    font.drawInBatch(pp.sup(), (float) mouseX, (float) mouseY, defaultColor, false, mat, bufferSource,
                            Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
                    mat.translate(supTranslation.negate());
                }
            }
        }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
            final var defaultColor = configColor(null);
            final var ts = toScaledFactory(font.lineHeight);
            for (final var elem : this.molecule.contents()) {
                if (elem instanceof Bond bond) {
                    if (Objects.isNull(this.molecule.getAtom(bond.a())) ||
                            Objects.isNull(this.molecule.getAtom(bond.b())))
                        continue;
                    final var atomA = this.molecule.getAtom(bond.a()).orElseThrow();
                    final var atomAWidth = elementWidths.get(atomA.element());
                    final var atomAAbove = atomA.above().map(elementWidths::get);
                    final var atomARight = atomA.right().map(elementWidths::get);
                    final var atomABelow = atomA.below().map(elementWidths::get);
                    final var atomALeft = atomA.left().map(elementWidths::get);
                    final var atomAInvisible = atomA.isInvisible();
                    final var atomB = this.molecule.getAtom(bond.b()).orElseThrow();
                    final var atomBWidth = elementWidths.get(atomB.element());
                    final var atomBAbove = atomB.above().map(elementWidths::get);
                    final var atomBRight = atomB.right().map(elementWidths::get);
                    final var atomBBelow = atomB.below().map(elementWidths::get);
                    final var atomBLeft = atomB.left().map(elementWidths::get);
                    final var atomBInvisible = atomB.isInvisible();
                    final var start = floored(ts.apply(atomA.position()));
                    start.add(x, y);
                    start.add(0, font.lineHeight / 2);
                    final var end = floored(ts.apply(atomB.position()));
                    end.add(x, y);
                    end.add(0, font.lineHeight / 2);
                    final TriPredicate<@NotNull Integer, @NotNull Integer, @NotNull Integer> notCloseToAtom = (xt, yt,
                                                                                                               _c) -> {
                        if (atomAInvisible && atomBInvisible)
                            return true;
                        Vector2ic t = new Vector2i(xt, yt);
                        var diff = new Vector2i();
                        start.sub(t, diff);
                        diff.absolute();
                        if (diff.x < atomAWidth * 2 / 3 && (!atomAInvisible && diff.y < font.lineHeight * 2 / 3))
                            return false;
                        if (atomAAbove.isPresent() && diff.x < atomAAbove.get() * 2 / 3 &&
                                Math.abs((start.y - font.lineHeight - 1) - yt) < font.lineHeight * 2 / 3)
                            return false;
                        if (atomABelow.isPresent() && diff.x < atomABelow.get() * 2 / 3 &&
                                Math.abs((start.y + font.lineHeight + 1) - yt) < font.lineHeight * 2 / 3)
                            return false;
                        if (atomARight.isPresent() &&
                                Math.abs((start.x + (atomAWidth + atomARight.get()) / 2 + 1) - xt) <
                                        atomARight.get() * 2 / 3 &&
                                diff.y < font.lineHeight * 2 / 3)
                            return false;
                        if (atomALeft.isPresent() && Math.abs((start.x - (atomAWidth + atomALeft.get()) / 2 - 1) - xt) <
                                atomALeft.get() * 2 / 3 && diff.y < font.lineHeight * 2 / 3)
                            return false;
                        end.sub(t, diff);
                        diff.absolute();
                        if (diff.x < atomBWidth * 2 / 3 && (!atomBInvisible && diff.y < font.lineHeight * 2 / 3))
                            return false;
                        if (atomBAbove.isPresent() && diff.x < atomBAbove.get() * 2 / 3 &&
                                Math.abs((end.y - font.lineHeight - 1) - yt) < font.lineHeight * 2 / 3)
                            return false;
                        if (atomBBelow.isPresent() && diff.x < atomBBelow.get() * 2 / 3 &&
                                Math.abs((end.y + font.lineHeight + 1) - yt) < font.lineHeight * 2 / 3)
                            return false;
                        if (atomBRight.isPresent() &&
                                Math.abs((end.x + (atomBWidth + atomBRight.get()) / 2 + 1) - xt) <
                                        atomBRight.get() * 2 / 3 &&
                                diff.y < font.lineHeight * 2 / 3)
                            return false;
                        if (atomBLeft.isPresent() && Math.abs((end.x - (atomBWidth + atomBLeft.get()) / 2 - 1) - xt) <
                                atomBLeft.get() * 2 / 3 && diff.y < font.lineHeight * 2 / 3)
                            return false;
                        return true;
                    };
                    double dy = end.y - start.y, dx = end.x - start.x;
                    double length = Math.hypot(dx, dy);
                    final BiFunction<Integer, Integer, TriPredicate<@NotNull Integer, @NotNull Integer, @NotNull Integer>> notCloseToAtomAndBands = (m,
                                                                                                                                                     b) -> (xt,
                                                                                                                                                            yt,
                                                                                                                                                            count) -> {
                                                                                                                                                         if (!notCloseToAtom
                                                                                                                                                                 .test(xt,
                                                                                                                                                                         yt,
                                                                                                                                                                         count))
                                                                                                                                                             return false;
                                                                                                                                                         return Math
                                                                                                                                                                 .hypot(start.x -
                                                                                                                                                                         xt,
                                                                                                                                                                         start.y -
                                                                                                                                                                                 yt) %
                                                                                                                                                                 m <
                                                                                                                                                                 b;
                                                                                                                                                     };
                    final BiFunction<Integer, Integer, TriPredicate<@NotNull Integer, @NotNull Integer, @NotNull Integer>> notCloseToAtomAndDot = (m,
                                                                                                                                                   b) -> (xt,
                                                                                                                                                          yt,
                                                                                                                                                          count) -> {
                                                                                                                                                       if (!notCloseToAtom
                                                                                                                                                               .test(xt,
                                                                                                                                                                       yt,
                                                                                                                                                                       count))
                                                                                                                                                           return false;
                                                                                                                                                       return count %
                                                                                                                                                               m <
                                                                                                                                                               b;
                                                                                                                                                   };
                    int addX = (int) Math.round(dy / length * 2), addY = (int) -Math.round(dx / length * 2);
                    int addHX = (int) Math.round(dy / length), addHY = (int) -Math.round(dx / length);
                    int colorA = colorForElement(atomA.element().element());
                    int colorB = colorForElement(atomB.element().element());
                    IntBinaryOperator color = (xp, yp) -> {
                        final var d2a = Math.pow(xp - start.x, 2) + Math.pow(yp - start.y, 2);
                        final var d2b = Math.pow(xp - end.x, 2) + Math.pow(yp - end.y, 2);
                        return d2a < d2b ? colorA : colorB;
                    };
                    List<Vector2i> allTargets = new ArrayList<>();
                    plotLine(addX * 3 / 2, addY * 3 / 2, -addX * 3 / 2, -addY * 3 / 2, (_xt, _yt, _c) -> true,
                            (xp, yp) -> {
                                allTargets.add(new Vector2i(xp / 2, yp / 2));
                                allTargets.add(new Vector2i((xp + 1) / 2, yp / 2));
                                allTargets.add(new Vector2i(xp / 2, (yp + 1) / 2));
                                allTargets.add(new Vector2i((xp + 1) / 2, (yp + 1) / 2));
                            });
                    final var thickness = bond.totalThickness();
                    final var starting = bond.centered() ? (thickness - 1) / 2f : (float) ((thickness - 1) / 2);
                    var done = bond.lines().length > 0 && bond.lines()[0].thick ? 1 : 0;
                    for (var i = 0; i < bond.lines().length; done += bond.lines()[i].thick ? 3 : 1, i++) {
                        final var delta = done - starting;
                        final var sX = Mth.floor(delta) * addX + (int) (Mth.frac(delta) * 2) * addHX;
                        final var sY = Mth.floor(delta) * addY + (int) (Mth.frac(delta) * 2) * addHY;
                        switch (bond.lines()[i]) {
                            case SOLID -> plotLine(start.x + sX, start.y + sY, end.x + sX, end.y + sY, notCloseToAtom,
                                    color, guiGraphics);
                            case DOTTED -> plotLine(start.x + sX, start.y + sY, end.x + sX, end.y + sY,
                                    notCloseToAtomAndDot.apply(2, 1), color, guiGraphics);
                            case INWARD, OUTWARD -> {
                                final var shouldDraw = bond.lines()[i] == Bond.Line.INWARD ?
                                        notCloseToAtomAndBands.apply(3, 1) : notCloseToAtom;
                                for (final var pair : allTargets) {
                                    plotLine(start.x + sX, start.y + sY, end.x + pair.x + sX, end.y + pair.y + sY,
                                            shouldDraw, color, guiGraphics);
                                }
                            }
                            case THICK -> {
                                for (final var pair : allTargets) {
                                    plotLine(start.x + pair.x + sX, start.y + pair.y + sY, end.x + pair.x + sX,
                                            end.y + pair.y + sY, notCloseToAtom, color, guiGraphics);
                                }
                            }
                        }
                    }
                } else if (elem instanceof Parens pp) {
                    final var bounds = this.molecule.subset(pp.atoms()).boundsWithSize(toScaledFactory(font.lineHeight),
                            sizeOfAtomFactory(font.lineHeight));
                    final var xyMin = floored(bounds.getFirst());
                    xyMin.add(x, y);
                    xyMin.add(-2, -1);
                    final var xyMax = floored(bounds.getSecond());
                    xyMax.add(x, y);
                    xyMax.add(2, 1);
                    guiGraphics.hLine(xyMin.x - 2, xyMin.x + 2, xyMin.y, defaultColor);
                    guiGraphics.hLine(xyMin.x - 2, xyMin.x + 2, xyMax.y, defaultColor);
                    guiGraphics.hLine(xyMax.x + 2, xyMax.x - 2, xyMin.y, defaultColor);
                    guiGraphics.hLine(xyMax.x + 2, xyMax.x - 2, xyMax.y, defaultColor);
                    guiGraphics.vLine(xyMin.x - 2, xyMin.y, xyMax.y, defaultColor);
                    guiGraphics.vLine(xyMax.x + 2, xyMin.y, xyMax.y, defaultColor);
                } else if (elem instanceof CircleTransformation ct) {
                    final var centroid = Arrays.stream(ct.atoms())
                            .mapToObj(idx -> this.molecule.getAtom(idx).get().position())
                            .reduce(new Vector2f(), (a, b) -> new Vector2f(a).add(new Vector2f(b)))
                            .div(ct.atoms().length);
                    for (int part = 0; part < 128; part++) {
                        final var angle = (float) part / 64 * Mth.PI;
                        final var u = new Vector2f(Mth.cos(angle), Mth.sin(angle));
                        final var p = u.mul(ct.A()).add(centroid);
                        final var r = floored(toScaledFactory(font.lineHeight).apply(p)).add(x,
                                y + font.lineHeight / 2);
                        guiGraphics.fill(r.x, r.y, r.x + 1, r.y + 1, defaultColor);
                    }
                    // final var cc = toScreen(font.lineHeight, centroid).add(x, y + font.lineHeight / 2);
                    // guiGraphics.fill(cc.x, cc.y, cc.x + 1, cc.y + 1, DEBUG_COLOR);
                }
            }
        }

        public static void plotLine(int x0, int y0, int x1, int y1,
                                    TriPredicate<@NotNull Integer, @NotNull Integer, @NotNull Integer> shouldDraw,
                                    BiConsumer<Integer, Integer> doDraw) {
            final int dx = Math.abs(x1 - x0), dy = -Math.abs(y1 - y0);
            final int sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1;
            int error = dx + dy;
            int count = 0;
            while (true) {
                // This is horribly unoptimized
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
    }
}
