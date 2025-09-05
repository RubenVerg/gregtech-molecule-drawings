package com.rubenverg.moldraw.molecule;

import com.google.gson.*;
import org.joml.Vector2f;

import java.lang.reflect.Type;

public record Atom(
                   int index,
                   Element element,
                   Vector2f position)
        implements MoleculeElement<Atom> {

    @Override
    public int[] coveredAtoms() {
        return new int[] { index };
    }

    @Override
    public Atom replaceInOrder(int[] newIndices) {
        return new Atom(
                newIndices[0],
                element,
                position);
    }

    public boolean isInvisible() {
        return element.invisible;
    }

    public static class Json implements JsonSerializer<Atom>, JsonDeserializer<Atom> {

        private Json() {}

        public static Json INSTANCE = new Json();

        @Override
        public Atom deserialize(JsonElement jsonElement, Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!jsonElement.isJsonObject()) throw new JsonParseException("Atom JSON must be an object");
            final var obj = jsonElement.getAsJsonObject();
            if (!obj.has("index")) throw new JsonParseException("Atom JSON must contain an index");
            final var index = obj.get("index").getAsInt();
            if (!obj.has("element")) throw new JsonParseException("Atom JSON must contain an element");
            final var element = Element.create(obj.get("element").getAsString());
            final var position = new Vector2f();
            if (obj.has("u") && obj.has("v")) {
                position.x = obj.get("u").getAsFloat();
                position.y = obj.get("v").getAsFloat();
                position.mul(MathUtils.UVtoXY);
            } else if (obj.has("x") && obj.has("y")) {
                position.x = obj.get("x").getAsFloat();
                position.y = obj.get("y").getAsFloat();
            } else {
                throw new JsonParseException("Atom JSON must contain either u and v, or x and y");
            }
            return new Atom(index, element, position);
        }

        @Override
        public JsonElement serialize(Atom atom, Type type, JsonSerializationContext jsonSerializationContext) {
            final var obj = new JsonObject();
            obj.addProperty("index", atom.index);
            obj.addProperty("element", atom.element.symbol);
            obj.addProperty("x", atom.position.x);
            obj.addProperty("y", atom.position.y);
            return obj;
        }
    }
}
