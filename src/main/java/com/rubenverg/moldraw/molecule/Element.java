package com.rubenverg.moldraw.molecule;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.*;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IOreMaterial;
import gtPlusPlus.core.material.MaterialsElements;

public class Element {

    private static final Map<String, Element> elements = new HashMap<>();

    public final String symbol;
    public final boolean invisible;
    public final Color color;
    public final @Nullable IOreMaterial material;
    public final List<IOreMaterial> additionalMaterials;
    boolean standard;

    protected Element(String symbol, boolean invisible) {
        this.symbol = symbol;
        this.invisible = invisible;
        this.color = Color.NONE;
        this.standard = false;
        this.material = null;
        this.additionalMaterials = new ArrayList<>();
    }

    protected Element(String symbol, boolean invisible, Color color, @Nullable IOreMaterial material,
        IOreMaterial... additionalMaterials) {
        this.symbol = symbol;
        this.invisible = invisible;
        this.color = color;
        this.standard = false;
        this.material = material;
        this.additionalMaterials = new ArrayList<>(Arrays.asList(additionalMaterials));
    }

    public static Element create(String symbol) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, false));
    }

    public static Element create(String symbol, boolean invisible) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, invisible));
    }

    public static Element create(String symbol, Color color, @Nullable IOreMaterial material,
        IOreMaterial... additionalMaterials) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, false, color, material, additionalMaterials));
    }

    public static Element create(String symbol, boolean invisible, Color color, @Nullable IOreMaterial material,
        IOreMaterial... additionalMaterials) {
        return elements.computeIfAbsent(symbol, s -> new Element(s, invisible, color, material, additionalMaterials));
    }

    private static Element createStandard(String symbol, Integer color, IOreMaterial material,
        IOreMaterial... additionalMaterials) {
        final var el = create(
            symbol,
            Objects.isNull(color) ? Color.NONE : new Color.Optional(color | (0xff << 24)),
            material,
            additionalMaterials);
        el.standard = true;
        return el;
    }

    public static Optional<Element> forMaterial(IOreMaterial material) {
        for (final var e : elements.values())
            if (Objects.nonNull(e.material) && e.material.equals(material) || e.additionalMaterials.stream()
                .anyMatch(mat -> mat.equals(material))) return Optional.of(e);
        return Optional.empty();
    }

    public Element posIon() {
        return Element.create(symbol + "⁺", invisible, color, material);
    }

    public Element negIon() {
        return Element.create(symbol + "⁻", invisible, color, material);
    }

    public static Element H = Element.createStandard("H", 0xffffff, Materials.Hydrogen);
    public static Element D = Element.createStandard("D", 0xffffc0, Materials.Deuterium);
    public static Element T = Element.createStandard("T", 0xffffa0, Materials.Tritium);
    public static Element He = Element.createStandard("He", 0xd9ffff, Materials.Helium, Materials.Helium3);
    public static Element Li = Element.createStandard("Li", 0xcc80ff, Materials.Lithium);
    public static Element Be = Element.createStandard("Be", 0xc2ff00, Materials.Beryllium);
    public static Element B = Element.createStandard("B", 0xffb5b5, Materials.Boron);
    public static Element C = Element.createStandard("C", 0x909090, Materials.Carbon);
    public static Element N = Element.createStandard("N", 0x3050f8, Materials.Nitrogen);
    public static Element O = Element.createStandard("O", 0xff0d0d, Materials.Oxygen);
    public static Element F = Element.createStandard("F", 0x90e050, Materials.Fluorine);
    public static Element Ne = Element.createStandard("Ne", 0xb3e3f5, MaterialsElements.getInstance().NEON);
    public static Element Na = Element.createStandard("Na", 0xab5cf2, Materials.Sodium);
    public static Element Mg = Element.createStandard("Mg", 0x8aff00, Materials.Magnesium);
    public static Element Al = Element.createStandard("Al", 0xbfa6a6, Materials.Aluminium);
    public static Element Si = Element.createStandard("Si", 0xf0c8a0, Materials.Silicon);
    public static Element P = Element.createStandard("P", 0xff8000, Materials.Phosphorus);
    public static Element S = Element.createStandard("S", 0xffff30, Materials.Sulfur);
    public static Element Cl = Element.createStandard("Cl", 0x1ff01f, Materials.Chlorine);
    public static Element Ar = Element.createStandard("Ar", 0x80d1e3, Materials.Argon);
    public static Element K = Element.createStandard("K", 0x8f40d4, Materials.Potassium);
    public static Element Ca = Element.createStandard("Ca", 0x3dff00, Materials.Calcium);
    public static Element Sc = Element.createStandard("Sc", 0xe6e6e6, Materials.Scandium);
    public static Element Ti = Element.createStandard("Ti", 0xbfc2c7, Materials.Titanium);
    public static Element V = Element.createStandard("V", 0xa6a6ab, Materials.Vanadium);
    public static Element Cr = Element.createStandard("Cr", 0x8a99c7, Materials.Chrome);
    public static Element Mn = Element.createStandard("Mn", 0x9c7ac7, Materials.Manganese);
    public static Element Fe = Element.createStandard("Fe", 0xe06633, Materials.Iron);
    public static Element Co = Element.createStandard("Co", 0xf090a0, Materials.Cobalt);
    public static Element Ni = Element.createStandard("Ni", 0x50d050, Materials.Nickel);
    public static Element Cu = Element.createStandard("Cu", 0xc88033, Materials.Copper);
    public static Element Zn = Element.createStandard("Zn", 0x7d80b0, Materials.Zinc);
    public static Element Ga = Element.createStandard("Ga", 0xc28f8f, Materials.Gallium);
    public static Element Ge = Element.createStandard("Ge", 0x668f8f, MaterialsElements.getInstance().GERMANIUM);
    public static Element As = Element.createStandard("As", 0xbd80e3, Materials.Arsenic);
    public static Element Se = Element.createStandard("Se", 0xffa100, MaterialsElements.getInstance().SELENIUM);
    public static Element Br = Element.createStandard("Br", 0xa62929, MaterialsElements.getInstance().BROMINE);
    public static Element Kr = Element.createStandard("Kr", 0x5cb8d1, MaterialsElements.getInstance().KRYPTON);
    public static Element Rb = Element.createStandard("Rb", 0x702eb0, Materials.Rubidium);
    public static Element Sr = Element.createStandard("Sr", 0x00ff00, Materials.Strontium);
    public static Element Y = Element.createStandard("Y", 0x94ffff, Materials.Yttrium);
    public static Element Zr = Element.createStandard("Zr", 0x94e0e0, MaterialsElements.getInstance().ZIRCONIUM);
    public static Element Nb = Element.createStandard("Nb", 0x73c2c9, Materials.Niobium);
    public static Element Mo = Element.createStandard("Mo", 0x54b5b5, Materials.Molybdenum);
    public static Element Tc = Element.createStandard("Tc", 0x3b9e9e, MaterialsElements.getInstance().TECHNETIUM);
    public static Element Ru = Element.createStandard("Ru", 0x248f8f, MaterialsElements.getInstance().RUTHENIUM);
    public static Element Rh = Element.createStandard("Rh", 0x0a7d8c, MaterialsElements.getInstance().RHODIUM);
    public static Element Pd = Element.createStandard("Pd", 0x006985, Materials.Palladium);
    public static Element Ag = Element.createStandard("Ag", 0xc0c0c0, Materials.Silver);
    public static Element Cd = Element.createStandard("Cd", 0xffd98f, Materials.Cadmium);
    public static Element In = Element.createStandard("In", 0xa67573, Materials.Indium);
    public static Element Sn = Element.createStandard("Sn", 0x668080, Materials.Tin);
    public static Element Sb = Element.createStandard("Sb", 0x9e63b5, Materials.Antimony);
    public static Element Te = Element.createStandard("Te", 0xd47a00, Materials.Tellurium);
    public static Element I = Element.createStandard("I", 0x940094, MaterialsElements.getInstance().IODINE);
    public static Element Xe = Element.createStandard("Xe", 0x429eb0, MaterialsElements.getInstance().XENON);
    public static Element Cs = Element.createStandard("Cs", 0x57178f, Materials.Caesium);
    public static Element Ba = Element.createStandard("Ba", 0x00c900, Materials.Barium);
    public static Element La = Element.createStandard("La", 0x70d4ff, Materials.Lanthanum);
    public static Element Ce = Element.createStandard("Ce", 0xffffc7, Materials.Cerium);
    public static Element Pr = Element.createStandard("Pr", 0xd9ffc7, Materials.Praseodymium);
    public static Element Nd = Element.createStandard("Nd", 0xc7ffc7, Materials.Neodymium);
    public static Element Pm = Element.createStandard("Pm", 0xa3ffc7, Materials.Promethium);
    public static Element Sm = Element.createStandard("Sm", 0x8fffc7, Materials.Samarium);
    public static Element Eu = Element.createStandard("Eu", 0x61ffc7, Materials.Europium);
    public static Element Gd = Element.createStandard("Gd", 0x45ffc7, Materials.Gadolinium);
    public static Element Tb = Element.createStandard("Tb", 0x30ffc7, Materials.Terbium);
    public static Element Dy = Element.createStandard("Dy", 0x1fffc7, Materials.Dysprosium);
    public static Element Ho = Element.createStandard("Ho", 0x00ff9c, Materials.Holmium);
    public static Element Er = Element.createStandard("Er", 0x00e675, Materials.Erbium);
    public static Element Tm = Element.createStandard("Tm", 0x00d452, Materials.Thulium);
    public static Element Yb = Element.createStandard("Yb", 0x00bf38, Materials.Ytterbium);
    public static Element Lu = Element.createStandard("Lu", 0x00ab24, Materials.Lutetium);
    public static Element Hf = Element.createStandard("Hf", 0x4dc2ff, MaterialsElements.getInstance().HAFNIUM);
    public static Element Ta = Element.createStandard("Ta", 0x4da6ff, Materials.Tantalum);
    public static Element W = Element.createStandard("W", 0x2194d6, Materials.Tungsten);
    public static Element Re = Element.createStandard("Re", 0x267dab, MaterialsElements.getInstance().RHENIUM);
    public static Element Os = Element.createStandard("Os", 0x266696, Materials.Osmium);
    public static Element Ir = Element.createStandard("Ir", 0x175487, Materials.Iridium);
    public static Element Pt = Element.createStandard("Pt", 0xd0d0e0, Materials.Platinum);
    public static Element Au = Element.createStandard("Au", 0xffd123, Materials.Gold);
    public static Element Hg = Element.createStandard("Hg", 0xb8b8d0, Materials.Mercury);
    public static Element Tl = Element.createStandard("Tl", 0xa6544d, MaterialsElements.getInstance().THALLIUM);
    public static Element Pb = Element.createStandard("Pb", 0x575961, Materials.Lead);
    public static Element Bi = Element.createStandard("Bi", 0x9e4fb5, Materials.Bismuth);
    public static Element Po = Element.createStandard("Po", 0xab5c00, MaterialsElements.getInstance().POLONIUM);
    public static Element At = Element.createStandard("At", 0x754f45, null);
    public static Element Rn = Element.createStandard("Rn", 0x428296, Materials.Radon);
    public static Element Fr = Element.createStandard("Fr", 0x420066, null);
    public static Element Ra = Element.createStandard("Ra", 0x007d00, MaterialsElements.getInstance().RADIUM);
    public static Element Ac = Element.createStandard("Ac", 0x70abfa, null);
    public static Element Th = Element.createStandard("Th", 0x00baff, Materials.Thorium);
    public static Element Pa = Element.createStandard("Pa", 0x00a1ff, MaterialsElements.getInstance().PROTACTINIUM);
    public static Element U = Element.createStandard("U", 0x008fff, Materials.Uranium, Materials.Uranium235);
    public static Element Np = Element.createStandard("Np", 0x0080ff, MaterialsElements.getInstance().NEPTUNIUM);
    public static Element Pu = Element.createStandard("Pu", 0x006bff, Materials.Plutonium, Materials.Plutonium241);
    public static Element Am = Element.createStandard("Am", 0x545cf2, Materials.Americium);
    public static Element Cm = Element.createStandard("Cm", 0x785ce3, MaterialsElements.getInstance().CURIUM);
    public static Element Bk = Element.createStandard("Bk", 0x8a4fe3, null);
    public static Element Cf = Element.createStandard("Cf", 0xa136d4, MaterialsElements.getInstance().CALIFORNIUM);
    public static Element Es = Element.createStandard("Es", 0xb31fd4, null);
    public static Element Fm = Element.createStandard("Fm", 0xb31fba, MaterialsElements.getInstance().FERMIUM);
    public static Element Md = Element.createStandard("Md", 0xb30da6, null);
    public static Element No = Element.createStandard("No", 0xbd0d87, null);
    public static Element Lr = Element.createStandard("Lr", 0xc70066, null);
    public static Element Rf = Element.createStandard("Rf", 0xcc0059, null);
    public static Element Db = Element.createStandard("Db", 0xd1004f, null);
    public static Element Sg = Element.createStandard("Sg", 0xd90045, null);
    public static Element Bh = Element.createStandard("Bh", 0xe00038, null);
    public static Element Hs = Element.createStandard("Hs", 0xe6002e, null);
    public static Element Mt = Element.createStandard("Mt", 0xeb0026, null);
    public static Element Ds = Element.createStandard("Ds", null, null);
    public static Element Rg = Element.createStandard("Rg", null, null);
    public static Element Cn = Element.createStandard("Cn", null, null);
    public static Element Nh = Element.createStandard("Nh", null, null);
    public static Element Fl = Element.createStandard("Fl", null, Materials.Flerovium);
    public static Element Mc = Element.createStandard("Mc", null, null);
    public static Element Lv = Element.createStandard("Lv", null, null);
    public static Element Ts = Element.createStandard("Ts", null, null);
    public static Element Og = Element.createStandard("Og", null, null);

    public static Element INVISIBLE = elements.computeIfAbsent("", s -> new Element(s, true));
    public static Element BULLET = Element.create("•");

    public static Element Nq = Element
        .createStandard("Nq", null, Materials.Naquadah, Materials.NaquadahEnriched, Materials.Naquadria);
    public static Element Ke = Element.createStandard("Ke", null, Materials.Trinium);
    public static Element Tr = Element.createStandard("Tr", null, Materials.Tritanium);
    public static Element Dr = Element.createStandard("Dr", null, Materials.Duranium);
    public static Element Nt = Element.createStandard("Nt", null, Materials.Neutronium);

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

    public interface Color {

        Color NONE = new None();

        record None() implements Color {}

        record Always(int color) implements Color {}

        record Optional(int color) implements Color {}
    }

    public record Counted(Element element, int count) {

        @Override
        public @NotNull String toString() {
            if (count == 1) return element.symbol;
            final var builder = new StringBuilder(element.symbol);
            for (final var ch : Integer.toString(count)
                .getBytes(StandardCharsets.UTF_8)) {
                builder.appendCodePoint(ch + '₀' - '0');
            }
            return builder.toString();
        }
    }
}
