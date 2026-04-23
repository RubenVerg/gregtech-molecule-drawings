package com.rubenverg.moldraw.molecule;

import java.util.Arrays;

import com.google.gson.*;

public record Bond(int a, int b, boolean centered, Line... lines) implements MoleculeElement<Bond> {

    @Override
    public String type() {
        return "bond";
    }

    @Override
    public int[] coveredAtoms() {
        return new int[] { a, b };
    }

    @Override
    public Bond replaceInOrder(int[] newIndices) {
        return new Bond(newIndices[0], newIndices[1], centered, lines);
    }

    public int totalThickness() {
        return Arrays.stream(lines)
            .mapToInt(line -> line.thick ? 3 : 1)
            .reduce(0, Integer::sum);
    }

    public enum Line {

        SOLID("solid"),
        DOTTED("dotted"),
        THICK("thick", true),
        INWARD("inward", true),
        OUTWARD("outward", true),;

        public final String jsonName;
        public final boolean thick;

        Line(String jsonName) {
            this(jsonName, false);
        }

        Line(String jsonName, boolean thick) {
            this.jsonName = jsonName;
            this.thick = thick;
        }
    }

    public static Line[] SINGLE = new Line[] { Line.SOLID };
    public static Line[] DOUBLE = new Line[] { Line.SOLID, Line.SOLID };
    public static Line[] TRIPLE = new Line[] { Line.SOLID, Line.SOLID, Line.SOLID };
}
