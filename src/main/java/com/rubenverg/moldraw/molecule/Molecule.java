package com.rubenverg.moldraw.molecule;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix2d;
import org.joml.Matrix2dc;
import org.joml.Matrix3d;
import org.joml.Matrix4x3fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import akka.japi.Pair;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true, chain = true)
public class Molecule implements CompositeElement<Molecule> {

    private int atomIndex = -1;
    private final List<MoleculeElement<?>> contents = new ArrayList<>();
    @Getter
    private final Matrix3d transformation = new Matrix3d();
    @Getter
    @Setter
    private FloatList spinGroups = new FloatArrayList();

    public Molecule() {}

    @Override
    public String type() {
        return "molecule";
    }

    public Molecule transformation(Matrix2dc matrix) {
        matrix.get(this.transformation);
        return this;
    }

    public Molecule uv() {
        return this.transformation(MathUtils.UVtoXY);
    }

    public Molecule xy() {
        final var mat = new Matrix2d();
        mat.identity();
        return this.transformation(mat);
    }

    public Molecule addNoTransform(MoleculeElement<?> elem) {
        this.contents.add(elem);
        return this;
    }

    public Molecule add(MoleculeElement<?> elem) {
        elem.beforeAdd(this);
        return addNoTransform(elem);
    }

    public Molecule addAll(Collection<MoleculeElement<?>> elems) {
        for (final var elem : elems) add(elem);
        return this;
    }

    public Molecule addAll(Molecule mol) {
        // don't call beforeAdd since they were already added
        this.contents.addAll(mol.contents);
        return this;
    }

    public Molecule skipAnAtom() {
        ++atomIndex;
        return this;
    }

    public Molecule setIndex(int index) {
        atomIndex = index;
        return this;
    }

    public Molecule atom(Element.Counted element, @Nullable Element.Counted above, @Nullable Element.Counted right,
        @Nullable Element.Counted below, @Nullable Element.Counted left, Vector2fc ab, int spinGroup) {
        return atom(element, above, right, below, left, new Vector3f(ab, 0), spinGroup);
    }

    public Molecule atom(Element.Counted element, @Nullable Element.Counted above, @Nullable Element.Counted right,
        @Nullable Element.Counted below, @Nullable Element.Counted left, Vector3fc abc, int spinGroup) {
        add(
            new Atom(
                ++atomIndex,
                element,
                Optional.ofNullable(above),
                Optional.ofNullable(right),
                Optional.ofNullable(below),
                Optional.ofNullable(left),
                new Vector3f(abc),
                spinGroup));
        return this;
    }

    public Molecule atom(Element.Counted element, @Nullable Element.Counted above, @Nullable Element.Counted right,
        @Nullable Element.Counted below, @Nullable Element.Counted left, float a, float b, int spinGroup) {
        return atom(element, above, right, below, left, new Vector2f(a, b), spinGroup);
    }

    public Molecule atom(Element.Counted element, @Nullable Element.Counted above, @Nullable Element.Counted right,
        @Nullable Element.Counted below, @Nullable Element.Counted left, float a, float b) {
        return atom(element, above, right, below, left, a, b, 0);
    }

    public Molecule atom(Element element, int count, Vector2f ab) {
        return atom(element, count, new Vector3f(ab, 0));
    }

    public Molecule atom(Element element, int count, Vector3f abc, int spinGroup) {
        return atom(element.count(count), null, null, null, null, abc, spinGroup);
    }

    public Molecule atom(Element element, int count, Vector3f abc) {
        return atom(element, count, abc, 0);
    }

    public Molecule atom(Element element, int count, float a, float b) {
        return atom(element, count, new Vector2f(a, b));
    }

    public Molecule atom(Element element, float a, float b) {
        return atom(element, 1, a, b);
    }

    public Molecule invAtom(Vector2f ab) {
        return atom(Element.INVISIBLE, 1, ab);
    }

    public Molecule invAtom(Vector3f abc, int spinGroup) {
        return atom(Element.INVISIBLE, 1, abc, spinGroup);
    }

    public Molecule invAtom(Vector3f abc) {
        return invAtom(abc, 0);
    }

    public Molecule invAtom(float a, float b) {
        return invAtom(new Vector2f(a, b));
    }

    public Molecule bond(int a, int b, boolean centered, Bond.Line... lines) {
        add(new Bond(a, b, centered, lines));
        return this;
    }

    public Molecule bond(int a, int b, Bond.Line... lines) {
        return bond(a, b, false, lines);
    }

    public Molecule bond(int a, int b) {
        return bond(a, b, Bond.Line.SOLID);
    }

    public Molecule ring(Vector3fc first, Vector3fc next, int spinGroup) {
        add(BenzeneRing.counterClockwise(++atomIndex, first, next, spinGroup));
        atomIndex += 5;
        return this;
    }

    public Molecule ring(float x0, float y0, float x1, float y1, int spinGroup) {
        return ring(new Vector3f(x0, y0, 0), new Vector3f(x1, y1, 0), spinGroup);
    }

    public Molecule ring(float x0, float y0, float x1, float y1) {
        return ring(x0, y0, x1, y1, 0);
    }

    public List<MoleculeElement<?>> contents() {
        return this.contents.stream()
            .toList();
    }

    @Override
    public Collection<MoleculeElement<?>> children() {
        return contents();
    }

    public List<Atom> atoms() {
        return flatChildren().stream()
            .filter(elem -> elem instanceof Atom)
            .map(elem -> (Atom) elem)
            .toList();
    }

    public Optional<Atom> getAtom(int index) {
        return atoms().stream()
            .filter(atom -> atom.index() == index)
            .findFirst();
    }

    public Molecule affine(Matrix4x3fc transformation) {
        for (final var atom : atoms()) {
            atom.position()
                .mulPosition(transformation);
        }
        return this;
    }

    public Molecule relabeled(IntUnaryOperator mapper) {
        final var result = new Molecule();
        for (final var elem : this.contents) {
            result.add(
                elem.replaceInOrder(
                    Arrays.stream(elem.coveredAtoms())
                        .map(mapper)
                        .toArray()));
        }
        return result;
    }

    public Molecule increment(int n) {
        return relabeled(i -> i + n);
    }

    public Molecule copy() {
        return relabeled(IntUnaryOperator.identity());
    }

    public Molecule subset(int... atomIndices) {
        final var result = new Molecule();
        AtomicInteger atomCount = new AtomicInteger(-1);
        final Int2IntMap numbersMapping = new Int2IntArrayMap();
        for (final var elem : this.contents) {
            final var oldIndices = elem.coveredAtoms();
            if (Arrays.stream(oldIndices)
                .allMatch(
                    index -> Arrays.stream(atomIndices)
                        .anyMatch(atomIndex -> atomIndex == index))) {
                result.add(
                    elem.replaceInOrder(
                        Arrays.stream(oldIndices)
                            .map(index -> numbersMapping.computeIfAbsent(index, (_i) -> atomCount.incrementAndGet()))
                            .toArray()));
            }
        }
        return result;
    }

    @Override
    public Molecule replaceInOrder(int[] newIndices) {
        var idx = 0;
        final var result = subset();
        for (final var elem : this.contents) {
            final var c = elem.coveredAtoms().length;
            result.add(elem.replaceInOrder(Arrays.copyOfRange(newIndices, idx, idx + c)));
            idx += c;
        }
        return result;
    }

    public Pair<Vector2f, Vector2f> bounds() {
        final var atoms = atoms();
        if (atoms.isEmpty()) return new Pair<>(new Vector2f(), new Vector2f());
        Vector2f min = new Vector2f(
            atoms.get(0)
                .position().x,
            atoms.get(0)
                .position().y),
            max = new Vector2f(
                atoms.get(0)
                    .position().x,
                atoms.get(0)
                    .position().y);
        for (final var atom : atoms) {
            min.min(new Vector2f(atom.position().x, atom.position().y));
            max.max(new Vector2f(atom.position().x, atom.position().y));
        }
        return new Pair<>(min, max);
    }

    public Pair<Vector2f, Vector2f> boundsWithSize(Function<Vector3f, Vector2f> translateCoordinates,
        Function<Atom, Pair<Vector2f, Vector2f>> getSize) {
        final var atoms = atoms();
        if (atoms.isEmpty()) return new Pair<>(new Vector2f(), new Vector2f());
        final var t0 = translateCoordinates.apply(
            atoms.get(0)
                .position());
        final var s0 = getSize.apply(atoms.get(0));
        final Vector2f min = new Vector2f(t0).sub(s0.first()), max = new Vector2f(t0).add(s0.second());
        for (final var atom : atoms) {
            final var t = translateCoordinates.apply(atom.position());
            final var s = getSize.apply(atom);
            min.min(new Vector2f(t).sub(s.first()));
            max.max(new Vector2f(t).add(s.second()));
        }
        return new Pair<>(min, max);
    }

    public static Molecule tetragonal(Element center, Element top, Element back, Element front, Element side) {
        return new Molecule().xy()
            .atom(center, 0, 0)
            .atom(top, 0, 1)
            .atom(back, (float) Math.cos(Math.toRadians(-15)), (float) Math.sin(Math.toRadians(-15)))
            .atom(front, (float) Math.cos(Math.toRadians(-60)), (float) Math.sin(Math.toRadians(-60)))
            .atom(side, (float) Math.cos(Math.toRadians(-150)), (float) Math.sin(Math.toRadians(-150)))
            .bond(0, 1)
            .bond(0, 2, Bond.Line.INWARD)
            .bond(0, 3, Bond.Line.OUTWARD)
            .bond(0, 4);
    }
}
