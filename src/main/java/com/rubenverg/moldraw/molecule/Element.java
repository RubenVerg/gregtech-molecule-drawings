package com.rubenverg.moldraw.molecule;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class Element {

    private static final Map<String, Element> elements = new HashMap<>();

    public final String symbol;
    public final boolean invisible;
    public final Color color;
    public final @NotNull Material material;
    public final List<Material> additionalMaterials;
    boolean standard;

    protected Element(String symbol, boolean invisible) {
        this.symbol = symbol;
        this.invisible = invisible;
        this.color = Color.NONE;
        this.standard = false;
        this.material = GTMaterials.NULL;
        this.additionalMaterials = new ArrayList<>();
    }

    protected Element(String symbol, boolean invisible, Color color, @Nullable Material material,
                      Material... additionalMaterials) {
        this.symbol = symbol;
        this.invisible = invisible;
        this.color = color;
        this.standard = false;
        this.material = Objects.requireNonNullElse(material, GTMaterials.NULL);
        this.additionalMaterials = new ArrayList<>(Arrays.asList(additionalMaterials));
    }

    public static Element create(String symbol) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, false));
    }

    public static Element create(String symbol, boolean invisible) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, invisible));
    }

    public static Element create(String symbol, Color color, @Nullable Material material,
                                 Material... additionalMaterials) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, false, color, material, additionalMaterials));
    }

    public static Element create(String symbol, boolean invisible, Color color, @Nullable Material material,
                                 Material... additionalMaterials) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, invisible, color, material, additionalMaterials));
    }

    private static Element createStandard(String symbol, Integer color, Material material,
                                          Material... additionalMaterials) {
        final var el = create(symbol, Objects.isNull(color) ? Color.NONE : new Color.Optional(color | (0xff << 24)),
                material, additionalMaterials);
        el.standard = true;
        return el;
    }

    public static Optional<Element> forMaterial(Material material) {
        for (final var e : elements.values())
            if (e.material.equals(material) || e.additionalMaterials.stream().anyMatch(mat -> mat.equals(material)))
                return Optional.of(e);
        return Optional.empty();
    }

    public Element posIon() {
        return Element.create(symbol + "⁺", invisible, color, material);
    }

    public Element negIon() {
        return Element.create(symbol + "⁻", invisible, color, material);
    }

    public static Element H = Element.createStandard("H", 0xffffff, GTMaterials.Hydrogen);
    public static Element D = Element.createStandard("D", 0xffffc0, GTMaterials.Deuterium);
    public static Element T = Element.createStandard("T", 0xffffa0, GTMaterials.Tritium);
    public static Element He = Element.createStandard("He", 0xd9ffff, GTMaterials.Helium, GTMaterials.Helium3);
    public static Element Li = Element.createStandard("Li", 0xcc80ff, GTMaterials.Lithium);
    public static Element Be = Element.createStandard("Be", 0xc2ff00, GTMaterials.Beryllium);
    public static Element B = Element.createStandard("B", 0xffb5b5, GTMaterials.Boron);
    public static Element C = Element.createStandard("C", 0x909090, GTMaterials.Carbon);
    public static Element N = Element.createStandard("N", 0x3050f8, GTMaterials.Nitrogen);
    public static Element O = Element.createStandard("O", 0xff0d0d, GTMaterials.Oxygen);
    public static Element F = Element.createStandard("F", 0x90e050, GTMaterials.Fluorine);
    public static Element Ne = Element.createStandard("Ne", 0xb3e3f5, GTMaterials.Neon);
    public static Element Na = Element.createStandard("Na", 0xab5cf2, GTMaterials.Sodium);
    public static Element Mg = Element.createStandard("Mg", 0x8aff00, GTMaterials.Magnesium);
    public static Element Al = Element.createStandard("Al", 0xbfa6a6, GTMaterials.Aluminium);
    public static Element Si = Element.createStandard("Si", 0xf0c8a0, GTMaterials.Silicon);
    public static Element P = Element.createStandard("P", 0xff8000, GTMaterials.Phosphorus);
    public static Element S = Element.createStandard("S", 0xffff30, GTMaterials.Sulfur);
    public static Element Cl = Element.createStandard("Cl", 0x1ff01f, GTMaterials.Chlorine);
    public static Element Ar = Element.createStandard("Ar", 0x80d1e3, GTMaterials.Argon);
    public static Element K = Element.createStandard("K", 0x8f40d4, GTMaterials.Potassium);
    public static Element Ca = Element.createStandard("Ca", 0x3dff00, GTMaterials.Calcium);
    public static Element Sc = Element.createStandard("Sc", 0xe6e6e6, GTMaterials.Scandium);
    public static Element Ti = Element.createStandard("Ti", 0xbfc2c7, GTMaterials.Titanium);
    public static Element V = Element.createStandard("V", 0xa6a6ab, GTMaterials.Vanadium);
    public static Element Cr = Element.createStandard("Cr", 0x8a99c7, GTMaterials.Chromium);
    public static Element Mn = Element.createStandard("Mn", 0x9c7ac7, GTMaterials.Manganese);
    public static Element Fe = Element.createStandard("Fe", 0xe06633, GTMaterials.Iron);
    public static Element Co = Element.createStandard("Co", 0xf090a0, GTMaterials.Cobalt);
    public static Element Ni = Element.createStandard("Ni", 0x50d050, GTMaterials.Nickel);
    public static Element Cu = Element.createStandard("Cu", 0xc88033, GTMaterials.Copper);
    public static Element Zn = Element.createStandard("Zn", 0x7d80b0, GTMaterials.Zinc);
    public static Element Ga = Element.createStandard("Ga", 0xc28f8f, GTMaterials.Gallium);
    public static Element Ge = Element.createStandard("Ge", 0x668f8f, GTMaterials.Germanium);
    public static Element As = Element.createStandard("As", 0xbd80e3, GTMaterials.Arsenic);
    public static Element Se = Element.createStandard("Se", 0xffa100, GTMaterials.Selenium);
    public static Element Br = Element.createStandard("Br", 0xa62929, GTMaterials.Bromine);
    public static Element Kr = Element.createStandard("Kr", 0x5cb8d1, GTMaterials.Krypton);
    public static Element Rb = Element.createStandard("Rb", 0x702eb0, GTMaterials.Rubidium);
    public static Element Sr = Element.createStandard("Sr", 0x00ff00, GTMaterials.Strontium);
    public static Element Y = Element.createStandard("Y", 0x94ffff, GTMaterials.Yttrium);
    public static Element Zr = Element.createStandard("Zr", 0x94e0e0, GTMaterials.Zirconium);
    public static Element Nb = Element.createStandard("Nb", 0x73c2c9, GTMaterials.Niobium);
    public static Element Mo = Element.createStandard("Mo", 0x54b5b5, GTMaterials.Molybdenum);
    public static Element Tc = Element.createStandard("Tc", 0x3b9e9e, GTMaterials.Technetium);
    public static Element Ru = Element.createStandard("Ru", 0x248f8f, GTMaterials.Ruthenium);
    public static Element Rh = Element.createStandard("Rh", 0x0a7d8c, GTMaterials.Rhodium);
    public static Element Pd = Element.createStandard("Pd", 0x006985, GTMaterials.Palladium);
    public static Element Ag = Element.createStandard("Ag", 0xc0c0c0, GTMaterials.Silver);
    public static Element Cd = Element.createStandard("Cd", 0xffd98f, GTMaterials.Cadmium);
    public static Element In = Element.createStandard("In", 0xa67573, GTMaterials.Indium);
    public static Element Sn = Element.createStandard("Sn", 0x668080, GTMaterials.Tin);
    public static Element Sb = Element.createStandard("Sb", 0x9e63b5, GTMaterials.Antimony);
    public static Element Te = Element.createStandard("Te", 0xd47a00, GTMaterials.Tellurium);
    public static Element I = Element.createStandard("I", 0x940094, GTMaterials.Iodine);
    public static Element Xe = Element.createStandard("Xe", 0x429eb0, GTMaterials.Xenon);
    public static Element Cs = Element.createStandard("Cs", 0x57178f, GTMaterials.Caesium);
    public static Element Ba = Element.createStandard("Ba", 0x00c900, GTMaterials.Barium);
    public static Element La = Element.createStandard("La", 0x70d4ff, GTMaterials.Lanthanum);
    public static Element Ce = Element.createStandard("Ce", 0xffffc7, GTMaterials.Cerium);
    public static Element Pr = Element.createStandard("Pr", 0xd9ffc7, GTMaterials.Praseodymium);
    public static Element Nd = Element.createStandard("Nd", 0xc7ffc7, GTMaterials.Neodymium);
    public static Element Pm = Element.createStandard("Pm", 0xa3ffc7, GTMaterials.Promethium);
    public static Element Sm = Element.createStandard("Sm", 0x8fffc7, GTMaterials.Samarium);
    public static Element Eu = Element.createStandard("Eu", 0x61ffc7, GTMaterials.Europium);
    public static Element Gd = Element.createStandard("Gd", 0x45ffc7, GTMaterials.Gadolinium);
    public static Element Tb = Element.createStandard("Tb", 0x30ffc7, GTMaterials.Terbium);
    public static Element Dy = Element.createStandard("Dy", 0x1fffc7, GTMaterials.Dysprosium);
    public static Element Ho = Element.createStandard("Ho", 0x00ff9c, GTMaterials.Holmium);
    public static Element Er = Element.createStandard("Er", 0x00e675, GTMaterials.Erbium);
    public static Element Tm = Element.createStandard("Tm", 0x00d452, GTMaterials.Thulium);
    public static Element Yb = Element.createStandard("Yb", 0x00bf38, GTMaterials.Ytterbium);
    public static Element Lu = Element.createStandard("Lu", 0x00ab24, GTMaterials.Lutetium);
    public static Element Hf = Element.createStandard("Hf", 0x4dc2ff, GTMaterials.Hafnium);
    public static Element Ta = Element.createStandard("Ta", 0x4da6ff, GTMaterials.Tantalum);
    public static Element W = Element.createStandard("W", 0x2194d6, GTMaterials.Tungsten);
    public static Element Re = Element.createStandard("Re", 0x267dab, GTMaterials.Rhenium);
    public static Element Os = Element.createStandard("Os", 0x266696, GTMaterials.Osmium);
    public static Element Ir = Element.createStandard("Ir", 0x175487, GTMaterials.Iridium);
    public static Element Pt = Element.createStandard("Pt", 0xd0d0e0, GTMaterials.Platinum);
    public static Element Au = Element.createStandard("Au", 0xffd123, GTMaterials.Gold);
    public static Element Hg = Element.createStandard("Hg", 0xb8b8d0, GTMaterials.Mercury);
    public static Element Tl = Element.createStandard("Tl", 0xa6544d, GTMaterials.Thallium);
    public static Element Pb = Element.createStandard("Pb", 0x575961, GTMaterials.Lead);
    public static Element Bi = Element.createStandard("Bi", 0x9e4fb5, GTMaterials.Bismuth);
    public static Element Po = Element.createStandard("Po", 0xab5c00, GTMaterials.Polonium);
    public static Element At = Element.createStandard("At", 0x754f45, GTMaterials.Astatine);
    public static Element Rn = Element.createStandard("Rn", 0x428296, GTMaterials.Radon);
    public static Element Fr = Element.createStandard("Fr", 0x420066, GTMaterials.Francium);
    public static Element Ra = Element.createStandard("Ra", 0x007d00, GTMaterials.Radium);
    public static Element Ac = Element.createStandard("Ac", 0x70abfa, GTMaterials.Actinium);
    public static Element Th = Element.createStandard("Th", 0x00baff, GTMaterials.Thorium);
    public static Element Pa = Element.createStandard("Pa", 0x00a1ff, GTMaterials.Protactinium);
    public static Element U = Element.createStandard("U", 0x008fff, GTMaterials.Uranium238, GTMaterials.Uranium235);
    public static Element Np = Element.createStandard("Np", 0x0080ff, GTMaterials.Neptunium);
    public static Element Pu = Element.createStandard("Pu", 0x006bff, GTMaterials.Plutonium239,
            GTMaterials.Plutonium241);
    public static Element Am = Element.createStandard("Am", 0x545cf2, GTMaterials.Americium);
    public static Element Cm = Element.createStandard("Cm", 0x785ce3, GTMaterials.Curium);
    public static Element Bk = Element.createStandard("Bk", 0x8a4fe3, GTMaterials.Berkelium);
    public static Element Cf = Element.createStandard("Cf", 0xa136d4, GTMaterials.Californium);
    public static Element Es = Element.createStandard("Es", 0xb31fd4, GTMaterials.Einsteinium);
    public static Element Fm = Element.createStandard("Fm", 0xb31fba, GTMaterials.Fermium);
    public static Element Md = Element.createStandard("Md", 0xb30da6, GTMaterials.Mendelevium);
    public static Element No = Element.createStandard("No", 0xbd0d87, GTMaterials.Nobelium);
    public static Element Lr = Element.createStandard("Lr", 0xc70066, GTMaterials.Lawrencium);
    public static Element Rf = Element.createStandard("Rf", 0xcc0059, GTMaterials.Rutherfordium);
    public static Element Db = Element.createStandard("Db", 0xd1004f, GTMaterials.Dubnium);
    public static Element Sg = Element.createStandard("Sg", 0xd90045, GTMaterials.Seaborgium);
    public static Element Bh = Element.createStandard("Bh", 0xe00038, GTMaterials.Bohrium);
    public static Element Hs = Element.createStandard("Hs", 0xe6002e, GTMaterials.Hassium);
    public static Element Mt = Element.createStandard("Mt", 0xeb0026, GTMaterials.Meitnerium);
    public static Element Ds = Element.createStandard("Ds", null, GTMaterials.Darmstadtium);
    public static Element Rg = Element.createStandard("Rg", null, GTMaterials.Roentgenium);
    public static Element Cn = Element.createStandard("Cn", null, GTMaterials.Copernicium);
    public static Element Nh = Element.createStandard("Nh", null, GTMaterials.Nihonium);
    public static Element Fl = Element.createStandard("Fl", null, GTMaterials.Flerovium);
    public static Element Mc = Element.createStandard("Mc", null, GTMaterials.Moscovium);
    public static Element Lv = Element.createStandard("Lv", null, GTMaterials.Livermorium);
    public static Element Ts = Element.createStandard("Ts", null, GTMaterials.Tennessine);
    public static Element Og = Element.createStandard("Og", null, GTMaterials.Oganesson);

    public static Element INVISIBLE = elements.computeIfAbsent("", s -> new Element(s, true));
    public static Element BULLET = Element.create("•");

    public static Element Nq = Element.createStandard("Nq", null, GTMaterials.Naquadah, GTMaterials.NaquadahEnriched,
            GTMaterials.Naquadria);
    public static Element Ke = Element.createStandard("Ke", null, GTMaterials.Trinium);
    public static Element Tr = Element.createStandard("Tr", null, GTMaterials.Tritanium);
    public static Element Dr = Element.createStandard("Dr", null, GTMaterials.Duranium);
    public static Element Nt = Element.createStandard("Nt", null, GTMaterials.Neutronium);

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
                        jsonDeserializationContext.deserialize(obj.get("color"), Element.Color.class),
                        obj.has("material") ? GTCEuAPI.materialManager.getMaterial(obj.get("material").getAsString()) :
                                null);
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
                obj.add("color", jsonSerializationContext.serialize(element.color, Element.Color.class));
            if (!element.material.isNull())
                obj.add("material", new JsonPrimitive(element.material.getResourceLocation().toString()));
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
