package com.rubenverg.moldraw.molecule;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Element {

    private static final Map<String, Element> elements = new HashMap<>();

    public final String symbol;
    public final boolean invisible;
    public final Color color;
    boolean standard;

    protected Element(String symbol, boolean invisible) {
        this.symbol = symbol;
        this.invisible = invisible;
        this.color = Color.NONE;
        this.standard = false;
    }

    protected Element(String symbol, boolean invisible, Color color) {
        this.symbol = symbol;
        this.invisible = invisible;
        this.color = color;
        this.standard = false;
    }

    public static Element create(String symbol) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, false));
    }

    public static Element create(String symbol, boolean invisible) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, invisible));
    }

    public static Element create(String symbol, Color color) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, false, color));
    }

    public static Element create(String symbol, boolean invisible, Color color) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, invisible, color));
    }

    private static Element createStandard(String symbol, Integer color) {
        final var el = create(symbol, Objects.isNull(color) ? Color.NONE : new Color.Optional(color | (0xff << 24)));
        el.standard = true;
        return el;
    }

    public static Element H = Element.createStandard("H", 0xffffff);
    public static Element He = Element.createStandard("He", 0xd9ffff);
    public static Element Li = Element.createStandard("Li", 0xcc80ff);
    public static Element Be = Element.createStandard("Be", 0xc2ff00);
    public static Element B = Element.createStandard("B", 0xffb5b5);
    public static Element C = Element.createStandard("C", 0x909090);
    public static Element N = Element.createStandard("N", 0x3050f8);
    public static Element O = Element.createStandard("O", 0xff0d0d);
    public static Element F = Element.createStandard("F", 0x90e050);
    public static Element Ne = Element.createStandard("Ne", 0xb3e3f5);
    public static Element Na = Element.createStandard("Na", 0xab5cf2);
    public static Element Mg = Element.createStandard("Mg", 0x8aff00);
    public static Element Al = Element.createStandard("Al", 0xbfa6a6);
    public static Element Si = Element.createStandard("Si", 0xf0c8a0);
    public static Element P = Element.createStandard("P", 0xff8000);
    public static Element S = Element.createStandard("S", 0xffff30);
    public static Element Cl = Element.createStandard("Cl", 0x1ff01f);
    public static Element Ar = Element.createStandard("Ar", 0x80d1e3);
    public static Element K = Element.createStandard("K", 0x8f40d4);
    public static Element Ca = Element.createStandard("Ca", 0x3dff00);
    public static Element Sc = Element.createStandard("Sc", 0xe6e6e6);
    public static Element Ti = Element.createStandard("Ti", 0xbfc2c7);
    public static Element V = Element.createStandard("V", 0xa6a6ab);
    public static Element Cr = Element.createStandard("Cr", 0x8a99c7);
    public static Element Mn = Element.createStandard("Mn", 0x9c7ac7);
    public static Element Fe = Element.createStandard("Fe", 0xe06633);
    public static Element Co = Element.createStandard("Co", 0xf090a0);
    public static Element Ni = Element.createStandard("Ni", 0x50d050);
    public static Element Cu = Element.createStandard("Cu", 0xc88033);
    public static Element Zn = Element.createStandard("Zn", 0x7d80b0);
    public static Element Ga = Element.createStandard("Ga", 0xc28f8f);
    public static Element Ge = Element.createStandard("Ge", 0x668f8f);
    public static Element As = Element.createStandard("As", 0xbd80e3);
    public static Element Se = Element.createStandard("Se", 0xffa100);
    public static Element Br = Element.createStandard("Br", 0xa62929);
    public static Element Kr = Element.createStandard("Kr", 0x5cb8d1);
    public static Element Rb = Element.createStandard("Rb", 0x702eb0);
    public static Element Sr = Element.createStandard("Sr", 0x00ff00);
    public static Element Y = Element.createStandard("Y", 0x94ffff);
    public static Element Zr = Element.createStandard("Zr", 0x94e0e0);
    public static Element Nb = Element.createStandard("Nb", 0x73c2c9);
    public static Element Mo = Element.createStandard("Mo", 0x54b5b5);
    public static Element Tc = Element.createStandard("Tc", 0x3b9e9e);
    public static Element Ru = Element.createStandard("Ru", 0x248f8f);
    public static Element Rh = Element.createStandard("Rh", 0x0a7d8c);
    public static Element Pd = Element.createStandard("Pd", 0x006985);
    public static Element Ag = Element.createStandard("Ag", 0xc0c0c0);
    public static Element Cd = Element.createStandard("Cd", 0xffd98f);
    public static Element In = Element.createStandard("In", 0xa67573);
    public static Element Sn = Element.createStandard("Sn", 0x668080);
    public static Element Sb = Element.createStandard("Sb", 0x9e63b5);
    public static Element Te = Element.createStandard("Te", 0xd47a00);
    public static Element I = Element.createStandard("I", 0x940094);
    public static Element Xe = Element.createStandard("Xe", 0x429eb0);
    public static Element Cs = Element.createStandard("Cs", 0x57178f);
    public static Element Ba = Element.createStandard("Ba", 0x00c900);
    public static Element La = Element.createStandard("La", 0x70d4ff);
    public static Element Ce = Element.createStandard("Ce", 0xffffc7);
    public static Element Pr = Element.createStandard("Pr", 0xd9ffc7);
    public static Element Nd = Element.createStandard("Nd", 0xc7ffc7);
    public static Element Pm = Element.createStandard("Pm", 0xa3ffc7);
    public static Element Sm = Element.createStandard("Sm", 0x8fffc7);
    public static Element Eu = Element.createStandard("Eu", 0x61ffc7);
    public static Element Gd = Element.createStandard("Gd", 0x45ffc7);
    public static Element Tb = Element.createStandard("Tb", 0x30ffc7);
    public static Element Dy = Element.createStandard("Dy", 0x1fffc7);
    public static Element Ho = Element.createStandard("Ho", 0x00ff9c);
    public static Element Er = Element.createStandard("Er", 0x00e675);
    public static Element Tm = Element.createStandard("Tm", 0x00d452);
    public static Element Yb = Element.createStandard("Yb", 0x00bf38);
    public static Element Lu = Element.createStandard("Lu", 0x00ab24);
    public static Element Hf = Element.createStandard("Hf", 0x4dc2ff);
    public static Element Ta = Element.createStandard("Ta", 0x4da6ff);
    public static Element W = Element.createStandard("W", 0x2194d6);
    public static Element Re = Element.createStandard("Re", 0x267dab);
    public static Element Os = Element.createStandard("Os", 0x266696);
    public static Element Ir = Element.createStandard("Ir", 0x175487);
    public static Element Pt = Element.createStandard("Pt", 0xd0d0e0);
    public static Element Au = Element.createStandard("Au", 0xffd123);
    public static Element Hg = Element.createStandard("Hg", 0xb8b8d0);
    public static Element Tl = Element.createStandard("Tl", 0xa6544d);
    public static Element Pb = Element.createStandard("Pb", 0x575961);
    public static Element Bi = Element.createStandard("Bi", 0x9e4fb5);
    public static Element Po = Element.createStandard("Po", 0xab5c00);
    public static Element At = Element.createStandard("At", 0x754f45);
    public static Element Rn = Element.createStandard("Rn", 0x428296);
    public static Element Fr = Element.createStandard("Fr", 0x420066);
    public static Element Ra = Element.createStandard("Ra", 0x007d00);
    public static Element Ac = Element.createStandard("Ac", 0x70abfa);
    public static Element Th = Element.createStandard("Th", 0x00baff);
    public static Element Pa = Element.createStandard("Pa", 0x00a1ff);
    public static Element U = Element.createStandard("U", 0x008fff);
    public static Element Np = Element.createStandard("Np", 0x0080ff);
    public static Element Pu = Element.createStandard("Pu", 0x006bff);
    public static Element Am = Element.createStandard("Am", 0x545cf2);
    public static Element Cm = Element.createStandard("Cm", 0x785ce3);
    public static Element Bk = Element.createStandard("Bk", 0x8a4fe3);
    public static Element Cf = Element.createStandard("Cf", 0xa136d4);
    public static Element Es = Element.createStandard("Es", 0xb31fd4);
    public static Element Fm = Element.createStandard("Fm", 0xb31fba);
    public static Element Md = Element.createStandard("Md", 0xb30da6);
    public static Element No = Element.createStandard("No", 0xbd0d87);
    public static Element Lr = Element.createStandard("Lr", 0xc70066);
    public static Element Rf = Element.createStandard("Rf", 0xcc0059);
    public static Element Db = Element.createStandard("Db", 0xd1004f);
    public static Element Sg = Element.createStandard("Sg", 0xd90045);
    public static Element Bh = Element.createStandard("Bh", 0xe00038);
    public static Element Hs = Element.createStandard("Hs", 0xe6002e);
    public static Element Mt = Element.createStandard("Mt", 0xeb0026);
    public static Element Ds = Element.createStandard("Ds", null);
    public static Element Rg = Element.createStandard("Rg", null);
    public static Element Cn = Element.createStandard("Cn", null);
    public static Element Nh = Element.createStandard("Nh", null);
    public static Element Fl = Element.createStandard("Fl", null);
    public static Element Mc = Element.createStandard("Mc", null);
    public static Element Lv = Element.createStandard("Lv", null);
    public static Element Ts = Element.createStandard("Ts", null);
    public static Element Og = Element.createStandard("Og", null);

    public static Element INVISIBLE = elements.computeIfAbsent("", s -> new Element(s, true));
    public static Element BULLET = Element.create("•");

    static {
        INVISIBLE.standard = true;
        BULLET.standard = true;
    }

    public Counted one() {
        return count(1);
    }

    public Counted count(int count) {
        return new Counted(this, count);
    }

    public static class Json implements JsonSerializer<Element>, JsonDeserializer<Element> {

        private Json() {}

        public static Element.Json INSTANCE = new Element.Json();

        @Override
        public Element deserialize(JsonElement jsonElement, Type type,
                                   JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement.isJsonPrimitive()) return Element.create(jsonElement.getAsString());
            else if (jsonElement.isJsonObject()) {
                final var obj = jsonElement.getAsJsonObject();
                if (obj.has("color")) return Element.create(obj.get("symbol").getAsString(),
                        Objects.requireNonNullElse(obj.get("invisible"), new JsonPrimitive(false)).getAsBoolean(),
                        jsonDeserializationContext.deserialize(obj.get("color"), Element.Color.class));
                else return Element.create(obj.get("symbol").getAsString(),
                        Objects.requireNonNullElse(obj.get("invisible"), new JsonPrimitive(false)).getAsBoolean());
            } else throw new JsonParseException("Invalid element JSON");
        }

        @Override
        public JsonElement serialize(Element element, Type type,
                                     JsonSerializationContext jsonSerializationContext) {
            if (element.standard) return new JsonPrimitive(element.symbol);
            if (element.color instanceof Element.Color.None && !element.invisible)
                return new JsonPrimitive(element.symbol);
            final var obj = new JsonObject();
            obj.add("symbol", new JsonPrimitive(element.symbol));
            if (element.invisible) obj.add("invisible", new JsonPrimitive(true));
            if (!(element.color instanceof Element.Color.None))
                obj.add("color", jsonSerializationContext.serialize(element.color));
            return obj;
        }
    }

    public sealed interface Color {

        Color NONE = new None();

        record None() implements Color {}

        record Always(int color) implements Color {}

        record Optional(int color) implements Color {}

        class Json implements JsonSerializer<Color>, JsonDeserializer<Color> {

            private Json() {}

            public static Color.Json INSTANCE = new Color.Json();

            @Override
            public Color deserialize(JsonElement jsonElement, Type type,
                                     JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                if (jsonElement.isJsonNull()) return NONE;
                if (jsonElement.isJsonPrimitive()) {
                    final var prim = jsonElement.getAsJsonPrimitive();
                    if (prim.isString())
                        return new Always(java.awt.Color.decode(prim.getAsString()).getRGB() | (0xff << 24));
                    else if (prim.isNumber()) return new Always(prim.getAsNumber().intValue());
                }
                if (jsonElement.isJsonObject()) {
                    final var obj = jsonElement.getAsJsonObject();
                    final var colPrim = obj.get("color").getAsJsonPrimitive();
                    final var col = colPrim.isString() ?
                            java.awt.Color.decode(colPrim.getAsString()).getRGB() | (0xff << 24) :
                            colPrim.getAsNumber().intValue();
                    final var optional = obj.has("optional") ? obj.getAsJsonPrimitive("optional").getAsBoolean() : true;
                    return optional ? new Optional(col) : new Always(col);
                } else throw new JsonParseException("Invalid element color JSON");
            }

            @Override
            public JsonElement serialize(Color color, Type type,
                                         JsonSerializationContext jsonSerializationContext) {
                if (color instanceof None) return JsonNull.INSTANCE;
                if (color instanceof Always always) return new JsonPrimitive(always.color);
                if (color instanceof Optional optional) {
                    final var obj = new JsonObject();
                    obj.add("color", new JsonPrimitive(optional.color));
                    obj.add("optional", new JsonPrimitive(true));
                    return obj;
                }
                return JsonNull.INSTANCE;
            }
        }
    }

    public record Counted(Element element, int count) {

        @Override
        public @NotNull String toString() {
            if (count == 1) return element.symbol;
            final var builder = new StringBuilder(element.symbol);
            for (final var ch : Integer.toString(count).getBytes(StandardCharsets.UTF_8)) {
                builder.appendCodePoint(ch + '₀' - '0');
            }
            return builder.toString();
        }

        public static class Json implements JsonSerializer<Counted>, JsonDeserializer<Counted> {

            private Json() {}

            public static Json INSTANCE = new Json();

            @Override
            public Counted deserialize(JsonElement jsonElement, Type type,
                                       JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                if (jsonElement.isJsonPrimitive() || jsonElement.isJsonObject())
                    return jsonDeserializationContext.<Element>deserialize(jsonElement, Element.class).count(1);
                else if (jsonElement.isJsonArray())
                    return jsonDeserializationContext
                            .<Element>deserialize(jsonElement.getAsJsonArray().get(0), Element.class)
                            .count(jsonElement.getAsJsonArray().get(1).getAsInt());
                else throw new JsonParseException("Invalid element JSON");
            }

            @Override
            public JsonElement serialize(Counted counted, Type type,
                                         JsonSerializationContext jsonSerializationContext) {
                if (counted.count == 1) return jsonSerializationContext.serialize(counted.element);
                final var arr = new JsonArray();
                arr.add(jsonSerializationContext.serialize(counted.element));
                arr.add(counted.count);
                return arr;
            }
        }
    }
}
