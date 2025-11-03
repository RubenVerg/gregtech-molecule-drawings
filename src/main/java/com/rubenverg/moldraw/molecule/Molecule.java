package com.rubenverg.moldraw.molecule;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

import java.lang.Math;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

public class Molecule {

    private int atomIndex = -1;
    private final List<MoleculeElement<?>> contents = new ArrayList<>();
    @Getter
    private final Matrix2d transformation = new Matrix2d();

    public Molecule() {}

    public Molecule transformation(Matrix2dc matrix) {
        matrix.get(this.transformation);
        return this;
    }

    public Molecule uv() {
        return this.transformation(MathUtils.UVtoXY);
    }

    public Molecule xy() {
        final var mat = new Matrix2d();
        mat.identity();
        return this.transformation(mat);
    }

    public Molecule add(MoleculeElement<?> elem) {
        this.contents.add(elem);
        return this;
    }

    public Molecule addAll(Collection<MoleculeElement<?>> elems) {
        this.contents.addAll(elems);
        return this;
    }

    public Molecule addAll(Molecule mol) {
        return addAll(mol.contents);
    }

    public Molecule skipAnAtom() {
        ++atomIndex;
        return this;
    }

    public Molecule setIndex(int index) {
        atomIndex = index;
        return this;
    }

    public Molecule atom(Element.Counted element, @Nullable Element.Counted above, @Nullable Element.Counted right,
                         @Nullable Element.Counted below, @Nullable Element.Counted left, Vector2fc ab) {
        final var xy = new Vector2f(ab);
        xy.mul(this.transformation);
        this.contents.add(new Atom(++atomIndex, element, Optional.ofNullable(above), Optional.ofNullable(right),
                Optional.ofNullable(below), Optional.ofNullable(left), xy));
        return this;
    }

    public Molecule atom(Element.Counted element, @Nullable Element.Counted above, @Nullable Element.Counted right,
                         @Nullable Element.Counted below, @Nullable Element.Counted left, float a, float b) {
        return atom(element, above, right, below, left, new Vector2f(a, b));
    }

    public Molecule atom(Element element, int count, Vector2f ab) {
        return atom(element.count(count), null, null, null, null, ab);
    }

    public Molecule atom(Element element, int count, float a, float b) {
        return atom(element, count, new Vector2f(a, b));
    }

    public Molecule atom(Element element, float a, float b) {
        return atom(element, 1, a, b);
    }

    public Molecule invAtom(Vector2f ab) {
        return atom(Element.INVISIBLE, 1, ab);
    }

    public Molecule invAtom(float a, float b) {
        return invAtom(new Vector2f(a, b));
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

    public Molecule affine(Matrix3x2fc transformation) {
        for (final var atom : atoms()) {
            atom.position().mulPosition(transformation);
        }
        return this;
    }

    public Molecule relabeled(IntUnaryOperator mapper) {
        final var result = new Molecule();
        for (final var elem : this.contents) {
            result.add(elem.replaceInOrder(Arrays.stream(elem.coveredAtoms()).map(mapper).toArray()));
        }
        return result;
    }

    public Molecule increment(int n) {
        return relabeled(i -> i + n);
    }

    public Molecule copy() {
        return relabeled(IntUnaryOperator.identity());
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

    public Pair<Vector2f, Vector2f> boundsWithSize(UnaryOperator<Vector2f> translateCoordinates,
                                                   Function<Atom, Pair<Vector2f, Vector2f>> getSize) {
        final var atoms = atoms();
        if (atoms.isEmpty()) return new Pair<>(new Vector2f(), new Vector2f());
        final var t0 = translateCoordinates.apply(atoms.get(0).position());
        final var s0 = getSize.apply(atoms.get(0));
        final Vector2f min = new Vector2f(t0).sub(s0.getFirst()), max = new Vector2f(t0).sub(s0.getSecond());
        for (final var atom : atoms) {
            final var t = translateCoordinates.apply(atom.position());
            final var s = getSize.apply(atom);
            min.min(t.sub(s.getFirst()));
            max.max(t.add(s.getSecond()));
        }
        return new Pair<>(min, max);
    }

    public static Molecule tetragonal(Element center, Element top, Element back, Element front, Element side) {
        return new Molecule()
                .xy()
                .atom(center, 0, 0)
                .atom(top, 0, 1)
                .atom(back, (float) Math.cos(Math.toRadians(-15)), (float) Math.sin(Math.toRadians(-15)))
                .atom(front, (float) Math.cos(Math.toRadians(-60)), (float) Math.sin(Math.toRadians(-60)))
                .atom(side, (float) Math.cos(Math.toRadians(-150)), (float) Math.sin(Math.toRadians(-150)))
                .bond(0, 1)
                .bond(0, 2, Bond.Type.INWARD)
                .bond(0, 3, Bond.Type.OUTWARD)
                .bond(0, 4);
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
            final var molecule = new Molecule();
            obj.getAsJsonArray("contents").asList().forEach(content -> {
                if (!content.isJsonObject()) throw new JsonParseException("Molecule JSON contents must be objects");
                final var contentObj = content.getAsJsonObject();
                final var type = contentObj.get("type").getAsString();
                molecule.add(switch (type) {
                    case "atom" -> jsonDeserializationContext.deserialize(contentObj, Atom.class);
                    case "bond" -> jsonDeserializationContext.deserialize(contentObj, Bond.class);
                    case "parens" -> jsonDeserializationContext.deserialize(contentObj, Parens.class);
                    case "circle" -> jsonDeserializationContext.deserialize(contentObj, CircleTransformation.class);
                    default -> throw new JsonParseException(
                            "Molecule JSON contents have unknown type %s".formatted(type));
                });
            });
            return molecule;
        }

        @Override
        public JsonElement serialize(Molecule molecule, Type type, JsonSerializationContext jsonSerializationContext) {
            final var obj = new JsonObject();
            final var arr = new JsonArray();
            for (final var content : molecule.contents) {
                final var c = jsonSerializationContext.serialize(content);
                c.getAsJsonObject().addProperty("type", content instanceof Atom ? "atom" :
                        content instanceof Bond ? "bond" : content instanceof Parens ? "parens" :
                                content instanceof CircleTransformation ? "circle" : "e");
                if (c.getAsJsonObject().get("type").getAsString().equals("e")) throw new RuntimeException("???");
                arr.add(c);
            }
            obj.add("contents", arr);
            return obj;
        }
    }
}
