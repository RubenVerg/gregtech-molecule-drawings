package com.rubenverg.moldraw.molecule;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import lombok.Getter;
import org.joml.Vector2f;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Molecule {

    private int atomIndex = -1;
    private final List<MoleculeElement<?>> contents = new ArrayList<>();
    private boolean isXY = false;
    @Getter
    private int additionalOffset = 0;

    public Molecule() {}

    public Molecule(int additionalOffset) {
        this.additionalOffset = additionalOffset;
    }

    public Molecule uv() {
        this.isXY = false;
        return this;
    }

    public Molecule xy() {
        this.isXY = true;
        return this;
    }

    public Molecule add(MoleculeElement<?> elem) {
        this.contents.add(elem);
        return this;
    }

    public Molecule atom(Element element, Vector2f uv1) {
        final var uv = new Vector2f(uv1);
        if (isXY) MathUtils.squareToTriangle(uv);
        this.contents.add(new Atom(++atomIndex, element, uv));
        return this;
    }

    public Molecule atom(Element element, float u, float v) {
        return atom(element, new Vector2f(u, v));
    }

    public Molecule invAtom(Vector2f uv) {
        return atom(Element.Invisible.inv, uv);
    }

    public Molecule invAtom(float u, float v) {
        return invAtom(new Vector2f(u, v));
    }

    public Molecule bond(int a, int b, Bond.Type type) {
        this.contents.add(new Bond(a, b, type));
        return this;
    }

    public Molecule bond(int a, int b) {
        return bond(a, b, Bond.Type.SINGLE);
    }

    public List<MoleculeElement<?>> contents() {
        return this.contents.stream().toList();
    }

    public List<Atom> atoms() {
        return this.contents.stream().filter(elem -> elem instanceof Atom).map(elem -> (Atom) elem).toList();
    }

    public Optional<Atom> getAtom(int index) {
        return atoms().stream().filter(atom -> atom.index() == index).findFirst();
    }

    public List<Bond> bonds() {
        return this.contents.stream().filter(elem -> elem instanceof Bond).map(elem -> (Bond) elem).toList();
    }

    public Molecule subset(int... atomIndices) {
        final var result = new Molecule();
        AtomicInteger atomCount = new AtomicInteger(-1);
        final Int2IntMap numbersMapping = new Int2IntArrayMap();
        for (final var elem : this.contents) {
            final var oldIndices = elem.coveredAtoms();
            if (Arrays.stream(oldIndices)
                    .allMatch(index -> Arrays.stream(atomIndices).anyMatch(atomIndex -> atomIndex == index))) {
                result.add(elem.replaceInOrder(Arrays.stream(oldIndices)
                        .map(index -> numbersMapping.computeIfAbsent(index, (_i) -> atomCount.incrementAndGet()))
                        .toArray()));
            }
        }
        return result;
    }

    public Pair<Vector2f, Vector2f> bounds() {
        final var atoms = atoms();
        if (atoms.isEmpty()) return new Pair<>(new Vector2f(), new Vector2f());
        Vector2f min = new Vector2f(atoms.get(0).position()), max = new Vector2f(atoms.get(0).position());
        for (final var atom : atoms) {
            min.min(atom.position());
            max.max(atom.position());
        }
        return new Pair<>(min, max);
    }

    public Pair<Vector2f, Vector2f> boundsXY() {
        final var atoms = atoms();
        if (atoms.isEmpty()) return new Pair<>(new Vector2f(), new Vector2f());
        Vector2f min = new Vector2f(atoms.get(0).position()), max = new Vector2f(atoms.get(0).position());
        MathUtils.triangleToSquare(min);
        MathUtils.triangleToSquare(max);
        var temp = new Vector2f();
        for (final var atom : atoms) {
            temp = new Vector2f(atom.position());
            MathUtils.triangleToSquare(temp);
            min.min(temp);
            max.max(temp);
        }
        return new Pair<>(min, max);
    }

    public static class Json implements JsonSerializer<Molecule>, JsonDeserializer<Molecule> {

        private Json() {}

        public static Json INSTANCE = new Json();

        @Override
        public Molecule deserialize(JsonElement jsonElement, Type reflectType,
                                    JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!jsonElement.isJsonObject()) throw new JsonParseException("Molecule JSON must be an object");
            final var obj = jsonElement.getAsJsonObject();
            if (!obj.has("contents")) throw new JsonParseException("Molecule JSON must contain contents property");
            int additionalOffset = 0;
            if (obj.has("additional_offset")) additionalOffset = obj.get("additional_offset").getAsInt();
            final var molecule = new Molecule(additionalOffset);
            obj.getAsJsonArray("contents").asList().forEach(content -> {
                if (!content.isJsonObject()) throw new JsonParseException("Molecule JSON contents must be objects");
                final var contentObj = content.getAsJsonObject();
                final var type = contentObj.get("type").getAsString();
                molecule.add(switch (type) {
                    case "atom" -> jsonDeserializationContext.deserialize(contentObj, Atom.class);
                    case "bond" -> jsonDeserializationContext.deserialize(contentObj, Bond.class);
                    case "parens" -> jsonDeserializationContext.deserialize(contentObj, Parens.class);
                    default -> throw new JsonParseException(
                            "Molecule JSON contents have unknown type %s".formatted(type));
                });
            });
            return molecule;
        }

        @Override
        public JsonElement serialize(Molecule molecule, Type type, JsonSerializationContext jsonSerializationContext) {
            final var obj = new JsonObject();
            if (molecule.additionalOffset != 0) obj.addProperty("additional_offset", molecule.additionalOffset);
            final var arr = new JsonArray();
            for (final var content : molecule.contents) {
                final var c = jsonSerializationContext.serialize(content);
                c.getAsJsonObject().addProperty("type", content instanceof Atom ? "atom" :
                        content instanceof Bond ? "bond" : content instanceof Parens ? "parens" : "e");
                if (c.getAsJsonObject().get("type").getAsString().equals("e")) throw new RuntimeException("???");
                arr.add(c);
            }
            obj.add("contents", arr);
            return obj;
        }
    }
}
