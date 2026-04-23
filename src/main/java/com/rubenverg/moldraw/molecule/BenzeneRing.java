package com.rubenverg.moldraw.molecule;

import java.util.*;

import org.joml.Math;
import org.joml.Matrix2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.google.gson.*;
import com.rubenverg.moldraw.MolDrawConfig;

public record BenzeneRing(int[] indices, Vector3f first, Vector3f next, int spinGroup, float angle)
    implements CompositeElement<BenzeneRing> {

    @Override
    public String type() {
        return "benzene";
    }

    @Override
    public int[] coveredAtoms() {
        return Arrays.copyOf(indices, indices.length);
    }

    @Override
    public BenzeneRing replaceInOrder(int[] newIndices) {
        return new BenzeneRing(newIndices, first, next, spinGroup, angle);
    }

    @Override
    public void beforeAdd(Molecule to) {
        first.mul(to.transformation());
        next.mul(to.transformation());
    }

    @Override
    public Collection<MoleculeElement<?>> children() {
        final List<MoleculeElement<?>> result = new ArrayList<>();
        final var points = points();
        for (var idx = 0; idx < indices.length; idx++) {
            result.add(
                new Atom(
                    indices[idx],
                    Element.INVISIBLE.count(1),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    new Vector3f(points[idx]),
                    spinGroup));
            result.add(
                new Bond(
                    indices[idx],
                    indices[(idx + 1) % points.length],
                    false,
                    switch (MolDrawConfig.INSTANCE.molecule.benzeneCircle) {
                    case DOUBLE_BONDS -> idx % 2 == 0 ? Bond.SINGLE : Bond.DOUBLE;
                    case CIRCLE -> Bond.SINGLE;
                    }));
        }
        if (MolDrawConfig.INSTANCE.molecule.benzeneCircle == MolDrawConfig.MoleculeConfig.AromaticMode.CIRCLE)
            result.add(
                new CircleTransformation(
                    new Matrix2f().identity()
                        .scale(
                            new Vector3f(next).sub(first)
                                .length() * 2 / 3),
                    indices));
        return result;
    }

    public Vector3fc[] points() {
        final var result = new Vector3fc[indices.length];
        result[0] = first;
        result[1] = next;
        final var delta = new Vector3f(next).sub(first);
        for (var idx = 2; idx < indices.length; idx++) {
            final var old = new Vector3f(delta);
            delta.rotateAxis(Math.PI_TIMES_2_f / indices.length, 0, 0, 1);
            delta.rotateAxis(angle, old.x, old.y, old.z);
            result[idx] = new Vector3f(result[idx - 1]).add(delta);
        }
        return result;
    }

    public static BenzeneRing from(int firstIndex, Vector3fc first, Vector3fc next, int spinGroup, float angle) {
        return new BenzeneRing(
            new int[] { firstIndex, firstIndex + 1, firstIndex + 2, firstIndex + 3, firstIndex + 4, firstIndex + 5 },
            new Vector3f(first),
            new Vector3f(next),
            spinGroup,
            angle);
    }

    public static BenzeneRing clockwise(int firstIndex, Vector3fc first, Vector3fc next, int spinGroup) {
        return BenzeneRing.from(firstIndex, first, next, spinGroup, Math.PI_f);
    }

    public static BenzeneRing counterClockwise(int firstIndex, Vector3fc first, Vector3fc next, int spinGroup) {
        return BenzeneRing.from(firstIndex, first, next, spinGroup, 0);
    }

    public static BenzeneRing clockwise(int firstIndex, float x0, float y0, float x1, float y1, int spinGroup) {
        return BenzeneRing.from(firstIndex, new Vector3f(x0, y0, 0), new Vector3f(x1, y1, 0), spinGroup, Math.PI_f);
    }

    public static BenzeneRing counterClockwise(int firstIndex, float x0, float y0, float x1, float y1, int spinGroup) {
        return BenzeneRing.from(firstIndex, new Vector3f(x0, y0, 0), new Vector3f(x1, y1, 0), spinGroup, 0);
    }
}
