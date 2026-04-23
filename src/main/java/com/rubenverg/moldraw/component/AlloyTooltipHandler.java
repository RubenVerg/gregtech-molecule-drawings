package com.rubenverg.moldraw.component;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.IntBinaryOperator;

import net.minecraft.client.Minecraft;

import org.joml.Vector2i;

import com.google.common.math.LongMath;
import com.rubenverg.moldraw.MolDrawConfig;
import com.rubenverg.moldraw.MoleculeColorize;

import akka.japi.Pair;
import bartworks.system.material.Werkstoff;
import codechicken.lib.gui.GuiDraw;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IOreMaterial;
import gtPlusPlus.core.material.Material;

public class AlloyTooltipHandler implements GuiDraw.ITooltipLineHandler {

    private static long maybeMultiplyByMass(IOreMaterial material, long count) {
        if (!MolDrawConfig.INSTANCE.alloy.partsByMass) return count;
        if (material instanceof Materials gt) return count * gt.getMass();
        if (material instanceof Werkstoff bw) return count * bw.getStats()
            .getMass();
        if (material instanceof gtPlusPlus.core.material.Material pp) return count * pp.getMass();
        return count;
    }

    private static List<Pair<IOreMaterial, Long>> maybeMultiplyByMass(List<Pair<IOreMaterial, Long>> rawComponents) {
        return rawComponents.stream()
            .map(pair -> new Pair<>(pair.first(), maybeMultiplyByMass(pair.first(), pair.second())))
            .toList();
    }

    private static Pair<Long, Long> simplify(Pair<Long, Long> frac) {
        final var gcd = LongMath.gcd(frac.first(), frac.second());
        return new Pair<>(frac.first() / gcd, frac.second() / gcd);
    }

    public static List<Pair<IOreMaterial, Long>> doDeriveComponents(IOreMaterial material) {
        List<Pair<IOreMaterial, Long>> materialComponents = null;
        if (material instanceof Materials gt) materialComponents = gt.mMaterialList.stream().map(stack -> new Pair<>((IOreMaterial)stack.mMaterial, stack.mAmount)).toList();
        if (material instanceof Werkstoff bw) materialComponents = bw.getContents().getRight().stream().map(pair -> new Pair<>((IOreMaterial)pair.getLeft(), (long)pair.getRight())).toList();
        if (material instanceof Material pp) {
            if (pp.getComposites().isEmpty() && Objects.nonNull(pp.getGTMaterial())) materialComponents = pp.getGTMaterial().mMaterialList.stream().map(stack -> new Pair<>((IOreMaterial)stack.mMaterial, stack.mAmount)).toList();
            else materialComponents = pp.getComposites().stream().map(stack -> new Pair<>((IOreMaterial)stack.getStackMaterial(), (long)stack.getPartsPerOneHundred())).toList();
        }
        if (Objects.isNull(materialComponents) || materialComponents.isEmpty())
            return List.of(new Pair<>(material, 1L));
        final var normalizedComponents = materialComponents.stream().map(pair -> new Pair<>(switch (pair.first()) {
            case Werkstoff bw -> bw.getBridgeMaterial();
            case Material pp when Objects.nonNull(pp.getGTMaterial()) -> pp.getGTMaterial();
            case IOreMaterial other -> other;
        }, pair.second())).toList();
        final Map<IOreMaterial, Pair<Long, Long>> collectedComponents = new HashMap<>();
        for (final var c : normalizedComponents) {
            if (MolDrawConfig.INSTANCE.alloy.recursive) {
                final var innerComponents = deriveComponents(c.first());
                final var innerTotal = innerComponents.stream().map(Pair::second).reduce(0L, Long::sum);
                for (final var inner : innerComponents) {
                    collectedComponents.compute(inner.first(),
                            (_material, previous) -> Objects.isNull(previous) ?
                                    simplify(new Pair<>(inner.second() * c.second(), innerTotal)) :
                                    simplify(new Pair<>(previous.first() * innerTotal + inner.second() * previous.second(), innerTotal * previous.second())));
                }
            } else collectedComponents.compute(c.first(),
                    (_material, previous) -> Objects.isNull(previous) ? new Pair<>(c.second(), 1L) :
                            simplify(new Pair<>(previous.first() + c.second() * previous.second(), previous.second())));
        }
        final var lcm = collectedComponents.values().stream().map(Pair::second).reduce(1L,
                (a, b) -> a * b / LongMath.gcd(a, b));
        return collectedComponents.entrySet().stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                .map(pair -> new Pair<>(pair.first(), pair.second().first() * lcm / pair.second().second()))
                .sorted(Comparator.comparingLong((Pair<IOreMaterial, Long> x) -> -maybeMultiplyByMass(x.first(), x.second())))
                .toList();
    }

    private static final Map<IOreMaterial, List<Pair<IOreMaterial, Long>>> COMPONENTS_CACHE = new HashMap<>();

    public static void invalidateComponentsCache() {
        COMPONENTS_CACHE.clear();
    }

    public static List<Pair<IOreMaterial, Long>> deriveComponents(IOreMaterial material) {
        // Intentionally not using `computeIfAbsent` since the recursive calls will cause concurrent modification
        if (!COMPONENTS_CACHE.containsKey(material)) {
            COMPONENTS_CACHE.put(material, doDeriveComponents(material));
        }
        return COMPONENTS_CACHE.get(material);
    }

    public static String getFormula(IOreMaterial material) {
        if (material instanceof Materials gt) return gt.getChemicalFormula()
            .isEmpty() ? "?" : gt.getChemicalFormula();
        if (material instanceof Werkstoff bw) return bw.getFormulaTooltip()
            .isEmpty() ? "?" : bw.getFormulaTooltip();
        if (material instanceof Material pp) return pp.vChemicalFormula;
        return "?";
    }

    public final int baseHeight;
    public static final int BASE_WIDTH = 200;

    public final List<Pair<IOreMaterial, Long>> rawComponents;
    public final List<Pair<IOreMaterial, Long>> components;
    public final long total;
    public final List<Pair<Double, IOreMaterial>> stops;
    public final List<Pair<Double, IOreMaterial>> centers;
    public final List<Pair<Vector2i, IOreMaterial>> textStarts;

    private final int addTop, addBottom;
    private final int addLeft, addRight;

    public AlloyTooltipHandler(List<Pair<IOreMaterial, Long>> rcs) {
        baseHeight = MolDrawConfig.INSTANCE.alloy.pieChartRadius * 5 / 2;

        rawComponents = rcs;
        components = maybeMultiplyByMass(rcs);
        total = components.stream()
            .mapToLong(Pair::second)
            .reduce(0, Long::sum);

        final List<Pair<Long, IOreMaterial>> s = new ArrayList<>(), c = new ArrayList<>();
        var current = 0L;
        for (final var comp : components) {
            s.add(new Pair<>(current, comp.first()));
            c.add(new Pair<>(current, comp.first()));
            current += comp.second();
        }
        stops = s.stream()
            .map(pair -> new Pair<>(Math.PI * 2 * pair.first() / total, pair.second()))
            .toList();

        c.remove(0);
        c.add(new Pair<>(total, null));
        centers = org.spongepowered.libraries.com.google.common.collect.Streams
            .zip(
                s.stream(),
                c.stream(),
                (begin, end) -> new Pair<>(
                    Math.PI * (Objects.requireNonNull(begin)
                        .first()
                        + Objects.requireNonNull(end)
                            .first())
                        / total,
                    begin.second()))
            .toList();

        final var font = Minecraft.getMinecraft().fontRenderer;
        final List<Pair<Vector2i, IOreMaterial>> ts = new ArrayList<>();
        int atMostY = Integer.MAX_VALUE, atLeastY = Integer.MIN_VALUE;
        int al = 0, ar = 0;
        for (int i = 0; i < components.size(); i++) {
            final var count = components.get(i)
                .second();
            final var material = components.get(i)
                .first();
            final var center = centers.get(i)
                .first();
            final int cy = (int) (-Math.cos(center) * 0.9 * MolDrawConfig.INSTANCE.alloy.pieChartRadius);
            final var left = center > Math.PI;
            final var ex = (left ? -1 : 1) * (MolDrawConfig.INSTANCE.alloy.pieChartRadius + 10);
            final var percentage = count * 100d / total;
            final var percentageString = percentage < 0.1 ? "<0.1%" : "%.1f%%".formatted(percentage);
            final var text = percentageString + " " + getFormula(material);
            final var width = font.getStringWidth(text);

            if (left) {
                final var topY = Math.min(cy - font.FONT_HEIGHT / 2, atMostY);
                ts.add(new Pair<>(new Vector2i(ex - 5 - width, topY), material));
                atMostY = topY - font.FONT_HEIGHT - 1;
                al = Math.max(al, width);
            } else {
                final var topY = Math.max(cy - font.FONT_HEIGHT / 2, atLeastY);
                ts.add(new Pair<>(new Vector2i(ex + 5, topY), material));
                atLeastY = topY + font.FONT_HEIGHT + 1;
                ar = Math.max(ar, width);
            }
        }
        textStarts = ts;
        addTop = Math.max(0, -atMostY - baseHeight / 2);
        addBottom = Math.max(0, atLeastY - baseHeight / 2);
        addLeft = Math.max(0, al + MolDrawConfig.INSTANCE.alloy.pieChartRadius + 20 - BASE_WIDTH / 2);
        addRight = Math.max(0, ar + MolDrawConfig.INSTANCE.alloy.pieChartRadius + 20 - BASE_WIDTH / 2);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(BASE_WIDTH + addLeft + addRight, baseHeight + addBottom + addTop);
    }

    @Override
    public void draw(int x, int y) {
        final var font = Minecraft.getMinecraft().fontRenderer;

        final int xm = BASE_WIDTH / 2 + addLeft + x, ym = baseHeight / 2 + addTop + y;

        final IntBinaryOperator sc = (xp, yp) -> {
            final int rx = xp - xm, ry = yp - ym;
            final double ng = Math.atan2(rx, -ry);
            final double angle = ng < 0 ? ng + 2 * Math.PI : ng;
            for (int si = 1; si <= stops.size(); si++) {
                if (angle <= stops.get(si % stops.size())
                    .first())
                    return MoleculeColorize.colorForMaterial(
                        stops.get(si - 1)
                            .second());
            }
            return MoleculeColorize.colorForMaterial(
                stops.get(stops.size() - 1)
                    .second());
        };

        GraphicalUtils.plotCircle(xm, ym, MolDrawConfig.INSTANCE.alloy.pieChartRadius, GraphicalUtils::alwaysDraw, sc);

        final IntBinaryOperator white = (_xp, _yp) -> 0xffffffff;

        for (int i = 0; i < components.size(); i++) {
            final var count = components.get(i)
                .second();
            final var material = components.get(i)
                .first();
            final var center = centers.get(i)
                .first();
            final var textStart = textStarts.get(i)
                .first();
            final var topY = ym + textStart.y;
            final var centerY = topY + font.FONT_HEIGHT / 2;
            final var startX = xm + textStart.x;
            final var cx = xm + (int) (Math.sin(center) * 0.9 * MolDrawConfig.INSTANCE.alloy.pieChartRadius);
            final var cy = ym - (int) (Math.cos(center) * 0.9 * MolDrawConfig.INSTANCE.alloy.pieChartRadius);
            final var left = center > Math.PI;
            final var ex = xm + (left ? -1 : 1) * (MolDrawConfig.INSTANCE.alloy.pieChartRadius + 10);
            final var percentage = count * 100d / total;
            final var percentageString = percentage < 0.1 ? "<0.1%" : "%.1f%%".formatted(percentage);
            final var percentageText = percentageString + " ";

            GraphicalUtils.plotLine(cx, cy, cx, centerY, GraphicalUtils::alwaysDraw, white);
            GraphicalUtils.plotLine(cx, centerY, ex, centerY, GraphicalUtils::alwaysDraw, white);
            font.drawString(percentageText, startX, topY, 0xffffffff);
            font.drawString(
                getFormula(material),
                startX + font.getStringWidth(percentageText),
                topY,
                MoleculeColorize.colorForMaterial(material));
        }
    }
}
