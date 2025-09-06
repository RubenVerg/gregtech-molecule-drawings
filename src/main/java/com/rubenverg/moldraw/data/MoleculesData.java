package com.rubenverg.moldraw.data;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

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
        molecules.put(GTCEu.id("glycolonitrile"), new Molecule()
                .uv()
                .atom(Element.create("HO"), 0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .atom(Element.N, 1, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3, Bond.Type.TRIPLE));
        molecules.put(GTCEu.id("diethylenetriamine"), new Molecule()
                .uv()
                .atom(Element.create("H₂N"), 0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .atom(Element.N, 2, 1)
                .atom(Element.H, 2.5f, 0.5f)
                .invAtom(2, 2)
                .invAtom(3, 2)
                .atom(Element.create("NH₂"), 3, 3)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4)
                .bond(3, 5)
                .bond(5, 6)
                .bond(6, 7));
        molecules.put(GTCEu.id("nitrobenzene"), new Molecule()
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
                .bond(5, 0, Bond.Type.DOUBLE)
                .atom(Element.N, 2, -1)
                .atom(Element.O, 2, -2)
                .atom(Element.O, 3, -1)
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8));
        molecules.put(GTCEu.id("glyceryl_trinitrate"), new Molecule()
                .uv()
                .atom(Element.O, 0, 0) // 0
                .atom(Element.N, 0, 1) // 1
                .atom(Element.O, -1, 2) // 2
                .atom(Element.O, 1, 1) // 3
                .bond(0, 1)
                .bond(1, 2)
                .bond(1, 3)
                .invAtom(1, 2) // 4
                .invAtom(2, 2) // 5
                .bond(3, 4)
                .bond(4, 5)
                .atom(Element.O, 3, 1) // 6
                .atom(Element.N, 3, 0) // 7
                .atom(Element.O, 2, 0) // 8
                .atom(Element.O, 4, -1) // 9
                .bond(5, 6)
                .bond(6, 7)
                .bond(7, 8)
                .bond(7, 9)
                .invAtom(2, 3) // 10
                .atom(Element.O, 3, 3) // 11
                .atom(Element.N, 3, 4) // 12
                .atom(Element.O, 4, 4) // 13
                .atom(Element.O, 2, 5) // 14
                .bond(5, 10)
                .bond(10, 11)
                .bond(11, 12)
                .bond(12, 13)
                .bond(12, 14));
        final var sir = new Molecule()
                .uv()
                .invAtom(0, 0)
                .atom(Element.O, 0, 1)
                .atom(Element.Si, 1, 1)
                .invAtom(2, 0)
                .invAtom(0, 2)
                .invAtom(1, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(2, 4)
                .bond(2, 5)
                .add(Parens.polymer(1, 2, 3, 4));
        molecules.put(GTCEu.id("silicone_rubber"), sir);
        molecules.put(GTCEu.id("polydimethylsiloxane"), sir);
        molecules.put(GTCEu.id("cumene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .invAtom(2, -1)
                .invAtom(3, -1)
                .invAtom(2, -2)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE)
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8));
        molecules.put(GTCEu.id("tetrafluoroethylene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.F, -1, 1)
                .atom(Element.F, 0, -1)
                .atom(Element.F, 2, -1)
                .atom(Element.F, 1, 1)
                .bond(0, 1, Bond.Type.DOUBLE_CENTERED)
                .bond(0, 2)
                .bond(0, 3)
                .bond(1, 4)
                .bond(1, 5));
        molecules.put(GTCEu.id("vinyl_acetate"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O, 1, 1)
                .invAtom(2, 1)
                .atom(Element.O, 3, 0)
                .invAtom(2, 2)
                .bond(0, 1, Bond.Type.DOUBLE)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE_CENTERED)
                .bond(3, 5));
        molecules.put(GTCEu.id("dimethylhydrazine"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .atom(Element.N, 1, 0)
                .atom(Element.H, 1.5f, -0.5f)
                .atom(Element.N, 1, 1)
                .atom(Element.H, 0.5f, 1.5f)
                .invAtom(2, 1)
                .bond(0, 1)
                .bond(1, 2)
                .bond(1, 3)
                .bond(3, 4)
                .bond(3, 5));
        molecules.put(GTCEu.id("dimethyldichlorosilane"), new Molecule()
                .uv()
                .atom(Element.Si, 0, 0)
                .invAtom(1, 0)
                .invAtom(0, 1)
                .atom(Element.Cl, -1, 0)
                .atom(Element.Cl, 0, -1)
                .bond(0, 1)
                .bond(0, 2)
                .bond(0, 3)
                .bond(0, 4));
        molecules.put(GTCEu.id("styrene"), new Molecule()
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
                .bond(6, 7, Bond.Type.DOUBLE));
        molecules.put(GTCEu.id("dichlorobenzene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .atom(Element.Cl, 2, -1)
                .atom(Element.Cl, -2, 3)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE)
                .bond(5, 6)
                .bond(2, 7));
        molecules.put(GTCEu.id("bisphenol_a"), new Molecule()
                .uv()
                .atom(Element.create("HO"), 0, 0) // 0
                .invAtom(1, 0) // 1
                .invAtom(1, 1) // 2
                .invAtom(2, 1) // 3
                .invAtom(3, 0) // 4
                .invAtom(3, -1) // 5
                .invAtom(2, -1) // 6
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 6, Bond.Type.DOUBLE)
                .bond(6, 1)
                .invAtom(4, 0) // 7
                .invAtom(5, 0) // 8
                .invAtom(4, -1) // 9
                .bond(4, 7)
                .bond(7, 8)
                .bond(7, 9)
                .invAtom(4, 1) // 10
                .invAtom(3, 2) // 11
                .invAtom(3, 3) // 12
                .invAtom(4, 3) // 13
                .invAtom(5, 2) // 14
                .invAtom(5, 1) // 15
                .atom(Element.create("OH"), 4, 4) // 16
                .bond(7, 10)
                .bond(10, 11)
                .bond(11, 12, Bond.Type.DOUBLE)
                .bond(12, 13)
                .bond(13, 14, Bond.Type.DOUBLE)
                .bond(14, 15)
                .bond(15, 10, Bond.Type.DOUBLE)
                .bond(13, 16));
        molecules.put(GTCEu.id("vinyl_chloride"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(0, 1)
                .atom(Element.Cl, -1, 2)
                .bond(0, 1, Bond.Type.DOUBLE)
                .bond(1, 2));
        molecules.put(GTCEu.id("diphenyl_isophthalate"), new Molecule()
                .uv()
                .invAtom(0, 0) // 0
                .invAtom(-1, 1) // 1
                .invAtom(-1, 2) // 2
                .invAtom(0, 2) // 3
                .invAtom(1, 1) // 4
                .invAtom(1, 0) // 5
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE)
                .atom(Element.O, 2, 1) // 6
                .invAtom(2, 2) // 7
                .atom(Element.O, 1, 3) // 8
                .bond(4, 6)
                .bond(6, 7)
                .bond(7, 8)
                .invAtom(3, 2) // 9
                .invAtom(3, 3) // 10
                .invAtom(4, 3) // 11
                .invAtom(5, 2) // 12
                .invAtom(5, 1) // 13
                .invAtom(4, 1) // 14
                .bond(7, 9)
                .bond(9, 10, Bond.Type.DOUBLE)
                .bond(10, 11)
                .bond(11, 12, Bond.Type.DOUBLE)
                .bond(12, 13)
                .bond(13, 14, Bond.Type.DOUBLE)
                .bond(14, 9)
                .invAtom(4, 4) // 15
                .atom(Element.O, 3, 5) // 16
                .atom(Element.O, 5, 4) // 17
                .bond(11, 15)
                .bond(15, 16)
                .bond(15, 17)
                .invAtom(5, 5) // 18
                .invAtom(4, 6) // 19
                .invAtom(4, 7) // 20
                .invAtom(5, 7) // 21
                .invAtom(6, 6) // 22
                .invAtom(6, 5) // 23
                .bond(17, 18)
                .bond(18, 19)
                .bond(19, 20, Bond.Type.DOUBLE)
                .bond(20, 21)
                .bond(21, 22, Bond.Type.DOUBLE)
                .bond(22, 23)
                .bond(23, 18, Bond.Type.DOUBLE));
        molecules.put(GTCEu.id("phthalic_acid"), new Molecule()
                .uv()
                .invAtom(0, 0) // 0
                .invAtom(-1, 1) // 1
                .invAtom(-1, 2) // 2
                .invAtom(0, 2) // 3
                .invAtom(1, 1) // 4
                .invAtom(1, 0) // 5
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE)
                .invAtom(2, -1) // 6
                .atom(Element.create("HO"), 2, -2) // 7
                .atom(Element.O, 3, -1) // 8
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8, Bond.Type.DOUBLE_CENTERED)
                .invAtom(2, 1) // 9
                .atom(Element.create("OH"), 3, 0) // 10
                .atom(Element.O, 2, 2) // 11
                .bond(4, 9)
                .bond(9, 10)
                .bond(9, 11, Bond.Type.DOUBLE_CENTERED));
        molecules.put(GTCEu.id("diaminobenzidine"), new Molecule()
                .uv()
                .invAtom(0, 0) // 0
                .invAtom(-1, 1) // 1
                .invAtom(-1, 2) // 2
                .invAtom(0, 2) // 3
                .invAtom(1, 1) // 4
                .invAtom(1, 0) // 5
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE)
                .atom(Element.create("H₂N"), -2, 1) // 6
                .atom(Element.create("NH₂"), -2, 3) // 7
                .bond(1, 6)
                .bond(2, 7)
                .invAtom(2, 1) // 8
                .invAtom(2, 2) // 9
                .invAtom(3, 2) // 10
                .invAtom(4, 1) // 11
                .invAtom(4, 0) // 12
                .invAtom(3, 0) // 13
                .bond(4, 8)
                .bond(8, 9, Bond.Type.DOUBLE)
                .bond(9, 10)
                .bond(10, 11, Bond.Type.DOUBLE)
                .bond(11, 12)
                .bond(12, 13, Bond.Type.DOUBLE)
                .bond(13, 8)
                .atom(Element.create("NH₂"), 3, 3) // 14
                .atom(Element.create("NH₂"), 5, 1) // 15
                .bond(10, 14)
                .bond(11, 15));
        molecules.put(GTCEu.id("dichlorobenzidine"), new Molecule()
                .uv()
                .invAtom(0, 0) // 0
                .invAtom(-1, 1) // 1
                .invAtom(-1, 2) // 2
                .invAtom(0, 2) // 3
                .invAtom(1, 1) // 4
                .invAtom(1, 0) // 5
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE)
                .atom(Element.create("H₂N"), -2, 1) // 6
                .atom(Element.Cl, 0, -1) // 7
                .bond(1, 6)
                .bond(0, 7)
                .invAtom(2, 1) // 8
                .invAtom(2, 2) // 9
                .invAtom(3, 2) // 10
                .invAtom(4, 1) // 11
                .invAtom(4, 0) // 12
                .invAtom(3, 0) // 13
                .bond(4, 8)
                .bond(8, 9, Bond.Type.DOUBLE)
                .bond(9, 10)
                .bond(10, 11, Bond.Type.DOUBLE)
                .bond(11, 12)
                .bond(12, 13, Bond.Type.DOUBLE)
                .bond(13, 8)
                .atom(Element.Cl, 3, 3) // 14
                .atom(Element.create("NH₂"), 5, 1) // 15
                .bond(10, 14)
                .bond(11, 15));
        molecules.put(GTCEu.id("nitrochlorobenzene"), new Molecule()
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
                .bond(5, 0, Bond.Type.DOUBLE)
                .atom(Element.N, 2, -1)
                .atom(Element.O, 2, -2)
                .atom(Element.O, 3, -1)
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8)
                .atom(Element.Cl, -2, 3)
                .bond(2, 9));
        molecules.put(GTCEu.id("chlorobenzene"), new Molecule()
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
                .bond(5, 0, Bond.Type.DOUBLE)
                .atom(Element.Cl, 2, -1)
                .bond(5, 6));
        molecules.put(GTCEu.id("octane"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .invAtom(2, 2)
                .invAtom(3, 2)
                .invAtom(3, 3)
                .invAtom(4, 3)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4)
                .bond(4, 5)
                .bond(5, 6)
                .bond(6, 7));
        molecules.put(GTCEu.id("ethyl_tertbutyl_ether"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(0, 1)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .atom(Element.O, 1, 1)
                .invAtom(1, 2)
                .invAtom(2, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(1, 3)
                .bond(1, 4)
                .bond(4, 5)
                .bond(5, 6));
        molecules.put(GTCEu.id("cyclohexanone_oxime"), new Molecule()
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
                .bond(5, 0)
                .atom(Element.N, 2, -1)
                .atom(Element.create("HO"), 2, -2)
                .bond(5, 6, Bond.Type.DOUBLE)
                .bond(6, 7));
        molecules.put(GTCEu.id("butyraldehyde"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .atom(Element.O, 2, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(4, 3, Bond.Type.DOUBLE));
        molecules.put(GTCEu.id("acetic_anhydride"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O, 2, -1)
                .atom(Element.O, 1, 1)
                .invAtom(2, 1)
                .atom(Element.O, 3, 0)
                .invAtom(2, 2)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE_CENTERED)
                .bond(1, 3)
                .bond(3, 4)
                .bond(4, 5, Bond.Type.DOUBLE_CENTERED)
                .bond(4, 6));
        molecules.put(GTCEu.id("aminophenol"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .atom(Element.create("OH"), 2, -1)
                .atom(Element.create("NH₂"), -2, 3)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.Type.DOUBLE)
                .bond(5, 6)
                .bond(2, 7));
        molecules.put(GTCEu.id("butanol"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .atom(Element.create("OH"), 2, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4));
        molecules.put(GTCEu.id("tributyl_phosphate"), new Molecule()
                .uv()
                .invAtom(0, 0) // 0
                .invAtom(1, 0) // 1
                .invAtom(1, 1) // 2
                .invAtom(2, 1) // 3
                .atom(Element.O, 2, 2) // 4
                .atom(Element.P, 3, 2) // 5
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4)
                .bond(4, 5)
                .atom(Element.O, 3, 3) // 6
                .invAtom(4, 3) // 7
                .invAtom(4, 4) // 8
                .invAtom(5, 4) // 9
                .invAtom(5, 5) // 10
                .bond(5, 6)
                .bond(6, 7)
                .bond(7, 8)
                .bond(8, 9)
                .bond(9, 10)
                .atom(Element.O, 2, 3) // 11
                .invAtom(1, 3) // 12
                .invAtom(1, 2) // 13
                .invAtom(0, 2) // 14
                .invAtom(0, 1) // 15
                .bond(5, 11)
                .bond(11, 12)
                .bond(12, 13)
                .bond(13, 14)
                .bond(14, 15)
                .atom(Element.O, 4, 1) // 16
                .bond(5, 16, Bond.Type.DOUBLE_CENTERED));
        molecules.put(GTCEu.id("chloroethane"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.Cl, 1, 1)
                .bond(0, 1)
                .bond(1, 2));
        molecules.put(GTCEu.id("acetylene"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .bond(0, 1, Bond.Type.TRIPLE));
        molecules.put(GTCEu.id("chloroacetate"), new Molecule()
                .uv()
                .atom(Element.Cl, 0, 0)
                .invAtom(0, 1)
                .invAtom(1, 1)
                .atom(Element.O, 2, 0)
                .atom(Element.create("OH"), 1, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3, Bond.Type.DOUBLE_CENTERED)
                .bond(2, 4));
        molecules.put(GTCEu.id("cyanoacetic_acid"), new Molecule()
                .uv()
                .atom(Element.N, 0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .atom(Element.O, 3, 0)
                .atom(Element.create("OH"), 2, 2)
                .bond(0, 1, Bond.Type.TRIPLE)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE_CENTERED)
                .bond(3, 5));
        molecules.put(GTCEu.id("ethyl_cyanoacetate"), new Molecule()
                .uv()
                .atom(Element.N, 0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .atom(Element.O, 3, 0)
                .atom(Element.O, 2, 2)
                .invAtom(3, 2)
                .invAtom(3, 3)
                .bond(0, 1, Bond.Type.TRIPLE)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE_CENTERED)
                .bond(3, 5)
                .bond(5, 6)
                .bond(6, 7));
        molecules.put(GTCEu.id("ethyl_cyanoacrylate"), new Molecule()
                .uv()
                .atom(Element.N, 0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .atom(Element.O, 3, 0)
                .atom(Element.O, 2, 2)
                .invAtom(3, 2)
                .invAtom(3, 3)
                .invAtom(0, 2)
                .bond(0, 1, Bond.Type.TRIPLE)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE_CENTERED)
                .bond(3, 5)
                .bond(5, 6)
                .bond(6, 7)
                .bond(2, 8, Bond.Type.DOUBLE_CENTERED));
        molecules.put(GTCEu.id("dimethyl_sulfide"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .atom(Element.S, 1, 0)
                .invAtom(1, 1)
                .bond(0, 1)
                .bond(1, 2));
        molecules.put(GTCEu.id("dimethyl_sulfoxide"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .atom(Element.S, 1, 0)
                .invAtom(1, 1)
                .atom(Element.O, 2, -1)
                .bond(0, 1)
                .bond(1, 2)
                .bond(1, 3, Bond.Type.DOUBLE_CENTERED));
        molecules.put(GTCEu.id("ammonium_oxalate"), new Molecule()
                .uv()
                .atom(Element.N, 0, 0) // 0
                .atom(Element.create("H₄"), 1 / 3f, 1 / 3f) // 1
                .atom(Element.O, 1, 1) // 2
                .invAtom(2, 1) // 3
                .atom(Element.O, 3, 0) // 4
                .invAtom(2, 2) // 5
                .atom(Element.O, 3, 2) // 6
                .atom(Element.O, 1, 3) // 7
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE_CENTERED)
                .bond(3, 5)
                .bond(5, 6)
                .bond(5, 7, Bond.Type.DOUBLE_CENTERED)
                .add(new Parens("2", "+", 0, 1))
                .add(new Parens("", "2-", 2, 3, 4, 5, 6, 7)));
        molecules.put(GTCEu.id("phthalic_anhydride"), new Molecule()
                .xy()
                .invAtom(0, 0.5f) // 0
                .invAtom(0, -0.5f) // 1
                .invAtom(MathUtils.COS30f, 1) // 2
                .invAtom(MathUtils.COS30f, -1) // 3
                .invAtom(2 * MathUtils.COS30f, 0.5f) // 4
                .invAtom(2 * MathUtils.COS30f, -0.5f) // 5
                .invAtom(2 * MathUtils.COS30f + 0.95f, MathUtils.SIN54f) // 6
                .invAtom(2 * MathUtils.COS30f + 0.95f, -MathUtils.SIN54f) // 7
                .atom(Element.O, 2 * MathUtils.COS30f + MathUtils.COS18f * MathUtils.PHIf, 0) // 8
                .atom(Element.O, 2 * MathUtils.COS30f + 0.95f + MathUtils.SIN18f, MathUtils.SIN54f + MathUtils.COS18f) // 9
                .atom(Element.O, 2 * MathUtils.COS30f + 0.95f + MathUtils.SIN18f, -MathUtils.SIN54f - MathUtils.COS18f) // 10
                .bond(0, 1, Bond.Type.DOUBLE)
                .bond(1, 3)
                .bond(3, 5, Bond.Type.DOUBLE)
                .bond(5, 4)
                .bond(4, 2, Bond.Type.DOUBLE)
                .bond(2, 0)
                .bond(5, 7)
                .bond(7, 8)
                .bond(8, 6)
                .bond(6, 4)
                .bond(7, 10, Bond.Type.DOUBLE_CENTERED)
                .bond(6, 9, Bond.Type.DOUBLE_CENTERED));
        molecules.put(GTCEu.id("ethylanthraquinone"), new Molecule()
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
                .bond(5, 0, Bond.Type.DOUBLE)
                .invAtom(2, 2)
                .invAtom(1, 3)
                .invAtom(1, 4)
                .invAtom(2, 4)
                .invAtom(3, 3)
                .invAtom(3, 2)
                .bond(6, 7, Bond.Type.DOUBLE)
                .bond(7, 8)
                .bond(8, 9, Bond.Type.DOUBLE)
                .bond(9, 10)
                .bond(10, 11, Bond.Type.DOUBLE)
                .bond(11, 6)
                .invAtom(2, 1)
                .atom(Element.O, 3, 0)
                .bond(4, 12)
                .bond(6, 12)
                .bond(12, 13, Bond.Type.DOUBLE_CENTERED)
                .invAtom(0, 3)
                .atom(Element.O, -1, 4)
                .bond(3, 14)
                .bond(7, 14)
                .bond(14, 15, Bond.Type.DOUBLE_CENTERED)
                .invAtom(4, 3)
                .invAtom(4, 4)
                .bond(10, 16)
                .bond(16, 17));
        molecules.put(GTCEu.id("acetone_azine"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(0, 1)
                .atom(Element.N, 1, 1)
                .atom(Element.N, 1, 2)
                .invAtom(2, 2)
                .invAtom(2, 3)
                .invAtom(-1, 2)
                .invAtom(3, 1)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE_CENTERED)
                .bond(2, 3)
                .bond(3, 4, Bond.Type.DOUBLE_CENTERED)
                .bond(4, 5)
                .bond(1, 6)
                .bond(4, 7));
        molecules.put(GTCEu.id("durene"), new Molecule()
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
                .bond(5, 0, Bond.Type.DOUBLE)
                .invAtom(0, -1)
                .invAtom(-2, 1)
                .invAtom(2, 1)
                .invAtom(0, 3)
                .bond(0, 6)
                .bond(1, 7)
                .bond(3, 9)
                .bond(4, 8));
        molecules.put(GTCEu.id("pyromellitic_dianhydride"), new Molecule()
                .xy()
                .invAtom(0, 1)
                .invAtom(0, -1)
                .invAtom(MathUtils.COS30f, 0.5f)
                .invAtom(MathUtils.COS30f, -0.5f)
                .invAtom(MathUtils.COS30f + 0.95f, MathUtils.SIN54f)
                .invAtom(MathUtils.COS30f + 0.95f, -MathUtils.SIN54f)
                .atom(Element.O, MathUtils.COS30f + MathUtils.COS18f * MathUtils.PHIf, 0)
                .atom(Element.O, MathUtils.COS30f + 0.95f + MathUtils.SIN18f, MathUtils.SIN54f + MathUtils.COS18f)
                .atom(Element.O, MathUtils.COS30f + 0.95f + MathUtils.SIN18f, -MathUtils.SIN54f - MathUtils.COS18f)
                .invAtom(-MathUtils.COS30f, 0.5f)
                .invAtom(-MathUtils.COS30f, -0.5f)
                .invAtom(-MathUtils.COS30f - 0.95f, MathUtils.SIN54f)
                .invAtom(-MathUtils.COS30f - 0.95f, -MathUtils.SIN54f)
                .atom(Element.O, -MathUtils.COS30f - MathUtils.COS18f * MathUtils.PHIf, 0)
                .atom(Element.O, -MathUtils.COS30f - 0.95f - MathUtils.SIN18f, MathUtils.SIN54f + MathUtils.COS18f)
                .atom(Element.O, -MathUtils.COS30f - 0.95f - MathUtils.SIN18f, -MathUtils.SIN54f - MathUtils.COS18f)
                .bond(9, 10)
                .bond(10, 1, Bond.Type.DOUBLE)
                .bond(1, 3)
                .bond(3, 2, Bond.Type.DOUBLE)
                .bond(2, 0)
                .bond(0, 9, Bond.Type.DOUBLE)
                .bond(3, 5)
                .bond(5, 6)
                .bond(6, 4)
                .bond(4, 2)
                .bond(4, 7, Bond.Type.DOUBLE_CENTERED)
                .bond(5, 8, Bond.Type.DOUBLE_CENTERED)
                .bond(9, 11)
                .bond(11, 13)
                .bond(13, 12)
                .bond(12, 10)
                .bond(11, 14, Bond.Type.DOUBLE_CENTERED)
                .bond(12, 15, Bond.Type.DOUBLE_CENTERED));
        molecules.put(GTCEu.id("dimethylformamide"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .atom(Element.O, 1, -1)
                .atom(Element.N, 0, 1)
                .invAtom(1, 1)
                .invAtom(-1, 2)
                .bond(0, 1, Bond.Type.DOUBLE_CENTERED)
                .bond(0, 2)
                .bond(2, 3)
                .bond(2, 4));
        molecules.put(GTCEu.id("methyl_formate"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .atom(Element.O, 1, -1)
                .atom(Element.O, 0, 1)
                .invAtom(1, 1)
                .bond(0, 1, Bond.Type.DOUBLE_CENTERED)
                .bond(0, 2)
                .bond(2, 3));
        molecules.put(GTCEu.id("tetranitromethane"), new Molecule()
                .xy()
                .invAtom(0, 0)
                .atom(Element.N, 1, 0)
                .atom(Element.O, 1 + MathUtils.COS30f, 0.5f)
                .atom(Element.O, 1 + MathUtils.COS30f, -0.5f)
                .bond(0, 1)
                .bond(1, 2)
                .bond(1, 3)
                .atom(Element.N, -1, 0)
                .atom(Element.O, -1 - MathUtils.COS30f, 0.5f)
                .atom(Element.O, -1 - MathUtils.COS30f, -0.5f)
                .bond(0, 4)
                .bond(4, 5)
                .bond(4, 6)
                .atom(Element.N, 0, 1)
                .atom(Element.O, 0.5f, 1 + MathUtils.COS30f)
                .atom(Element.O, -0.5f, 1 + MathUtils.COS30f)
                .bond(0, 7)
                .bond(7, 8)
                .bond(7, 9)
                .atom(Element.N, 0, -1)
                .atom(Element.O, 0.5f, -1 - MathUtils.COS30f)
                .atom(Element.O, -0.5f, -1 - MathUtils.COS30f)
                .bond(0, 10)
                .bond(10, 11)
                .bond(10, 12));
        molecules.put(GTCEu.id("chloromethane"),
                Molecule.tetragonal(Element.C, Element.Cl, Element.H, Element.H, Element.H));
        molecules.put(GTCEu.id("chloroform"),
                Molecule.tetragonal(Element.C, Element.H, Element.Cl, Element.Cl, Element.Cl));
        molecules.put(GTCEu.id("acetone_cyanohydrin"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(0, 1)
                .invAtom(-1, 0)
                .atom(Element.create("HO"), 0, -1)
                .invAtom(1, 0)
                .atom(Element.N, 1, 1)
                .bond(0, 1)
                .bond(0, 2)
                .bond(0, 3)
                .bond(0, 4)
                .bond(4, 5, Bond.Type.TRIPLE));
        molecules.put(GTCEu.id("methyl_methacrylate"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(0, 1)
                .invAtom(-1, 2)
                .invAtom(1, 1)
                .atom(Element.O, 1, 2)
                .atom(Element.O, 2, 0)
                .invAtom(2, 2)
                .bond(0, 1)
                .bond(1, 2, Bond.Type.DOUBLE_CENTERED)
                .bond(1, 3)
                .bond(3, 4)
                .bond(3, 5, Bond.Type.DOUBLE_CENTERED)
                .bond(4, 6));
        molecules.put(GTCEu.id("polymethyl_methacrylate"), new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(0, 1)
                .invAtom(1, 1)
                .invAtom(1, 2)
                .invAtom(0, 2)
                .invAtom(2, 0)
                .atom(Element.O, 3, 0)
                .atom(Element.O, 2, -1)
                .invAtom(3, -2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(2, 4)
                .bond(2, 5)
                .bond(5, 6, Bond.Type.DOUBLE_CENTERED)
                .bond(5, 7)
                .bond(7, 8)
                .add(Parens.polymer(1, 2, 4)));
        molecules.put(GTCEu.id("caprolactam"), new Molecule()
                .xy()
                .invAtom(Mth.sin(Mth.TWO_PI * 0 / 7), Mth.cos(Mth.TWO_PI * 0 / 7))
                .atom(Element.create("NH"), Mth.sin(Mth.TWO_PI * 1 / 7), Mth.cos(Mth.TWO_PI * 1 / 7))
                .invAtom(Mth.sin(Mth.TWO_PI * 2 / 7), Mth.cos(Mth.TWO_PI * 2 / 7))
                .invAtom(Mth.sin(Mth.TWO_PI * 3 / 7), Mth.cos(Mth.TWO_PI * 3 / 7))
                .invAtom(Mth.sin(Mth.TWO_PI * 4 / 7), Mth.cos(Mth.TWO_PI * 4 / 7))
                .invAtom(Mth.sin(Mth.TWO_PI * 5 / 7), Mth.cos(Mth.TWO_PI * 5 / 7))
                .invAtom(Mth.sin(Mth.TWO_PI * 6 / 7), Mth.cos(Mth.TWO_PI * 6 / 7))
                .atom(Element.O, Mth.sin(Mth.TWO_PI * 0 / 7), Mth.cos(Mth.TWO_PI * 0 / 7) + 1)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4)
                .bond(4, 5)
                .bond(5, 6)
                .bond(6, 0)
                .bond(0, 7, Bond.Type.DOUBLE_CENTERED));
        final var sbr = new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(0, 1)
                .invAtom(1, 1)
                .invAtom(1, 2)
                .invAtom(2, 2)
                .invAtom(2, 3)
                .invAtom(3, 3)
                .invAtom(3, 4)
                .invAtom(4, 4)
                .invAtom(4, 5)
                .invAtom(5, 5)
                .invAtom(5, 6)
                .invAtom(6, 6)
                .invAtom(6, 7)
                .invAtom(7, 7)
                .invAtom(7, 8)
                .invAtom(0, 2)
                .invAtom(-1, 2)
                .invAtom(-2, 3)
                .invAtom(-2, 4)
                .invAtom(-1, 4)
                .invAtom(0, 3)
                .invAtom(2, 5)
                .invAtom(1, 5)
                .invAtom(0, 6)
                .invAtom(0, 7)
                .invAtom(1, 7)
                .invAtom(2, 6)
                .invAtom(4, 6)
                .invAtom(3, 6)
                .invAtom(2, 7)
                .invAtom(2, 8)
                .invAtom(3, 8)
                .invAtom(4, 7)
                .invAtom(5, 8)
                .invAtom(4, 8)
                .invAtom(3, 9)
                .invAtom(3, 10)
                .invAtom(4, 10)
                .invAtom(5, 9)
                .invAtom(6, 4)
                .invAtom(6, 3)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4)
                .bond(4, 5, Bond.Type.DOUBLE)
                .bond(5, 6)
                .bond(6, 7)
                .bond(7, 8)
                .bond(8, 9)
                .bond(9, 10)
                .bond(10, 11)
                .bond(11, 12)
                .bond(12, 13)
                .bond(13, 14)
                .bond(14, 15)
                .bond(16, 17)
                .bond(17, 18, Bond.Type.DOUBLE)
                .bond(18, 19)
                .bond(19, 20, Bond.Type.DOUBLE)
                .bond(20, 21)
                .bond(21, 16, Bond.Type.DOUBLE)
                .bond(22, 23, Bond.Type.DOUBLE)
                .bond(23, 24)
                .bond(24, 25, Bond.Type.DOUBLE)
                .bond(25, 26)
                .bond(26, 27, Bond.Type.DOUBLE)
                .bond(27, 22)
                .bond(28, 29)
                .bond(29, 30, Bond.Type.DOUBLE)
                .bond(30, 31)
                .bond(31, 32, Bond.Type.DOUBLE)
                .bond(32, 33)
                .bond(33, 28, Bond.Type.DOUBLE)
                .bond(34, 35, Bond.Type.DOUBLE)
                .bond(35, 36)
                .bond(36, 37, Bond.Type.DOUBLE)
                .bond(37, 38)
                .bond(38, 39, Bond.Type.DOUBLE)
                .bond(39, 34)
                .bond(1, 16)
                .bond(7, 22)
                .bond(11, 28)
                .bond(13, 34)
                .bond(10, 40)
                .bond(40, 41, Bond.Type.DOUBLE)
                .add(Parens.polymer(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20, 21, 22, 23, 24,
                        25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41));
        molecules.put(GTCEu.id("styrene_butadiene_rubber"), sbr);
        molecules.put(GTCEu.id("raw_styrene_butadiene_rubber"), sbr);
        final var rubber = new Molecule()
                .uv()
                .invAtom(0, 0)
                .invAtom(0, 1)
                .invAtom(1, 1)
                .invAtom(1, 2)
                .invAtom(2, 2)
                .invAtom(2, 3)
                .invAtom(0, 3)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3, Bond.Type.DOUBLE)
                .bond(3, 4)
                .bond(3, 6)
                .bond(4, 5)
                .add(Parens.polymer(1, 2, 3, 4, 6));
        molecules.put(GTCEu.id("rubber"), rubber);
        molecules.put(GTCEu.id("raw_rubber"), rubber);
        return molecules;
    }
}
