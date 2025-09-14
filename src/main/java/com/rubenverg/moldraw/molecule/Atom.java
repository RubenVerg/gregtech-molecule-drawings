package com.rubenverg.moldraw.molecule;

import com.google.gson.*;
import org.joml.Vector2f;

import java.lang.reflect.Type;
import java.util.Optional;

public record Atom(
                   int index,
                   Element.Counted element,
                   Optional<Element.Counted> above,
                   Optional<Element.Counted> right,
                   Optional<Element.Counted> below,
                   Optional<Element.Counted> left,
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
                above,
                right,
                below,
                left,
                new Vector2f(position));
    }

    public boolean isInvisible() {
        return element.element().invisible;
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
            final Element.Counted element = obj.has("element") ?
                    jsonDeserializationContext.deserialize(obj.get("element"), Element.Counted.class) :
                    Element.INVISIBLE.one();
            final Optional<Element.Counted> above = obj.has("above") ?
                    Optional.of(jsonDeserializationContext.deserialize(obj.get("above"), Element.Counted.class)) :
                    Optional.empty();
            final Optional<Element.Counted> right = obj.has("right") ?
                    Optional.of(jsonDeserializationContext.deserialize(obj.get("right"), Element.Counted.class)) :
                    Optional.empty();
            final Optional<Element.Counted> below = obj.has("below") ?
                    Optional.of(jsonDeserializationContext.deserialize(obj.get("below"), Element.Counted.class)) :
                    Optional.empty();
            final Optional<Element.Counted> left = obj.has("left") ?
                    Optional.of(jsonDeserializationContext.deserialize(obj.get("left"), Element.Counted.class)) :
                    Optional.empty();
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
            return new Atom(index, element, above, right, below, left, position);
        }

        @Override
        public JsonElement serialize(Atom atom, Type type, JsonSerializationContext jsonSerializationContext) {
            final var obj = new JsonObject();
            obj.addProperty("index", atom.index);
            obj.add("element", jsonSerializationContext.serialize(atom.element, Element.Counted.class));
            atom.above.ifPresent(
                    counted -> obj.add("above", jsonSerializationContext.serialize(counted, Element.Counted.class)));
            atom.right.ifPresent(
                    counted -> obj.add("right", jsonSerializationContext.serialize(counted, Element.Counted.class)));
            atom.below.ifPresent(
                    counted -> obj.add("below", jsonSerializationContext.serialize(counted, Element.Counted.class)));
            atom.left.ifPresent(
                    counted -> obj.add("left", jsonSerializationContext.serialize(counted, Element.Counted.class)));
            obj.addProperty("x", atom.position.x);
            obj.addProperty("y", atom.position.y);
            return obj;
        }
    }
}
