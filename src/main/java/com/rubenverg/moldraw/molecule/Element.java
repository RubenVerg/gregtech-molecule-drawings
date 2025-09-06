package com.rubenverg.moldraw.molecule;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Element {

    private static final Map<String, Element> elements = new HashMap<>();

    public final String symbol;
    public final boolean invisible;

    protected Element(String symbol, boolean invisible) {
        this.symbol = symbol;
        this.invisible = invisible;
    }

    public static Element create(String symbol) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, false));
    }

    public static Element H = Element.create("H");
    public static Element He = Element.create("He");
    public static Element Li = Element.create("Li");
    public static Element Be = Element.create("Be");
    public static Element B = Element.create("B");
    public static Element C = Element.create("C");
    public static Element N = Element.create("N");
    public static Element O = Element.create("O");
    public static Element F = Element.create("F");
    public static Element Ne = Element.create("Ne");
    public static Element Na = Element.create("Na");
    public static Element Mg = Element.create("Mg");
    public static Element Al = Element.create("Al");
    public static Element Si = Element.create("Si");
    public static Element P = Element.create("P");
    public static Element S = Element.create("S");
    public static Element Cl = Element.create("Cl");
    public static Element Ar = Element.create("Ar");
    public static Element K = Element.create("K");
    public static Element Ca = Element.create("Ca");
    public static Element Sc = Element.create("Sc");
    public static Element Ti = Element.create("Ti");
    public static Element V = Element.create("V");
    public static Element Cr = Element.create("Cr");
    public static Element Mn = Element.create("Mn");
    public static Element Fe = Element.create("Fe");
    public static Element Co = Element.create("Co");
    public static Element Ni = Element.create("Ni");
    public static Element Cu = Element.create("Cu");
    public static Element Zn = Element.create("Zn");
    public static Element Ga = Element.create("Ga");
    public static Element Ge = Element.create("Ge");
    public static Element As = Element.create("As");
    public static Element Se = Element.create("Se");
    public static Element Br = Element.create("Br");
    public static Element Kr = Element.create("Kr");
    public static Element Rb = Element.create("Rb");
    public static Element Sr = Element.create("Sr");
    public static Element Y = Element.create("Y");
    public static Element Zr = Element.create("Zr");
    public static Element Nb = Element.create("Nb");
    public static Element Mo = Element.create("Mo");
    public static Element Tc = Element.create("Tc");
    public static Element Ru = Element.create("Ru");
    public static Element Rh = Element.create("Rh");
    public static Element Pd = Element.create("Pd");
    public static Element Ag = Element.create("Ag");
    public static Element Cd = Element.create("Cd");
    public static Element In = Element.create("In");
    public static Element Sn = Element.create("Sn");
    public static Element Sb = Element.create("Sb");
    public static Element Te = Element.create("Te");
    public static Element I = Element.create("I");
    public static Element Xe = Element.create("Xe");
    public static Element Cs = Element.create("Cs");
    public static Element Ba = Element.create("Ba");
    public static Element La = Element.create("La");
    public static Element Ce = Element.create("Ce");
    public static Element Pr = Element.create("Pr");
    public static Element Nd = Element.create("Nd");
    public static Element Pm = Element.create("Pm");
    public static Element Sm = Element.create("Sm");
    public static Element Eu = Element.create("Eu");
    public static Element Gd = Element.create("Gd");
    public static Element Tb = Element.create("Tb");
    public static Element Dy = Element.create("Dy");
    public static Element Ho = Element.create("Ho");
    public static Element Er = Element.create("Er");
    public static Element Tm = Element.create("Tm");
    public static Element Yb = Element.create("Yb");
    public static Element Lu = Element.create("Lu");
    public static Element Hf = Element.create("Hf");
    public static Element Ta = Element.create("Ta");
    public static Element W = Element.create("W");
    public static Element Re = Element.create("Re");
    public static Element Os = Element.create("Os");
    public static Element Ir = Element.create("Ir");
    public static Element Pt = Element.create("Pt");
    public static Element Au = Element.create("Au");
    public static Element Hg = Element.create("Hg");
    public static Element Tl = Element.create("Tl");
    public static Element Pb = Element.create("Pb");
    public static Element Bi = Element.create("Bi");
    public static Element Po = Element.create("Po");
    public static Element At = Element.create("At");
    public static Element Rn = Element.create("Rn");
    public static Element Fr = Element.create("Fr");
    public static Element Ra = Element.create("Ra");
    public static Element Ac = Element.create("Ac");
    public static Element Th = Element.create("Th");
    public static Element Pa = Element.create("Pa");
    public static Element U = Element.create("U");
    public static Element Np = Element.create("Np");
    public static Element Pu = Element.create("Pu");
    public static Element Am = Element.create("Am");
    public static Element Cm = Element.create("Cm");
    public static Element Bk = Element.create("Bk");
    public static Element Cf = Element.create("Cf");
    public static Element Es = Element.create("Es");
    public static Element Fm = Element.create("Fm");
    public static Element Md = Element.create("Md");
    public static Element No = Element.create("No");
    public static Element Lr = Element.create("Lr");
    public static Element Rf = Element.create("Rf");
    public static Element Db = Element.create("Db");
    public static Element Sg = Element.create("Sg");
    public static Element Bh = Element.create("Bh");
    public static Element Hs = Element.create("Hs");
    public static Element Mt = Element.create("Mt");
    public static Element Ds = Element.create("Ds");
    public static Element Rg = Element.create("Rg");
    public static Element Cn = Element.create("Cn");
    public static Element Nh = Element.create("Nh");
    public static Element Fl = Element.create("Fl");
    public static Element Mc = Element.create("Mc");
    public static Element Lv = Element.create("Lv");
    public static Element Ts = Element.create("Ts");
    public static Element Og = Element.create("Og");

    public static Element INVISIBLE = elements.computeIfAbsent("", s -> new Element(s, true));
    public static Element BULLET = Element.create("•");

    public Counted one() {
        return count(1);
    }

    public Counted count(int count) {
        return new Counted(this, count);
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
                if (jsonElement.isJsonPrimitive()) return Element.create(jsonElement.getAsString()).count(1);
                else if (jsonElement.isJsonArray())
                    return Element.create(jsonElement.getAsJsonArray().get(0).getAsString())
                            .count(jsonElement.getAsJsonArray().get(1).getAsInt());
                else throw new JsonParseException("Invalid element JSON");
            }

            @Override
            public JsonElement serialize(Counted counted, Type type,
                                         JsonSerializationContext jsonSerializationContext) {
                if (counted.count == 1) return new JsonPrimitive(counted.element.symbol);
                final var arr = new JsonArray();
                arr.add(counted.element.symbol);
                arr.add(counted.count);
                return arr;
            }
        }
    }
}
