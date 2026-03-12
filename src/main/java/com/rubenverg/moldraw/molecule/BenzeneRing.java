package com.rubenverg.moldraw.molecule;

import net.minecraft.util.Mth;

import com.google.gson.*;
import com.rubenverg.moldraw.MolDrawConfig;
import org.joml.Matrix2f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.lang.reflect.Type;
import java.util.*;

public record BenzeneRing(
                          int[] indices,
                          Vector3f first,
                          Vector3f next,
                          int spinGroup,
                          float angle)
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
            result.add(new Atom(indices[idx], Element.INVISIBLE.count(1), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.empty(), new Vector3f(points[idx]), spinGroup));
            result.add(new Bond(indices[idx], indices[(idx + 1) % points.length], false,
                    MolDrawConfig.INSTANCE.molecule.benzeneCircle ? Bond.SINGLE :
                            idx % 2 == 0 ? Bond.SINGLE : Bond.DOUBLE));
        }
        if (MolDrawConfig.INSTANCE.molecule.benzeneCircle) result.add(new CircleTransformation(
                new Matrix2f().identity().scale(new Vector3f(next).sub(first).length() * 2 / 3), indices));
        return result;
    }

    public Vector3fc[] points() {
        final var result = new Vector3fc[indices.length];
        result[0] = first;
        result[1] = next;
        final var delta = new Vector3f(next).sub(first);
        for (var idx = 2; idx < indices.length; idx++) {
            final var old = new Vector3f(delta);
            delta.rotateAxis(Mth.TWO_PI / indices.length, 0, 0, 1);
            delta.rotateAxis(angle, old.x, old.y, old.z);
            result[idx] = new Vector3f(result[idx - 1]).add(delta);
        }
        return result;
    }

    public static BenzeneRing from(int firstIndex, Vector3fc first, Vector3fc next, int spinGroup, float angle) {
        return new BenzeneRing(new int[] { firstIndex, firstIndex + 1, firstIndex + 2, firstIndex + 3, firstIndex + 4,
                firstIndex + 5 }, new Vector3f(first), new Vector3f(next), spinGroup, angle);
    }

    public static BenzeneRing clockwise(int firstIndex, Vector3fc first, Vector3fc next, int spinGroup) {
        return BenzeneRing.from(firstIndex, first, next, spinGroup, Mth.PI);
    }

    public static BenzeneRing counterClockwise(int firstIndex, Vector3fc first, Vector3fc next, int spinGroup) {
        return BenzeneRing.from(firstIndex, first, next, spinGroup, 0);
    }

    public static BenzeneRing clockwise(int firstIndex, float x0, float y0, float x1, float y1, int spinGroup) {
        return BenzeneRing.from(firstIndex, new Vector3f(x0, y0, 0), new Vector3f(x1, y1, 0), spinGroup, Mth.PI);
    }

    public static BenzeneRing counterClockwise(int firstIndex, float x0, float y0, float x1, float y1, int spinGroup) {
        return BenzeneRing.from(firstIndex, new Vector3f(x0, y0, 0), new Vector3f(x1, y1, 0), spinGroup, 0);
    }

    public static class Json implements JsonSerializer<BenzeneRing>, JsonDeserializer<BenzeneRing> {

        private Json() {}

        public static Json INSTANCE = new Json();

        @Override
        public BenzeneRing deserialize(JsonElement jsonElement, Type type,
                                       JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!jsonElement.isJsonObject()) throw new JsonParseException("Atom JSON must be an object");
            final var obj = jsonElement.getAsJsonObject();
            int[] indices;
            if (obj.has("indices"))
                indices = obj.getAsJsonArray("indices").asList().stream().mapToInt(JsonElement::getAsInt).toArray();
            else if (obj.has("start_index")) {
                final var count = obj.has("count") ? obj.get("count").getAsInt() : 6;
                indices = new int[count];
                for (var idx = 0; idx < count; idx++) indices[idx] = obj.get("start_index").getAsInt() + idx;
            } else throw new JsonParseException("Benzene ring JSON must contain either indices or start_index");
            final var first = new Vector3f();
            if (obj.has("u0") && obj.has("v0")) {
                final var xy = new Vector2f();
                xy.x = obj.get("u0").getAsFloat();
                xy.y = obj.get("v0").getAsFloat();
                xy.mul(MathUtils.UVtoXY);
                first.set(xy, 0);
            } else if (obj.has("x0") && obj.has("y0")) {
                first.x = obj.get("x0").getAsFloat();
                first.y = obj.get("y0").getAsFloat();
                first.z = Optional.ofNullable(obj.get("z0")).map(JsonElement::getAsFloat).orElse(0f);
            } else throw new JsonParseException(
                    "Benzene ring JSON must contain either u0 and v0, or x0 and y0 (and possibly z0)");
            final var next = new Vector3f();
            if (obj.has("u1") && obj.has("v1")) {
                final var xy = new Vector2f();
                xy.x = obj.get("u1").getAsFloat();
                xy.y = obj.get("v1").getAsFloat();
                xy.mul(MathUtils.UVtoXY);
                next.set(xy, 0);
            } else if (obj.has("x1") && obj.has("y1")) {
                next.x = obj.get("x1").getAsFloat();
                next.y = obj.get("y1").getAsFloat();
                next.z = Optional.ofNullable(obj.get("z1")).map(JsonElement::getAsFloat).orElse(0f);
            } else throw new JsonParseException(
                    "Benzene ring JSON must contain either u1 and v1, or x1 and y1 (and possibly z1)");
            final var spinGroup = obj.has("spin_group") ? obj.get("spin_group").getAsInt() : 0;
            final var angle = obj.has("angle") ? obj.get("angle").getAsFloat() :
                    obj.has("clockwise") ? obj.get("clockwise").getAsBoolean() ? Mth.PI : 0 : Mth.PI;
            return new BenzeneRing(indices, first, next, spinGroup, angle);
        }

        @Override
        public JsonElement serialize(BenzeneRing ring, Type type, JsonSerializationContext jsonSerializationContext) {
            final var obj = new JsonObject();
            final var indices = new JsonArray();
            for (final var index : ring.indices) indices.add(index);
            obj.add("indices", indices);
            obj.addProperty("x0", ring.first.x);
            obj.addProperty("y0", ring.first.y);
            if (ring.first.z != 0) obj.addProperty("z0", ring.first.z);
            obj.addProperty("x1", ring.next.x);
            obj.addProperty("y1", ring.next.y);
            if (ring.next.z != 0) obj.addProperty("z1", ring.next.z);
            if (ring.spinGroup != 0) obj.addProperty("spin_group", ring.spinGroup);
            obj.addProperty("angle", ring.angle);
            return obj;
        }
    }
}
