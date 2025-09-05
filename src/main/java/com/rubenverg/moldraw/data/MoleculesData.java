package com.rubenverg.moldraw.data;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.resources.ResourceLocation;

import com.rubenverg.moldraw.molecule.*;

import java.util.HashMap;
import java.util.Map;

public class MoleculesData {

    public static Map<ResourceLocation, Molecule> molecules() {
        final Map<ResourceLocation, Molecule> molecules = new HashMap<>();
        molecules.put(GTCEu.id("methane"), new Molecule()
                .uv()
                .atom(Element.BULLET, 0, 0));
        molecules.put(GTCEu.id("ethane"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .bond(0, 1));
        molecules.put(GTCEu.id("ethylene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .bond(0, 1, Bond.Type.DOUBLE));
        molecules.put(GTCEu.id("propane"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .bond(0, 1)
                .bond(1, 2));
        molecules.put(GTCEu.id("propene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE));
        molecules.put(GTCEu.id("butene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3));
        molecules.put(GTCEu.id("butadiene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .bond(0, 1, Bond.Type.DOUBLE)
                .bond(1, 2)
                .bond(2, 3, Bond.Type.DOUBLE));
        molecules.put(GTCEu.id("polyethylene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .add(Parens.polymer(1, 2)));
        molecules.put(GTCEu.id("polyvinyl_chloride"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .atom(Element.Cl, 0, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(2, 4)
                .add(Parens.polymer(1, 2, 4)));
        molecules.put(GTCEu.id("polytetrafluoroethylene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .atom(Element.F, 0, 1)
                .atom(Element.F, 2, -1)
                .atom(Element.F, 0, 2)
                .atom(Element.F, 2, 0)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(1, 4)
                .bond(1, 5)
                .bond(2, 6)
                .bond(2, 7)
                .add(Parens.polymer(1, 2, 4, 5, 6, 7)));
        molecules.put(GTCEu.id("polybenzimidazole"), new Molecule()
                .xy()
                .invAtom(0, 0) // 0
                .invAtom(1, 0) // 1
                .bond(0, 1)
                .atom(Element.N, 1 + MathUtils.COS54f, MathUtils.SIN54f) // 2
                .atom(Element.H, 1 + MathUtils.COS54f, MathUtils.SIN54f + 0.5f) // 3
                .atom(Element.N, 1 + MathUtils.COS54f, -MathUtils.SIN54f) // 4
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf, 0.5f) // 5
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf, -0.5f) // 6
                .bond(1, 2)
                .bond(2, 3)
                .bond(1, 4, Bond.Type.DOUBLE)
                .bond(2, 5)
                .bond(4, 6)
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + MathUtils.COS30f, 1) // 7
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + MathUtils.COS30f, -1) // 8
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f, 0.5f) // 9
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f, -0.5f) // 10
                .bond(5, 6, Bond.Type.DOUBLE)
                .bond(5, 7)
                .bond(6, 8)
                .bond(9, 7, Bond.Type.DOUBLE)
                .bond(8, 10, Bond.Type.DOUBLE)
                .bond(9, 10)
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1, 0.5f) // 11
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1, -0.5f) // 12
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                        MathUtils.COS30f, 1) // 13
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                        MathUtils.COS30f, -1) // 14
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                        2 * MathUtils.COS30f, 0.5f) // 15
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                        2 * MathUtils.COS30f, -0.5f) // 16
                .bond(9, 11)
                .bond(11, 12)
                .bond(13, 11, Bond.Type.DOUBLE)
                .bond(12, 14, Bond.Type.DOUBLE)
                .bond(13, 15)
                .bond(14, 16)
                .bond(15, 16, Bond.Type.DOUBLE)
                .atom(Element.N,
                        1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                                2 * MathUtils.COS30f + 0.95f,
                        MathUtils.SIN54f) // 17
                .atom(Element.H,
                        1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                                2 * MathUtils.COS30f + 0.95f,
                        MathUtils.SIN54f + 0.5f) // 18
                .atom(Element.N,
                        1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                                2 * MathUtils.COS30f + 0.95f,
                        -MathUtils.SIN54f) // 19
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                        2 * MathUtils.COS30f + MathUtils.COS18f * MathUtils.PHIf, 0) // 20
                .bond(15, 17)
                .bond(17, 18)
                .bond(16, 19)
                .bond(17, 20)
                .bond(19, 20, Bond.Type.DOUBLE)
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                        2 * MathUtils.COS30f + MathUtils.COS18f * MathUtils.PHIf + 1, 0) // 21
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                        2 * MathUtils.COS30f + MathUtils.COS18f * MathUtils.PHIf + 1, -1) // 22
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                        2 * MathUtils.COS30f + MathUtils.COS18f * MathUtils.PHIf + 1 +
                        MathUtils.COS30f, 0.5f) // 23
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                        2 * MathUtils.COS30f + MathUtils.COS18f * MathUtils.PHIf + 1 +
                        MathUtils.COS30f, -1.5f) // 24
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                        2 * MathUtils.COS30f + MathUtils.COS18f * MathUtils.PHIf + 1 +
                        2 * MathUtils.COS30f, 0) // 25
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                        2 * MathUtils.COS30f + MathUtils.COS18f * MathUtils.PHIf + 1 +
                        2 * MathUtils.COS30f, -1) // 26
                .bond(20, 21)
                .bond(21, 22, Bond.Type.DOUBLE)
                .bond(21, 23)
                .bond(22, 24)
                .bond(25, 23, Bond.Type.DOUBLE)
                .bond(24, 26, Bond.Type.DOUBLE)
                .bond(25, 26)
                .invAtom(1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 +
                        2 * MathUtils.COS30f + MathUtils.COS18f * MathUtils.PHIf + 1 +
                        2 * MathUtils.COS30f + 1, 0) // 27
                .bond(25, 27)
                .add(Parens.polymer(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
                        19, 20, 21, 22, 23, 24, 25, 26)));
        molecules.put(GTCEu.id("benzene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE));
        molecules.put(GTCEu.id("toluene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .invAtom(2, -1)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE)
                .bond(5, 6));
        molecules.put(GTCEu.id("polyvinyl_acetate"), new Molecule()
                .uv()
                .invAtom(0, 0) // 0
                .invAtom(1, 0) // 1
                .invAtom(1, 1) // 2
                .invAtom(2, 1) // 3
                .atom(Element.O, 0, 2) // 4
                .invAtom(-1, 2) // 5
                .invAtom(-1, 1) // 6
                .atom(Element.O, -2, 3) // 7
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(2, 4)
                .bond(4, 5)
                .bond(5, 6)
                .bond(5, 7, Bond.Type.DOUBLE_CENTERED)
                .add(Parens.polymer(1, 2)));
        molecules.put(GTCEu.id("polyphenylene_sulfide"), new Molecule()
                .uv()
                .invAtom(0, 0) // 0
                .invAtom(1, 0) // 1
                .bond(0, 1)
                .invAtom(1, 1) // 2
                .invAtom(2, 1) // 3
                .invAtom(3, 0) // 4
                .invAtom(3, -1) // 5
                .invAtom(2, -1) // 6
                .bond(1, 2)
                .bond(2, 3, Bond.Type.DOUBLE)
                .bond(3, 4)
                .bond(4, 5, Bond.Type.DOUBLE)
                .bond(5, 6)
                .bond(6, 1, Bond.Type.DOUBLE)
                .atom(Element.S, 4, 0) // 7
                .invAtom(5, 0) // 8
                .bond(4, 7)
                .bond(7, 8)
                .add(Parens.polymer(1, 2, 3, 4, 5, 6, 7)));
        molecules.put(GTCEu.id("polycaprolactam"), new Molecule()
                .uv()
                .invAtom(0, 0) // 0
                .atom(Element.N, 1, 0) // 1
                .atom(Element.H, 1.5f, -0.5f) // 2
                .invAtom(1, 1) // 3
                .invAtom(2, 1) // 4
                .invAtom(2, 2) // 5
                .invAtom(3, 2) // 6
                .invAtom(3, 3) // 7
                .invAtom(4, 3) // 8
                .atom(Element.O, 5, 2) // 9
                .invAtom(4, 4) // 10
                .bond(0, 1)
                .bond(1, 2)
                .bond(1, 3)
                .bond(3, 4)
                .bond(4, 5)
                .bond(5, 6)
                .bond(6, 7)
                .bond(7, 8)
                .bond(8, 9, Bond.Type.DOUBLE_CENTERED)
                .bond(8, 10)
                .add(Parens.polymer(1, 2, 3, 4, 5, 6, 7, 8, 9)));
        molecules.put(GTCEu.id("polyvinyl_butyral"), new Molecule()
                .uv()
                .invAtom(0, 0) // 0
                .invAtom(0, 1) // 1
                .invAtom(1, 1) // 2
                .invAtom(1, 2) // 3
                .invAtom(2, 2) // 4
                .invAtom(2, 3) // 5
                .atom(Element.O, -1, 2) // 6
                .atom(Element.O, 0, 3) // 7
                .invAtom(-1, 3) // 8
                .invAtom(-2, 4) // 9
                .invAtom(-2, 5) // 10
                .invAtom(-3, 6) // 11
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4)
                .bond(4, 5)
                .bond(1, 6)
                .bond(3, 7)
                .bond(6, 8)
                .bond(7, 8)
                .bond(8, 9)
                .bond(9, 10)
                .bond(10, 11)
                .add(Parens.polymer(1, 2, 3, 4, 6, 7, 8, 9, 10, 11)));
        molecules.put(GTCEu.id("dimethylbenzene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .invAtom(2, -1)
                .invAtom(-2, 3)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE)
                .bond(5, 6)
                .bond(2, 7));
        molecules.put(GTCEu.id("methanol"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .atom(Element.create("OH"), 1, 0)
                .bond(0, 1));
        molecules.put(GTCEu.id("acetone"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O, 2, -1)
                .invAtom(1, 1)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE_CENTERED)
                .bond(1, 3));
        molecules.put(GTCEu.id("methyl_acetate"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O, 2, -1)
                .atom(Element.O, 1, 1)
                .invAtom(2, 1)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE_CENTERED)
                .bond(1, 3)
                .bond(3, 4));
        molecules.put(GTCEu.id("ethanol"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.create("OH"), 1, 1)
                .bond(0, 1)
                .bond(1, 2));
        molecules.put(GTCEu.id("acetic_acid"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O, 2, -1)
                .atom(Element.create("OH"), 1, 1)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE_CENTERED)
                .bond(1, 3));
        molecules.put(GTCEu.id("phenol"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .atom(Element.create("OH"), 2, -1)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE)
                .bond(5, 6));
        molecules.put(GTCEu.id("ethylbenzene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .invAtom(2, 1)
                .invAtom(2, 2)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE)
                .bond(4, 6)
                .bond(6, 7));
        molecules.put(GTCEu.id("naphthalene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE)
                .invAtom(0, 3)
                .invAtom(1, 3)
                .invAtom(2, 2)
                .invAtom(2, 1)
                .bond(3, 6, Bond.Type.DOUBLE)
                .bond(6, 7)
                .bond(7, 8, Bond.Type.DOUBLE)
                .bond(8, 9)
                .bond(9, 4, Bond.Type.DOUBLE));
        molecules.put(GTCEu.id("ammonium_formate"), new Molecule()
                .uv()
                .xy()
                .atom(Element.N, 0, 0) // 0
                .atom(Element.H, 0, 1) // 1
                .atom(Element.H, 0, -1) // 2
                .atom(Element.H, 1, 0) // 3
                .atom(Element.H, -1, 0) // 4
                .bond(0, 1)
                .bond(0, 2)
                .bond(0, 3)
                .bond(0, 4)
                .add(Parens.posIon(0, 1, 2, 3, 4))
                .atom(Element.H, 2.5f, 0) // 5
                .atom(Element.C, 3.5f, 0) // 6
                .atom(Element.O, 4, MathUtils.COS30f) // 7
                .atom(Element.O, 4, -MathUtils.COS30f) // 8
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8, Bond.Type.DOUBLE)
                .add(Parens.negIon(5, 6, 7, 8)));
        molecules.put(GTCEu.id("glycerol"), new Molecule()
                .uv()
                .atom(Element.create("HO"), 0, 0)
                .invAtom(0, 1)
                .invAtom(1, 1)
                .atom(Element.create("OH"), 2, 0)
                .invAtom(1, 2)
                .atom(Element.create("OH"), 2, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(2, 4)
                .bond(4, 5));
        molecules.put(GTCEu.id("epichlorohydrin"), new Molecule()
                .uv()
                .atom(Element.O, 0, 0)
                .invAtom(0, 1)
                .invAtom(-1, 1)
                .invAtom(0, 2)
                .atom(Element.Cl, 1, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 0)
                .bond(1, 3)
                .bond(3, 4));
        molecules.put(GTCEu.id("formamide"), new Molecule()
                .uv()
                .atom(Element.O, 0, 0)
                .invAtom(-1, 1)
                .atom(Element.create("NH₂"), -1.25f, 2.25f)
                .bond(0, 1, Bond.Type.DOUBLE_CENTERED)
                .bond(1, 2));
        molecules.put(GTCEu.id("formaldehyde"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .atom(Element.O, 1, 0)
                .bond(0, 1, Bond.Type.DOUBLE_CENTERED));
        molecules.put(GTCEu.id("allyl_chloride"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .atom(Element.Cl, 2, 1)
                .bond(0, 1, Bond.Type.DOUBLE)
                .bond(1, 2)
                .bond(2, 3));
        molecules.put(GTCEu.id("formic_acid"), new Molecule()
                .uv()
                .atom(Element.O, 0, 0)
                .invAtom(-1, 1)
                .atom(Element.create("OH"), -1, 2)
                .bond(0, 1, Bond.Type.DOUBLE_CENTERED)
                .bond(1, 2));
        molecules.put(GTCEu.id("dichloroethane"), new Molecule()
                .uv()
                .atom(Element.Cl, 0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .atom(Element.Cl, 2, 1)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3));
        molecules.put(GTCEu.id("cyclohexane"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4)
                .bond(4, 5)
                .bond(5, 0));
        molecules.put(GTCEu.id("sugar"), new Molecule()
                .uv()
                .atom(Element.create("HO"), 0, 0) // 0
                .invAtom(0, 1) // 1
                .invAtom(1, 1) // 2
                .atom(Element.create("OH"), 2, 0) // 3
                .invAtom(1, 2) // 4
                .atom(Element.create("OH"), 0, 3) // 5
                .invAtom(2, 2) // 6
                .atom(Element.create("OH"), 3, 1) // 7
                .invAtom(2, 3) // 8
                .atom(Element.create("OH"), 1, 4) // 9
                .invAtom(3, 3) // 10
                .atom(Element.O, 3, 4) // 11
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(2, 4)
                .bond(4, 5)
                .bond(4, 6)
                .bond(6, 7)
                .bond(6, 8)
                .bond(8, 9)
                .bond(8, 10)
                .bond(10, 11, Bond.Type.DOUBLE));
        molecules.put(GTCEu.id("isoprene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(2, -1)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .bond(0, 1, Bond.Type.DOUBLE)
                .bond(1, 2)
                .bond(1, 3)
                .bond(3, 4, Bond.Type.DOUBLE));
        return molecules;
    }
}
