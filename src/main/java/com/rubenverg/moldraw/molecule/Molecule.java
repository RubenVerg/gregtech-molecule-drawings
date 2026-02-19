package com.rubenverg.moldraw.molecule;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

import java.lang.Math;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

@Accessors(fluent = true, chain = true)
public class Molecule {

    private int atomIndex = -1;
    private final List<MoleculeElement<?>> contents = new ArrayList<>();
    @Getter
    private final Matrix3d transformation = new Matrix3d();
    @Getter
    @Setter
    private FloatList spinGroups = new FloatArrayList();

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
                         @Nullable Element.Counted below, @Nullable Element.Counted left, Vector2fc ab, int spinGroup) {
        return atom(element, above, right, below, left, new Vector3f(ab, 0), spinGroup);
    }

    public Molecule atom(Element.Counted element, @Nullable Element.Counted above, @Nullable Element.Counted right,
                         @Nullable Element.Counted below, @Nullable Element.Counted left, Vector3fc abc,
                         int spinGroup) {
        final var xyz = new Vector3f(abc);
        xyz.mul(this.transformation);
        this.contents.add(new Atom(++atomIndex, element, Optional.ofNullable(above), Optional.ofNullable(right),
                Optional.ofNullable(below), Optional.ofNullable(left), xyz, spinGroup));
        return this;
    }

    public Molecule atom(Element.Counted element, @Nullable Element.Counted above, @Nullable Element.Counted right,
                         @Nullable Element.Counted below, @Nullable Element.Counted left, float a, float b,
                         int spinGroup) {
        return atom(element, above, right, below, left, new Vector2f(a, b), spinGroup);
    }

    public Molecule atom(Element.Counted element, @Nullable Element.Counted above, @Nullable Element.Counted right,
                         @Nullable Element.Counted below, @Nullable Element.Counted left, float a, float b) {
        return atom(element, above, right, below, left, a, b, 0);
    }

    public Molecule atom(Element element, int count, Vector2f ab) {
        return atom(element, count, new Vector3f(ab, 0));
    }

    public Molecule atom(Element element, int count, Vector3f abc, int spinGroup) {
        return atom(element.count(count), null, null, null, null, abc, spinGroup);
    }

    public Molecule atom(Element element, int count, Vector3f abc) {
        return atom(element, count, abc, 0);
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

    public Molecule invAtom(Vector3f abc, int spinGroup) {
        return atom(Element.INVISIBLE, 1, abc, spinGroup);
    }

    public Molecule invAtom(Vector3f abc) {
        return invAtom(abc, 0);
    }

    public Molecule invAtom(float a, float b) {
        return invAtom(new Vector2f(a, b));
    }

    public Molecule bond(int a, int b, boolean centered, Bond.Line... lines) {
        this.contents.add(new Bond(a, b, centered, lines));
        return this;
    }

    public Molecule bond(int a, int b, Bond.Line... lines) {
        return bond(a, b, false, lines);
    }

    public Molecule bond(int a, int b) {
        return bond(a, b, Bond.Line.SOLID);
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

    public Molecule affine(Matrix4x3fc transformation) {
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
        Vector2f min = new Vector2f(atoms.get(0).position().x, atoms.get(0).position().y),
                max = new Vector2f(atoms.get(0).position().x, atoms.get(0).position().y);
        for (final var atom : atoms) {
            min.min(new Vector2f(atom.position().x, atom.position().y));
            max.max(new Vector2f(atom.position().x, atom.position().y));
        }
        return new Pair<>(min, max);
    }

    public Pair<Vector2f, Vector2f> boundsWithSize(Function<Vector3f, Vector2f> translateCoordinates,
                                                   Function<Atom, Pair<Vector2f, Vector2f>> getSize) {
        final var atoms = atoms();
        if (atoms.isEmpty()) return new Pair<>(new Vector2f(), new Vector2f());
        final var t0 = translateCoordinates.apply(atoms.get(0).position());
        final var s0 = getSize.apply(atoms.get(0));
        final Vector2f min = new Vector2f(t0).sub(s0.getFirst()), max = new Vector2f(t0).add(s0.getSecond());
        for (final var atom : atoms) {
            final var t = translateCoordinates.apply(atom.position());
            final var s = getSize.apply(atom);
            min.min(new Vector2f(t).sub(s.getFirst()));
            max.max(new Vector2f(t).add(s.getSecond()));
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
                .bond(0, 2, Bond.Line.INWARD)
                .bond(0, 3, Bond.Line.OUTWARD)
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
            if (obj.has("spin")) {
                if (obj.get("spin").isJsonPrimitive()) {
                    if (obj.getAsJsonPrimitive("spin").isBoolean() && obj.get("spin").getAsBoolean())
                        molecule.spinGroups(FloatList.of(1 / 4f));
                    else if (obj.getAsJsonPrimitive("spin").isNumber())
                        molecule.spinGroups(FloatList.of(obj.get("spin").getAsFloat()));
                } else if (obj.get("spin").isJsonArray()) {
                    molecule.spinGroups(obj.getAsJsonArray("spin").asList().stream().map(JsonElement::getAsFloat)
                            .collect(Collectors.toCollection(FloatArrayList::new)));
                } else throw new JsonParseException("Invalid spin");
            }
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
            if (!molecule.spinGroups.isEmpty()) {
                if (molecule.spinGroups.size() == 1) obj.addProperty("spin", molecule.spinGroups.getFloat(0));
                else {
                    final var spin = new JsonArray();
                    molecule.spinGroups.forEach(spin::add);
                    obj.add("spin", spin);
                }
            }
            return obj;
        }
    }
}
