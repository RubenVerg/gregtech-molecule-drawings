package com.rubenverg.moldraw.molecule;

import com.google.gson.*;

import java.lang.reflect.Type;

public record Bond(
                   int a,
                   int b,
                   Type type)
        implements MoleculeElement<Bond> {

    @Override
    public int[] coveredAtoms() {
        return new int[] { a, b };
    }

    @Override
    public Bond replaceInOrder(int[] newIndices) {
        return new Bond(newIndices[0], newIndices[1], type);
    }

    public enum Type {

        SINGLE("single"),
        DOUBLE("double"),
        DOUBLE_CENTERED("double_centered"),
        TRIPLE("triple"),
        ;

        public final String jsonName;

        Type(String jsonName) {
            this.jsonName = jsonName;
        }

        public static class Json implements JsonSerializer<Type>, JsonDeserializer<Type> {

            private Json() {}

            public static final Json INSTANCE = new Json();

            @Override
            public Type deserialize(JsonElement jsonElement, java.lang.reflect.Type type,
                                    JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                final var str = jsonElement.getAsString();
                for (final var value : Type.values()) {
                    if (value.jsonName.equals(str)) return value;
                }
                throw new JsonParseException("Bond type %s not recognized".formatted(str));
            }

            @Override
            public JsonElement serialize(Type type, java.lang.reflect.Type type2,
                                         JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(type.jsonName);
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
            var type = Type.SINGLE;
            if (obj.has("bond_type")) type = jsonDeserializationContext.deserialize(obj.get("bond_type"), Type.class);
            return new Bond(a, b, type);
        }

        @Override
        public JsonElement serialize(Bond bond, java.lang.reflect.Type reflectType,
                                     JsonSerializationContext jsonSerializationContext) {
            final var obj = new JsonObject();
            obj.addProperty("a", bond.a);
            obj.addProperty("b", bond.b);
            obj.add("bond_type", jsonSerializationContext.serialize(bond.type, Type.class));
            return obj;
        }
    }
}
