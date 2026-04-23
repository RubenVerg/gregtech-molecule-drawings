package com.rubenverg.moldraw.molecule;

import com.google.gson.*;

public record Parens(String sub, String sup, int... atoms) implements MoleculeElement<Parens> {

    @Override
    public String type() {
        return "parens";
    }

    @Override
    public int[] coveredAtoms() {
        return atoms;
    }

    @Override
    public Parens replaceInOrder(int[] newIndices) {
        return new Parens(sub, sup, newIndices);
    }

    public static Parens polymer(int... atoms) {
        return new Parens("n", "", atoms);
    }

    public static Parens negIon(int... atoms) {
        return new Parens("", "-", atoms);
    }

    public static Parens posIon(int... atoms) {
        return new Parens("", "+", atoms);
    }
}
