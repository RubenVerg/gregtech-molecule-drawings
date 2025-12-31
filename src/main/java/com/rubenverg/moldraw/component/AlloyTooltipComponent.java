package com.rubenverg.moldraw.component;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import com.google.common.collect.Streams;
import com.google.common.math.LongMath;
import com.rubenverg.moldraw.MolDrawConfig;
import com.rubenverg.moldraw.MoleculeColorize;
import org.joml.Vector2i;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.function.IntBinaryOperator;

import javax.annotation.ParametersAreNonnullByDefault;

public record AlloyTooltipComponent(List<Pair<Material, Long>> rawComponents) implements TooltipComponent {

    private static long maybeMultiplyByMass(Material material, long count) {
        if (MolDrawConfig.INSTANCE.alloy.partsByMass) return count * material.getMass();
        return count;
    }

    private static List<Pair<Material, Long>> maybeMultiplyByMass(List<Pair<Material, Long>> rawComponents) {
        return rawComponents.stream()
                .map(pair -> new Pair<>(pair.getA(), maybeMultiplyByMass(pair.getA(), pair.getB()))).toList();
    }

    private static Pair<Long, Long> simplify(Pair<Long, Long> frac) {
        final var gcd = LongMath.gcd(frac.getA(), frac.getB());
        return new Pair<>(frac.getA() / gcd, frac.getB() / gcd);
    }

    public static List<Pair<Material, Long>> doDeriveComponents(Material material) {
        final var materialComponents = material.getMaterialComponents();
        if (Objects.isNull(materialComponents) || materialComponents.isEmpty())
            return List.of(new Pair<>(material, 1L));
        final Map<Material, Pair<Long, Long>> collectedComponents = new HashMap<>();
        for (final var c : materialComponents) {
            if (MolDrawConfig.INSTANCE.alloy.recursive) {
                final var innerComponents = deriveComponents(c.material());
                final var innerTotal = innerComponents.stream().map(Pair::getB).reduce(0L, Long::sum);
                for (final var inner : innerComponents) {
                    collectedComponents.compute(inner.getA(),
                            (_material, previous) -> Objects.isNull(previous) ?
                                    simplify(new Pair<>(inner.getB() * c.amount(), innerTotal)) :
                                    simplify(new Pair<>(previous.getA() * innerTotal + inner.getB() * previous.getB(),
                                            innerTotal * previous.getB())));
                }
            } else collectedComponents.compute(c.material(),
                    (_material, previous) -> Objects.isNull(previous) ? new Pair<>(c.amount(), 1L) :
                            simplify(new Pair<>(previous.getA() + c.amount() * previous.getB(), previous.getB())));
        }
        final var lcm = collectedComponents.values().stream().map(Pair::getB).reduce(1L,
                (a, b) -> a * b / LongMath.gcd(a, b));
        return collectedComponents.entrySet().stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                .map(pair -> new Pair<>(pair.getA(), pair.getB().getA() * lcm / pair.getB().getB()))
                .sorted(Comparator.comparingLong((Pair<Material, Long> x) -> -maybeMultiplyByMass(x.getA(), x.getB()))
                        .thenComparing(x -> x.getA().getChemicalFormula()))
                .toList();
    }

    private static final Map<Material, List<Pair<Material, Long>>> COMPONENTS_CACHE = new HashMap<>();

    public static void invalidateComponentsCache() {
        COMPONENTS_CACHE.clear();
    }

    public static List<Pair<Material, Long>> deriveComponents(Material material) {
        // Intentionally not using `computeIfAbsent` since the recursive calls will cause concurrent modification
        if (!COMPONENTS_CACHE.containsKey(material)) {
            COMPONENTS_CACHE.put(material, doDeriveComponents(material));
        }
        return COMPONENTS_CACHE.get(material);
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    public static class ClientAlloyTooltipComponent implements ClientTooltipComponent {

        public final int baseHeight;
        public static final int BASE_WIDTH = 200;

        public final List<Pair<Material, Long>> rawComponents;
        public final List<Pair<Material, Long>> components;
        public final long total;
        public final List<Pair<Double, Material>> stops;
        public final List<Pair<Double, Material>> centers;
        public final List<Pair<Vector2i, Material>> textStarts;

        private final int addTop, addBottom;
        private final int addLeft, addRight;

        @SuppressWarnings("UnstableApiUsage")
        public ClientAlloyTooltipComponent(AlloyTooltipComponent component) {
            baseHeight = MolDrawConfig.INSTANCE.alloy.pieChartRadius * 5 / 2;

            rawComponents = component.rawComponents;
            components = maybeMultiplyByMass(rawComponents);
            total = components.stream().mapToLong(Pair::getB).reduce(0, Long::sum);

            final List<Pair<Long, Material>> s = new ArrayList<>(), c = new ArrayList<>();
            var current = 0L;
            for (final var comp : components) {
                s.add(new Pair<>(current, comp.getA()));
                c.add(new Pair<>(current, comp.getA()));
                current += comp.getB();
            }
            stops = s.stream().map(pair -> new Pair<>(Math.PI * 2 * pair.getA() / total, pair.getB())).toList();

            c.remove(0);
            c.add(new Pair<>(total, GTMaterials.NULL));
            centers = Streams
                    .zip(s.stream(), c.stream(),
                            (begin, end) -> new Pair<>(Math.PI *
                                    (Objects.requireNonNull(begin).getA() + Objects.requireNonNull(end).getA()) / total,
                                    begin.getB()))
                    .toList();

            final var font = Minecraft.getInstance().font;
            final List<Pair<Vector2i, Material>> ts = new ArrayList<>();
            int atMostY = Integer.MAX_VALUE, atLeastY = Integer.MIN_VALUE;
            int al = 0, ar = 0;
            for (int i = 0; i < components.size(); i++) {
                final var count = components.get(i).getB();
                final var material = components.get(i).getA();
                final var center = centers.get(i).getA();
                final int cy = (int) (-Math.cos(center) * 0.9 * MolDrawConfig.INSTANCE.alloy.pieChartRadius);
                final var left = center > Math.PI;
                final var ex = (left ? -1 : 1) * (MolDrawConfig.INSTANCE.alloy.pieChartRadius + 10);
                final var percentage = count * 100d / total;
                final var percentageString = percentage < 0.1 ? "<0.1%" : "%.1f%%".formatted(percentage);
                final var text = Component.literal(percentageString + " ")
                        .append(MoleculeColorize.coloredFormula(new MaterialStack(material, 1), true));
                final var width = font.width(text);

                if (left) {
                    final var topY = Math.min(cy - font.lineHeight / 2, atMostY);
                    ts.add(new Pair<>(new Vector2i(ex - 5 - width, topY), material));
                    atMostY = topY - font.lineHeight - 1;
                    al = Math.max(al, width);
                } else {
                    final var topY = Math.max(cy - font.lineHeight / 2, atLeastY);
                    ts.add(new Pair<>(new Vector2i(ex + 5, topY), material));
                    atLeastY = topY + font.lineHeight + 1;
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
        public int getHeight() {
            return baseHeight + addBottom + addTop;
        }

        @Override
        public int getWidth(Font font) {
            return BASE_WIDTH + addLeft + addRight;
        }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
            final int xm = BASE_WIDTH / 2 + addLeft + x, ym = baseHeight / 2 + addTop + y;

            final IntBinaryOperator sc = (xp, yp) -> {
                final int rx = xp - xm, ry = yp - ym;
                final double ng = Math.atan2(rx, -ry);
                final double angle = ng < 0 ? ng + 2 * Math.PI : ng;
                for (int si = 1; si <= stops.size(); si++) {
                    if (angle <= stops.get(si % stops.size()).getA())
                        return MoleculeColorize.colorForMaterial(stops.get(si - 1).getB());
                }
                return MoleculeColorize.colorForMaterial(stops.get(stops.size() - 1).getB());
            };

            GraphicalUtils.plotCircle(xm, ym, MolDrawConfig.INSTANCE.alloy.pieChartRadius, GraphicalUtils::alwaysDraw,
                    sc, guiGraphics);

            final IntBinaryOperator white = (_xp, _yp) -> 0xffffffff;

            for (int i = 0; i < components.size(); i++) {
                final var count = components.get(i).getB();
                final var material = components.get(i).getA();
                final var center = centers.get(i).getA();
                final var textStart = textStarts.get(i).getA();
                final var topY = ym + textStart.y;
                final var centerY = topY + font.lineHeight / 2;
                final var startX = xm + textStart.x;
                final var cx = xm + (int) (Math.sin(center) * 0.9 * MolDrawConfig.INSTANCE.alloy.pieChartRadius);
                final var cy = ym - (int) (Math.cos(center) * 0.9 * MolDrawConfig.INSTANCE.alloy.pieChartRadius);
                final var left = center > Math.PI;
                final var ex = xm + (left ? -1 : 1) * (MolDrawConfig.INSTANCE.alloy.pieChartRadius + 10);
                final var percentage = count * 100d / total;
                final var percentageString = percentage < 0.1 ? "<0.1%" : "%.1f%%".formatted(percentage);
                final var text = Component.literal(percentageString + " ")
                        .append(MoleculeColorize.coloredFormula(new MaterialStack(material, 1), true));

                GraphicalUtils.plotLine(cx, cy, cx, centerY, GraphicalUtils::alwaysDraw, white, guiGraphics);
                GraphicalUtils.plotLine(cx, centerY, ex, centerY, GraphicalUtils::alwaysDraw, white, guiGraphics);
                guiGraphics.drawString(font, text, startX, topY, 0xffffffff);
            }
        }
    }
}
