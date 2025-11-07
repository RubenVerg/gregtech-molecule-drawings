package com.rubenverg.moldraw.molecule;

import com.google.gson.*;
import com.rubenverg.moldraw.MolDraw;

import java.util.Arrays;

public record Bond(
                   int a,
                   int b,
                   boolean centered,
                   Line... lines)
        implements MoleculeElement<Bond> {

    @Override
    public int[] coveredAtoms() {
        return new int[] { a, b };
    }

    @Override
    public Bond replaceInOrder(int[] newIndices) {
        return new Bond(newIndices[0], newIndices[1], centered, lines);
    }

    public int totalThickness() {
        return Arrays.stream(lines).mapToInt(line -> line.thick ? 3 : 1).reduce(0, Integer::sum);
    }

    public enum Line {

        SOLID("solid"),
        DOTTED("dotted"),
        THICK("thick", true),
        INWARD("inward", true),
        OUTWARD("outward", true),
        ;

        public final String jsonName;
        public final boolean thick;

        Line(String jsonName) {
            this(jsonName, false);
        }

        Line(String jsonName, boolean thick) {
            this.jsonName = jsonName;
            this.thick = thick;
        }

        public static class Json implements JsonSerializer<Line>, JsonDeserializer<Line> {

            private Json() {}

            public static final Json INSTANCE = new Json();

            @Override
            public Line deserialize(JsonElement jsonElement, java.lang.reflect.Type type,
                                    JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                final var str = jsonElement.getAsString();
                for (final var value : Line.values()) {
                    if (value.jsonName.equals(str)) return value;
                }
                throw new JsonParseException("Line type %s not recognized".formatted(str));
            }

            @Override
            public JsonElement serialize(Line line, java.lang.reflect.Type type2,
                                         JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(line.jsonName);
            }
        }
    }

    public static class Json implements JsonSerializer<Bond>, JsonDeserializer<Bond> {

        private Json() {}

        public static Json INSTANCE = new Json();

        @Override
        public Bond deserialize(JsonElement jsonElement, java.lang.reflect.Type reflectType,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!jsonElement.isJsonObject()) throw new JsonParseException("Bond JSON must be an object");
            final var obj = jsonElement.getAsJsonObject();
            if (!obj.has("a") || !obj.has("b")) throw new JsonParseException("Bond JSON must contain a and b");
            final int a = obj.get("a").getAsInt(), b = obj.get("b").getAsInt();
            if (obj.has("bond_type")) {
                MolDraw.LOGGER.warn("Molecule uses old bond format!");
                return switch (obj.get("bond_type").getAsString()) {
                    case "single" -> new Bond(a, b, false, SINGLE);
                    case "double" -> new Bond(a, b, false, DOUBLE);
                    case "double_centered" -> new Bond(a, b, true, DOUBLE);
                    case "triple" -> new Bond(a, b, false, TRIPLE);
                    case "outward" -> new Bond(a, b, false, Line.OUTWARD);
                    case "inward" -> new Bond(a, b, false, Line.INWARD);
                    case "thick" -> new Bond(a, b, false, Line.THICK);
                    case "one_and_half" -> new Bond(a, b, false, Line.SOLID, Line.DOTTED);
                    case "quadruple" -> new Bond(a, b, false, Line.SOLID, Line.SOLID, Line.SOLID, Line.SOLID);
                    case "quadruple_centered" -> new Bond(a, b, true, Line.SOLID, Line.SOLID, Line.SOLID, Line.SOLID);
                    case "dotted" -> new Bond(a, b, true, Line.DOTTED);
                    default -> throw new JsonParseException(
                            "Invalid bond type %s in old format.".formatted(obj.get("bond_type").getAsString()));
                };
            }
            final var centered = obj.has("centered") && obj.get("centered").getAsBoolean();
            final var lines = obj.has("lines") ? obj.getAsJsonArray("lines").asList().stream()
                    .map(el -> jsonDeserializationContext.<Line>deserialize(el, Line.class)).toArray(Line[]::new) :
                    SINGLE;
            return new Bond(a, b, centered, lines);
        }

        @Override
        public JsonElement serialize(Bond bond, java.lang.reflect.Type reflectType,
                                     JsonSerializationContext jsonSerializationContext) {
            final var obj = new JsonObject();
            obj.addProperty("a", bond.a);
            obj.addProperty("b", bond.b);
            if (bond.centered) obj.addProperty("centered", true);
            final var lines = new JsonArray();
            for (final var line : bond.lines) lines.add(jsonSerializationContext.serialize(line, Line.class));
            obj.add("lines", lines);
            return obj;
        }
    }

    public static Line[] SINGLE = new Line[] { Line.SOLID };
    public static Line[] DOUBLE = new Line[] { Line.SOLID, Line.SOLID };
    public static Line[] TRIPLE = new Line[] { Line.SOLID, Line.SOLID, Line.SOLID };
}
