package com.rubenverg.moldraw.molecule;

import org.joml.Matrix2fc;

import com.google.gson.*;

public record CircleTransformation(Matrix2fc A, int... atoms) implements MoleculeElement<CircleTransformation> {

    @Override
    public String type() {
        return "circle";
    }

    @Override
    public int[] coveredAtoms() {
        return atoms;
    }

    @Override
    public CircleTransformation replaceInOrder(int[] newIndices) {
        return new CircleTransformation(A, newIndices);
    }
}
