package com.rubenverg.moldraw.component;

import static com.rubenverg.moldraw.MoleculeColorize.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.rubenverg.moldraw.MolDrawConfig;
import com.rubenverg.moldraw.MoleculeColorize;
import com.rubenverg.moldraw.molecule.*;

import akka.japi.Pair;
import codechicken.lib.gui.GuiDraw;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class MoleculeTooltipHandler implements GuiDraw.ITooltipLineHandler {

    public static int DEBUG_COLOR = MathUtils.chatFormattingColor(ChatFormatting.RED);

    private final Molecule molecule;
    private final Vector2i xySize;
    private final Vector2f xyStart;
    private final boolean atomAtTop;
    private final boolean atomAtTopTop;
    private final boolean atomAtBotBot;
    private final boolean atomAtLefLef;
    private final boolean parenAtLef;
    private final List<Vector3f> centers;
    private final Map<Element.Counted, Integer> elementWidths = new HashMap<>();

    private UnaryOperator<Vector2f> toScaledFactory(int lineHeight) {
        return xy -> {
            var result = new Vector2f();
            new Vector2f(xy.x, xy.y).sub(xyStart, result);
            result.mul(MolDrawConfig.INSTANCE.molecule.moleculeScale);
            return new Vector2f(
                result.x + 8 + (atomAtLefLef ? 12 : 0) + (parenAtLef ? 6 : 0),
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
            if (atom.right()
                .isPresent()) {
                x1 += 1 + elementWidths.getOrDefault(
                    atom.right()
                        .get(),
                    0);
            }
            if (atom.left()
                .isPresent()) {
                x0 += 1 + elementWidths.getOrDefault(
                    atom.left()
                        .get(),
                    0);
            }
            if (atom.above()
                .isPresent()) {
                y0 += 1 + lineHeight;
                x0 = Math.max(
                    x0,
                    elementWidths.getOrDefault(
                        atom.above()
                            .get(),
                        0) / 2f);
                x1 = Math.max(
                    x1,
                    elementWidths.getOrDefault(
                        atom.above()
                            .get(),
                        0) / 2f);
            }
            if (atom.below()
                .isPresent()) {
                y1 += 1 + lineHeight;
                x0 = Math.max(
                    x0,
                    elementWidths.getOrDefault(
                        atom.below()
                            .get(),
                        0) / 2f);
                x1 = Math.max(
                    x1,
                    elementWidths.getOrDefault(
                        atom.below()
                            .get(),
                        0) / 2f);
            }
            return new Pair<>(new Vector2f(x0, y0), new Vector2f(x1, y1));
        };
    }

    private Vector2f project(Vector3fc xyz, int group) {
        final var vec = new Vector3f(xyz);
        if (MolDrawConfig.INSTANCE.molecule.spinMolecules && group >= 0
            && group < molecule.spinGroups()
                .size()) {
            final var freq = 1000 / (molecule.spinGroups()
                .getFloat(group) * MolDrawConfig.INSTANCE.molecule.spinSpeedMultiplier);
            vec.sub(centers.get(group));
            vec.mul(
                new Matrix3f().rotationY(System.currentTimeMillis() % (int) freq / freq * org.joml.Math.PI_TIMES_2_f));
            vec.add(centers.get(group));
        }
        return new Vector2f(vec.x, vec.y);
    }

    private Function<Vector3f, Vector2f> toScaledProjectedFactory(int lineHeight, int group) {
        return xyz -> toScaledFactory(lineHeight).apply(project(xyz, group));
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

    public MoleculeTooltipHandler(Molecule molecule) {
        this.molecule = molecule;
        final var bounds = molecule.bounds();
        final Vector2f diff = new Vector2f();
        bounds.second()
            .sub(bounds.first(), diff);
        diff.mul(MolDrawConfig.INSTANCE.molecule.moleculeScale);
        diff.ceil();
        this.xySize = new Vector2i((int) diff.x, (int) diff.y);
        this.xyStart = new Vector2f(bounds.first().x, bounds.second().y);
        this.atomAtTop = molecule.atoms()
            .stream()
            .anyMatch(atom -> {
                final var distanceFromTop = Math.abs(xyStart.y - atom.position().y);
                final var invisible = atom.isInvisible();
                return distanceFromTop < 0.1 && !invisible;
            });
        this.atomAtTopTop = molecule.atoms()
            .stream()
            .anyMatch(atom -> {
                final var distanceFromTop = Math.abs(xyStart.y - atom.position().y);
                final var invisible = atom.isInvisible();
                final var hasAbove = atom.above()
                    .isPresent();
                return distanceFromTop < 0.1 && !invisible && hasAbove;
            });
        this.atomAtBotBot = molecule.atoms()
            .stream()
            .anyMatch(atom -> {
                final var distanceFromBot = Math.abs(bounds.first().y - atom.position().y);
                final var invisible = atom.isInvisible();
                final var hasBelow = atom.below()
                    .isPresent();
                return distanceFromBot < 0.1 && !invisible && hasBelow;
            });
        this.atomAtLefLef = molecule.atoms()
            .stream()
            .anyMatch(atom -> {
                final var distanceFromLef = Math.abs(bounds.first().x - atom.position().x);
                final var invisible = atom.isInvisible();
                final var hasLef = atom.left()
                    .isPresent();
                return distanceFromLef < 0.1 && !invisible && hasLef;
            });
        this.parenAtLef = molecule.atoms()
            .stream()
            .anyMatch(atom -> {
                final var distanceFromLef = Math.abs(bounds.first().x - atom.position().x);
                if (distanceFromLef > 0.1) return false;
                return molecule.flatChildren()
                    .stream()
                    .anyMatch(
                        el -> el instanceof Parens parens && Arrays.stream(parens.atoms())
                            .anyMatch(d -> d == atom.index()));
            });
        {
            centers = molecule.spinGroups()
                .doubleStream()
                .mapToObj(freq -> new Vector3f(0, 0, 0))
                .toList();
            final IntList counts = molecule.spinGroups()
                .doubleStream()
                .mapToObj(freq -> 0)
                .collect(Collectors.toCollection(IntArrayList::new));
            for (final var elem : this.molecule.contents()) if (elem instanceof Atom atom) {
                if (atom.spinGroup() >= 0 && atom.spinGroup() < centers.size()) {
                    centers.get(atom.spinGroup())
                        .add(atom.position());
                    counts.set(atom.spinGroup(), counts.getInt(atom.spinGroup()) + 1);
                }
            }
            IntStream.range(0, centers.size())
                .forEach(
                    i -> centers.get(i)
                        .div(counts.getInt(i)));
        }
    }

    @Override
    public Dimension getSize() {
        return new Dimension(
            xySize.x + 32 + (atomAtLefLef ? 12 : 0) + (parenAtLef ? 6 : 0),
            xySize.y + 20 + (atomAtBotBot ? 10 : 0) + (atomAtTopTop ? 10 : 0));
    }

    @Override
    public void draw(int x, int y) {
        final var defaultColor = configColor(null);
        final var font = Minecraft.getMinecraft().fontRenderer;
        final var ts = toScaledFactory(font.FONT_HEIGHT);
        for (final var elem : this.molecule.contents()) {
            drawOneText(elem, font, x, y, defaultColor);
        }
        for (final var elem : this.molecule.contents()) {
            drawOneImage(elem, font, x, y, defaultColor, ts, null);
        }
    }

    private void drawOneText(MoleculeElement<?> elem, FontRenderer font, int x, int y, int defaultColor) {
        if (elem instanceof Atom atom) {
            final var xyPosition = floored(
                toScaledFactory(font.FONT_HEIGHT).apply(project(atom.position(), atom.spinGroup())));
            final var width = font.getStringWidth(
                atom.element()
                    .toString());
            final var cdx = (int) (-(float) width / 2) + 1;
            if (!atom.element()
                .element().invisible) font.drawString(
                    atom.element()
                        .toString(),
                    x + xyPosition.x + cdx,
                    y + xyPosition.y + 1,
                    colorForElement(
                        atom.element()
                            .element()),
                    false);
            elementWidths.put(
                atom.element(),
                atom.element()
                    .element().invisible ? 0 : width);
            if (atom.right()
                .isPresent()) {
                if (!atom.right()
                    .get()
                    .element().invisible) font.drawString(
                        atom.right()
                            .get()
                            .toString(),
                        x + xyPosition.x + (int) ((float) width / 2) + 1,
                        y + xyPosition.y + 1,
                        colorForElement(
                            atom.right()
                                .get()
                                .element()),
                        false);
                elementWidths.put(
                    atom.right()
                        .get(),
                    atom.right()
                        .get()
                        .element().invisible ? 0
                            : font.getStringWidth(
                                atom.right()
                                    .get()
                                    .toString()));
            }
            if (atom.left()
                .isPresent()) {
                final var leftWidth = font.getStringWidth(
                    atom.left()
                        .get()
                        .toString());
                if (!atom.left()
                    .get()
                    .element().invisible) font.drawString(
                        atom.left()
                            .get()
                            .toString(),
                        x + xyPosition.x - leftWidth + (int) (-(float) width / 2),
                        y + xyPosition.y + 1,
                        colorForElement(
                            atom.left()
                                .get()
                                .element()),
                        false);
                elementWidths.put(
                    atom.left()
                        .get(),
                    atom.left()
                        .get()
                        .element().invisible ? 0 : leftWidth);
            }
            if (atom.above()
                .isPresent()) {
                final var aboveWidth = font.getStringWidth(
                    atom.above()
                        .get()
                        .toString());
                if (!atom.above()
                    .get()
                    .element().invisible) font.drawString(
                        atom.above()
                            .get()
                            .toString(),
                        x + xyPosition.x + (int) (-(float) aboveWidth / 2) + 1,
                        y + xyPosition.y - font.FONT_HEIGHT + 1,
                        colorForElement(
                            atom.above()
                                .get()
                                .element()),
                        false);
                elementWidths.put(
                    atom.above()
                        .get(),
                    atom.above()
                        .get()
                        .element().invisible ? 0 : aboveWidth);
            }
            if (atom.below()
                .isPresent()) {
                final var belowWidth = font.getStringWidth(
                    atom.below()
                        .get()
                        .toString());
                if (!atom.below()
                    .get()
                    .element().invisible) font.drawString(
                        atom.below()
                            .get()
                            .toString(),
                        x + xyPosition.x + (int) (-(float) belowWidth / 2) + 1,
                        y + xyPosition.y + font.FONT_HEIGHT + 1,
                        colorForElement(
                            atom.below()
                                .get()
                                .element()),
                        false);
                elementWidths.put(
                    atom.below()
                        .get(),
                    atom.below()
                        .get()
                        .element().invisible ? 0 : belowWidth);
            }
            if (MolDrawConfig.INSTANCE.debugMode) font.drawString(
                Integer.toString(atom.index()),
                x + xyPosition.x - 5,
                y + xyPosition.y - 2,
                DEBUG_COLOR,
                false);
        } else if (elem instanceof Parens pp) {
            final var bounds = this.molecule.subset(pp.atoms())
                .boundsWithSize(toScaledProjectedFactory(font.FONT_HEIGHT, -1), sizeOfAtomFactory(font.FONT_HEIGHT));
            final var xySub = new Vector2i((int) bounds.second().x, (int) bounds.second().y);
            xySub.add(7, -2);
            font.drawString(pp.sub(), x + xySub.x, y + xySub.y, defaultColor, false);
            final var xySup = new Vector2i((int) bounds.second().x, (int) bounds.first().y);
            xySup.add(7, -4);
            font.drawString(pp.sup(), x + xySup.x, y + xySup.y, defaultColor, false);
        } else if (elem instanceof CompositeElement<?>composite) {
            for (final var child : composite.children()) drawOneText(child, font, x, y, defaultColor);
        }
    }

    private void drawOneImage(MoleculeElement<?> elem, FontRenderer font, int x, int y, int defaultColor,
        UnaryOperator<Vector2f> toScaled, @Nullable IntBinaryOperator overrideColor) {
        if (elem instanceof Bond bond) {
            if (Objects.isNull(this.molecule.getAtom(bond.a())) || Objects.isNull(this.molecule.getAtom(bond.b())))
                return;
            final var atomA = this.molecule.getAtom(bond.a())
                .orElseThrow();
            final var atomAWidth = elementWidths.get(atomA.element());
            final var atomAAbove = atomA.above()
                .map(elementWidths::get);
            final var atomARight = atomA.right()
                .map(elementWidths::get);
            final var atomABelow = atomA.below()
                .map(elementWidths::get);
            final var atomALeft = atomA.left()
                .map(elementWidths::get);
            final var atomAInvisible = atomA.isInvisible();
            final var atomB = this.molecule.getAtom(bond.b())
                .orElseThrow();
            final var atomBWidth = elementWidths.get(atomB.element());
            final var atomBAbove = atomB.above()
                .map(elementWidths::get);
            final var atomBRight = atomB.right()
                .map(elementWidths::get);
            final var atomBBelow = atomB.below()
                .map(elementWidths::get);
            final var atomBLeft = atomB.left()
                .map(elementWidths::get);
            final var atomBInvisible = atomB.isInvisible();
            final var start = floored(toScaled.apply(project(atomA.position(), atomA.spinGroup())));
            start.add(x, y);
            start.add(0, font.FONT_HEIGHT / 2);
            final var end = floored(toScaled.apply(project(atomB.position(), atomB.spinGroup())));
            end.add(x, y);
            end.add(0, font.FONT_HEIGHT / 2);
            final GraphicalUtils.PixelPredicate notCloseToAtom = (xt, yt, _c) -> {
                if (atomAInvisible && atomBInvisible) return true;
                Vector2ic t = new Vector2i(xt, yt);
                var diff = new Vector2i();
                start.sub(t, diff);
                diff.absolute();
                if (diff.x < atomAWidth * 2 / 3 && (!atomAInvisible && diff.y < font.FONT_HEIGHT * 2 / 3)) return false;
                if (atomAAbove.isPresent() && diff.x < atomAAbove.get() * 2 / 3
                    && Math.abs((start.y - font.FONT_HEIGHT - 1) - yt) < font.FONT_HEIGHT * 2 / 3) return false;
                if (atomABelow.isPresent() && diff.x < atomABelow.get() * 2 / 3
                    && Math.abs((start.y + font.FONT_HEIGHT + 1) - yt) < font.FONT_HEIGHT * 2 / 3) return false;
                if (atomARight.isPresent()
                    && Math.abs((start.x + (atomAWidth + atomARight.get()) / 2 + 1) - xt) < atomARight.get() * 2 / 3
                    && diff.y < font.FONT_HEIGHT * 2 / 3) return false;
                if (atomALeft.isPresent()
                    && Math.abs((start.x - (atomAWidth + atomALeft.get()) / 2 - 1) - xt) < atomALeft.get() * 2 / 3
                    && diff.y < font.FONT_HEIGHT * 2 / 3) return false;
                end.sub(t, diff);
                diff.absolute();
                if (diff.x < atomBWidth * 2 / 3 && (!atomBInvisible && diff.y < font.FONT_HEIGHT * 2 / 3)) return false;
                if (atomBAbove.isPresent() && diff.x < atomBAbove.get() * 2 / 3
                    && Math.abs((end.y - font.FONT_HEIGHT - 1) - yt) < font.FONT_HEIGHT * 2 / 3) return false;
                if (atomBBelow.isPresent() && diff.x < atomBBelow.get() * 2 / 3
                    && Math.abs((end.y + font.FONT_HEIGHT + 1) - yt) < font.FONT_HEIGHT * 2 / 3) return false;
                if (atomBRight.isPresent()
                    && Math.abs((end.x + (atomBWidth + atomBRight.get()) / 2 + 1) - xt) < atomBRight.get() * 2 / 3
                    && diff.y < font.FONT_HEIGHT * 2 / 3) return false;
                if (atomBLeft.isPresent()
                    && Math.abs((end.x - (atomBWidth + atomBLeft.get()) / 2 - 1) - xt) < atomBLeft.get() * 2 / 3
                    && diff.y < font.FONT_HEIGHT * 2 / 3) return false;
                return true;
            };
            final var startEnd = new Vector2f(end).sub(new Vector2f(start));
            final float dy = startEnd.y, dx = startEnd.x, length = startEnd.length();
            final BiFunction<Integer, Integer, GraphicalUtils.PixelPredicate> notCloseToAtomAndDot = (m,
                b) -> notCloseToAtom.and((xt, yt, count) -> count % m < b);
            int addX = Math.round(dy / length * 2), addY = -Math.round(dx / length * 2);
            int addHX = Math.round(dy / length), addHY = -Math.round(dx / length);
            int colorA = colorForElement(
                atomA.element()
                    .element());
            int colorB = colorForElement(
                atomB.element()
                    .element());
            IntBinaryOperator color = Objects.requireNonNullElse(overrideColor, (xp, yp) -> {
                final var d2a = Math.pow(xp - start.x, 2) + Math.pow(yp - start.y, 2);
                final var d2b = Math.pow(xp - end.x, 2) + Math.pow(yp - end.y, 2);
                return d2a < d2b ? colorA : colorB;
            });
            List<Vector2i> allTargets = new ArrayList<>();
            GraphicalUtils.plotLine(
                addX * 3 / 2,
                addY * 3 / 2,
                -addX * 3 / 2,
                -addY * 3 / 2,
                GraphicalUtils::alwaysDraw,
                (xp, yp) -> {
                    allTargets.add(new Vector2i(xp / 2, yp / 2));
                    allTargets.add(new Vector2i((xp + 1) / 2, yp / 2));
                    allTargets.add(new Vector2i(xp / 2, (yp + 1) / 2));
                    allTargets.add(new Vector2i((xp + 1) / 2, (yp + 1) / 2));
                });
            final var aboveEnd = new Vector2f(end).sub(new Vector2f(start))
                .perpendicular()
                .normalize(2)
                .add(new Vector2f(end));
            List<Vector2i> above = new ArrayList<>();
            GraphicalUtils.plotLine(
                start.x,
                start.y,
                Math.round(aboveEnd.x),
                Math.round(aboveEnd.y),
                GraphicalUtils::alwaysDraw,
                (GraphicalUtils.DrawPixel) (xp, yp) -> above.add(new Vector2i(xp, yp)));
            final var thickness = bond.totalThickness();
            final var starting = bond.centered() ? (thickness - 1) / 2f : (float) ((thickness - 1) / 2);
            var done = bond.lines().length > 0 && bond.lines()[0].thick ? 1 : 0;
            for (var i = 0; i < bond.lines().length; done += bond.lines()[i].thick ? 3 : 1, i++) {
                final var delta = done - starting;
                final var sX = (int) org.joml.Math.floor(delta) * addX
                    + (int) ((delta - org.joml.Math.floor(delta)) * 2) * addHX;
                final var sY = (int) org.joml.Math.floor(delta) * addY
                    + (int) ((delta - org.joml.Math.floor(delta)) * 2) * addHY;
                switch (bond.lines()[i]) {
                    case SOLID -> GraphicalUtils
                        .plotLine(start.x + sX, start.y + sY, end.x + sX, end.y + sY, notCloseToAtom, color);
                    case DOTTED -> GraphicalUtils.plotLine(
                        start.x + sX,
                        start.y + sY,
                        end.x + sX,
                        end.y + sY,
                        notCloseToAtomAndDot.apply(2, 1),
                        color);
                    case INWARD -> {
                        for (int j = 0;; j++) {
                            if (j >= above.size()) break;
                            if (j % 3 != 0) continue;
                            final var abovePoint = above.get(j);
                            final var a = new Vector2f(abovePoint);
                            final var startA = new Vector2f(a).sub(new Vector2f(start));
                            final var b = new Vector2f(start)
                                .add(new Vector2f(startEnd).mul(startEnd.dot(startA) / startEnd.dot(startEnd)))
                                .mul(2)
                                .sub(a);
                            final var belowPoint = new Vector2i(Math.round(b.x), Math.round(b.y));
                            GraphicalUtils.plotLine(
                                abovePoint.x + sX,
                                abovePoint.y + sY,
                                belowPoint.x + sX,
                                belowPoint.y + sY,
                                notCloseToAtom,
                                color);
                        }
                    }
                    case OUTWARD -> {
                        for (final var pair : allTargets) {
                            GraphicalUtils.plotLine(
                                start.x + sX,
                                start.y + sY,
                                end.x + pair.x + sX,
                                end.y + pair.y + sY,
                                notCloseToAtom,
                                color);
                        }
                    }
                    case THICK -> {
                        for (final var pair : allTargets) {
                            GraphicalUtils.plotLine(
                                start.x + pair.x + sX,
                                start.y + pair.y + sY,
                                end.x + pair.x + sX,
                                end.y + pair.y + sY,
                                notCloseToAtom,
                                color);
                        }
                    }
                }
            }
        } else if (elem instanceof Parens pp) {
            final var bounds = this.molecule.subset(pp.atoms())
                .boundsWithSize(toScaledProjectedFactory(font.FONT_HEIGHT, -1), sizeOfAtomFactory(font.FONT_HEIGHT));
            final var xyMin = floored(bounds.first());
            xyMin.add(x, y);
            xyMin.add(-2, -1);
            final var xyMax = floored(bounds.second());
            xyMax.add(x, y);
            xyMax.add(2, 1);
            GraphicalUtils.plotLine(
                xyMin.x - 2,
                xyMin.y,
                xyMin.x + 2,
                xyMin.y,
                GraphicalUtils.PixelPredicate::always,
                Objects.requireNonNullElse(overrideColor, (_x, _y) -> defaultColor));
            GraphicalUtils.plotLine(
                xyMin.x - 2,
                xyMax.y,
                xyMin.x + 2,
                xyMax.y,
                GraphicalUtils.PixelPredicate::always,
                Objects.requireNonNullElse(overrideColor, (_x, _y) -> defaultColor));
            GraphicalUtils.plotLine(
                xyMax.x + 2,
                xyMin.y,
                xyMax.x - 2,
                xyMin.y,
                GraphicalUtils.PixelPredicate::always,
                Objects.requireNonNullElse(overrideColor, (_x, _y) -> defaultColor));
            GraphicalUtils.plotLine(
                xyMax.x + 2,
                xyMax.y,
                xyMax.x - 2,
                xyMax.y,
                GraphicalUtils.PixelPredicate::always,
                Objects.requireNonNullElse(overrideColor, (_x, _y) -> defaultColor));
            GraphicalUtils.plotLine(
                xyMin.x - 2,
                xyMin.y,
                xyMin.x - 2,
                xyMax.y,
                GraphicalUtils.PixelPredicate::always,
                Objects.requireNonNullElse(overrideColor, (_x, _y) -> defaultColor));
            GraphicalUtils.plotLine(
                xyMax.x + 2,
                xyMin.y,
                xyMax.x + 2,
                xyMax.y,
                GraphicalUtils.PixelPredicate::always,
                Objects.requireNonNullElse(overrideColor, (_x, _y) -> defaultColor));
        } else if (elem instanceof CircleTransformation ct) {
            final var centroid = Arrays.stream(ct.atoms())
                .mapToObj(
                    idx -> this.molecule.getAtom(idx)
                        .orElseThrow()
                        .position())
                .reduce(new Vector3f(), (a, b) -> new Vector3f(a).add(new Vector3f(b)))
                .div(ct.atoms().length);
            for (int part = 0; part < 128; part++) {
                final var angle = (float) part / 64 * org.joml.Math.PI_f;
                final var u = new Vector3f(org.joml.Math.cos(angle), org.joml.Math.sin(angle), 0);
                final var p = u.mul(new Matrix3f(ct.A()))
                    .add(centroid.x, centroid.y, centroid.z);
                final var r = floored(toScaledProjectedFactory(font.FONT_HEIGHT, -1).apply(p))
                    .add(x, y + font.FONT_HEIGHT / 2);
                GraphicalUtils.screenPixel(
                    r.x,
                    r.y,
                    Objects.isNull(overrideColor) ? defaultColor : overrideColor.applyAsInt(r.x, r.y));
            }
            // final var cc = toScreen(font.FONT_HEIGHT, centroid).add(x, y + font.FONT_HEIGHT / 2);
            // guiGraphics.fill(cc.x, cc.y, cc.x + 1, cc.y + 1, DEBUG_COLOR);
        } else if (elem instanceof CompositeElement<?>composite) {
            IntBinaryOperator color = null;
            if (elem instanceof BenzeneRing ring && MolDrawConfig.INSTANCE.fun.aromanticBenzene) {
                final var bounds = this.molecule.subset(ring.indices())
                    .boundsWithSize(
                        toScaledProjectedFactory(font.FONT_HEIGHT, ring.spinGroup()),
                        sizeOfAtomFactory(font.FONT_HEIGHT));
                final var minY = floored(bounds.first()).y;
                final var maxY = floored(bounds.second()).y;
                final float dy = maxY - minY;
                color = (xp, yp) -> {
                    final var yo = ((yp - y) - minY) / dy;
                    return MoleculeColorize.lightenColor(
                        yo < 0.2f ? 0xff3aa740
                            : yo < 0.4f ? 0xffa8d47a : yo < 0.6f ? 0xffffffff : yo < 0.8f ? 0xffaaabaa : 0xff000000);
                };
            }
            for (final var child : composite.children()) drawOneImage(child, font, x, y, defaultColor, toScaled, color);
        }
    }
}
