package com.rubenverg.moldraw.molecule;

import com.google.gson.*;

import java.lang.reflect.Type;

public record Parens(
                     String sub,
                     String sup,
                     int... atoms)
        implements MoleculeElement {

    @Override
    public int[] coveredAtoms() {
        return atoms;
    }

    @Override
    public MoleculeElement<?> replaceInOrder(int[] newIndices) {
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

    public static class Json implements JsonSerializer<Parens>, JsonDeserializer<Parens> {

        private Json() {}

        public static Json INSTANCE = new Json();

        @Override
        public Parens deserialize(JsonElement jsonElement, Type type,
                                  JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!jsonElement.isJsonObject()) throw new JsonParseException("Parens JSON must be an object");
            final var obj = jsonElement.getAsJsonObject();
            String sub = "", sup = "";
            if (obj.has("sub")) sub = obj.get("sub").getAsString();
            if (obj.has("sup")) sup = obj.get("sup").getAsString();
            if (!obj.has("atoms")) throw new JsonParseException("Parens JSON must have atoms property");
            final var atoms = obj.getAsJsonArray("atoms").asList().stream().mapToInt(JsonElement::getAsInt).toArray();
            return new Parens(sub, sup, atoms);
        }

        @Override
        public JsonElement serialize(Parens parens, Type type, JsonSerializationContext jsonSerializationContext) {
            final var obj = new JsonObject();
            if (!parens.sub.isEmpty()) obj.addProperty("sub", parens.sub);
            if (!parens.sup.isEmpty()) obj.addProperty("sup", parens.sup);
            final var atomsArr = new JsonArray();
            for (final var atom : parens.atoms) {
                atomsArr.add(atom);
            }
            obj.add("atoms", atomsArr);
            return obj;
        }
    }
}
