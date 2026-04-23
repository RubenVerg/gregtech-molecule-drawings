package com.rubenverg.moldraw.data;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;

import org.joml.Matrix2f;

import com.rubenverg.moldraw.molecule.*;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IOreMaterial;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialMisc;
import gtnhlanth.common.register.WerkstoffMaterialPool;

@SuppressWarnings("removal")
public class Molecules {

    public static Map<IOreMaterial, Molecule> molecules() {
        final Map<IOreMaterial, Molecule> molecules = new HashMap<>();
        molecules.put(Materials.Methane, Molecule.tetragonal(Element.C, Element.H, Element.H, Element.H, Element.H));
        molecules.put(
            Materials.Ethane,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .bond(0, 1));
        molecules.put(
            Materials.Ethylene,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .bond(0, 1, Bond.DOUBLE));
        molecules.put(
            Materials.Propane,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .bond(0, 1)
                .bond(1, 2));
        molecules.put(
            Materials.Propene,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .bond(0, 1)
                .bond(1, 2, Bond.DOUBLE));
        molecules.put(
            Materials.Butene,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .bond(0, 1)
                .bond(1, 2, Bond.DOUBLE)
                .bond(2, 3));
        molecules.put(
            Materials.Butadiene,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .bond(0, 1, Bond.DOUBLE)
                .bond(1, 2)
                .bond(2, 3, Bond.DOUBLE));
        molecules.put(
            Materials.Polyethylene,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .add(Parens.polymer(1, 2)));
        molecules.put(
            Materials.PolyvinylChloride,
            new Molecule().uv()
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
        molecules.put(
            Materials.Polytetrafluoroethylene,
            new Molecule().uv()
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
        molecules.put(
            Materials.Polybenzimidazole,
            new Molecule().xy()
                .invAtom(0, 0) // 0
                .invAtom(1, 0) // 1
                .bond(0, 1)
                .atom(Element.N.one(), Element.H.one(), null, null, null, 1 + MathUtils.COS54f, MathUtils.SIN54f) // 2
                .skipAnAtom() // 3
                .atom(Element.N, 1 + MathUtils.COS54f, -MathUtils.SIN54f) // 4
                .ring(
                    1 + MathUtils.COS18f * MathUtils.PHIf,
                    -0.5f,
                    1 + MathUtils.COS18f * MathUtils.PHIf + MathUtils.COS30f,
                    -1) // 5
                .bond(1, 2)
                .bond(1, 4, Bond.DOUBLE)
                .bond(2, 10)
                .bond(4, 5)
                .ring(
                    1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1,
                    0.5f,
                    1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1,
                    -0.5f) // 11
                .bond(8, 11)
                .atom(
                    Element.N.one(),
                    Element.H.one(),
                    null,
                    null,
                    null,
                    1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 + 2 * MathUtils.COS30f + 0.95f,
                    MathUtils.SIN54f) // 17
                .skipAnAtom() // 18
                .atom(
                    Element.N,
                    1 + MathUtils.COS18f * MathUtils.PHIf + 2 * MathUtils.COS30f + 1 + 2 * MathUtils.COS30f + 0.95f,
                    -MathUtils.SIN54f) // 19
                .invAtom(
                    1 + MathUtils.COS18f * MathUtils.PHIf
                        + 2 * MathUtils.COS30f
                        + 1
                        + 2 * MathUtils.COS30f
                        + MathUtils.COS18f * MathUtils.PHIf,
                    0) // 20
                .bond(15, 17)
                .bond(14, 19)
                .bond(17, 20)
                .bond(19, 20, Bond.DOUBLE)
                .ring(
                    1 + MathUtils.COS18f * MathUtils.PHIf
                        + 2 * MathUtils.COS30f
                        + 1
                        + 2 * MathUtils.COS30f
                        + MathUtils.COS18f * MathUtils.PHIf
                        + 1,
                    0,
                    1 + MathUtils.COS18f * MathUtils.PHIf
                        + 2 * MathUtils.COS30f
                        + 1
                        + 2 * MathUtils.COS30f
                        + MathUtils.COS18f * MathUtils.PHIf
                        + 1,
                    -1) // 21
                .bond(20, 21)
                .invAtom(
                    1 + MathUtils.COS18f * MathUtils.PHIf
                        + 2 * MathUtils.COS30f
                        + 1
                        + 2 * MathUtils.COS30f
                        + MathUtils.COS18f * MathUtils.PHIf
                        + 1
                        + 2 * MathUtils.COS30f
                        + 1,
                    0) // 27
                .bond(25, 27)
                .add(
                    Parens.polymer(
                        1,
                        2,
                        3,
                        4,
                        5,
                        6,
                        7,
                        8,
                        9,
                        10,
                        11,
                        12,
                        13,
                        14,
                        15,
                        16,
                        17,
                        18,
                        19,
                        20,
                        21,
                        22,
                        23,
                        24,
                        25,
                        26)));
        molecules.put(
            Materials.Benzene,
            new Molecule().uv()
                .ring(0, 0, -1, 1));
        molecules.put(
            Materials.Toluene,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .invAtom(2, -1)
                .bond(5, 6));
        molecules.put(
            Materials.PolyvinylAcetate,
            new Molecule().uv()
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
                .bond(5, 7, true, Bond.DOUBLE)
                .add(Parens.polymer(1, 2)));
        molecules.put(
            Materials.PolyphenyleneSulfide,
            new Molecule().uv()
                .invAtom(0, 0) // 0
                .ring(2, -1, 1, 0) // 1
                .bond(0, 2)
                .atom(Element.S, 4, 0) // 7
                .invAtom(5, 0) // 8
                .bond(5, 7)
                .bond(7, 8)
                .add(Parens.polymer(1, 2, 3, 4, 5, 6, 7)));
        molecules.put(
            Materials.Polycaprolactam,
            new Molecule().uv()
                .invAtom(0, 0) // 0
                .atom(Element.N.one(), Element.H.one(), null, null, null, 1, 0) // 1
                .skipAnAtom() // 2
                .invAtom(1, 1) // 3
                .invAtom(2, 1) // 4
                .invAtom(2, 2) // 5
                .invAtom(3, 2) // 6
                .invAtom(3, 3) // 7
                .invAtom(4, 3) // 8
                .atom(Element.O, 5, 2) // 9
                .invAtom(4, 4) // 10
                .bond(0, 1)
                .bond(1, 3)
                .bond(3, 4)
                .bond(4, 5)
                .bond(5, 6)
                .bond(6, 7)
                .bond(7, 8)
                .bond(8, 9, true, Bond.DOUBLE)
                .bond(8, 10)
                .add(Parens.polymer(1, 2, 3, 4, 5, 6, 7, 8, 9)));
        molecules.put(
            Materials.Dimethylbenzene,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .invAtom(2, -1)
                .invAtom(2, 1)
                .bond(5, 6)
                .bond(4, 7));
        molecules.put(
            Materials.IIIDimethylbenzene,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .invAtom(2, -1)
                .invAtom(0, 3)
                .bond(5, 6)
                .bond(3, 7));
        molecules.put(
            Materials.IVDimethylbenzene,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .invAtom(2, -1)
                .invAtom(-2, 3)
                .bond(5, 6)
                .bond(2, 7));
        molecules.put(
            Materials.Methanol,
            new Molecule().uv()
                .invAtom(0, 0)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 1, 0)
                .bond(0, 1));
        molecules.put(
            Materials.Acetone,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O, 2, -1)
                .invAtom(1, 1)
                .bond(0, 1)
                .bond(1, 2, true, Bond.DOUBLE)
                .bond(1, 3));
        molecules.put(
            Materials.MethylAcetate,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O, 2, -1)
                .atom(Element.O, 1, 1)
                .invAtom(2, 1)
                .bond(0, 1)
                .bond(1, 2, true, Bond.DOUBLE)
                .bond(1, 3)
                .bond(3, 4));
        molecules.put(
            Materials.Ethanol,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 1, 1)
                .bond(0, 1)
                .bond(1, 2));
        molecules.put(
            Materials.AceticAcid,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O, 2, -1)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 1, 1)
                .bond(0, 1)
                .bond(1, 2, true, Bond.DOUBLE)
                .bond(1, 3));
        molecules.put(
            Materials.Phenol,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 2, -1)
                .bond(5, 6));
        molecules.put(
            Materials.Glycerol,
            new Molecule().uv()
                .atom(Element.O.one(), null, null, null, Element.H.one(), 0, 0)
                .invAtom(0, 1)
                .invAtom(1, 1)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 2, 0)
                .invAtom(1, 2)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 2, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(2, 4)
                .bond(4, 5));
        molecules.put(
            Materials.Epichlorohydrin,
            new Molecule().uv()
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
        molecules.put(
            Materials.AllylChloride,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .atom(Element.Cl, 2, 1)
                .bond(0, 1, Bond.DOUBLE)
                .bond(1, 2)
                .bond(2, 3));
        molecules.put(
            WerkstoffLoader.FormicAcid.getBridgeMaterial(),
            new Molecule().uv()
                .atom(Element.O, 0, 0)
                .invAtom(-1, 1)
                .atom(Element.O.one(), null, Element.H.one(), null, null, -1, 2)
                .bond(0, 1, true, Bond.DOUBLE)
                .bond(1, 2));
        molecules.put(
            Materials.Sugar,
            new Molecule().uv()
                .atom(Element.O.one(), null, null, null, Element.H.one(), 0, 0) // 0
                .invAtom(0, 1) // 1
                .invAtom(1, 1) // 2
                .atom(Element.O.one(), null, Element.H.one(), null, null, 2, 0) // 3
                .invAtom(1, 2) // 4
                .atom(Element.O.one(), null, Element.H.one(), null, null, 0, 3) // 5
                .invAtom(2, 2) // 6
                .atom(Element.O.one(), null, Element.H.one(), null, null, 3, 1) // 7
                .invAtom(2, 3) // 8
                .atom(Element.O.one(), null, Element.H.one(), null, null, 1, 4) // 9
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
                .bond(10, 11, Bond.DOUBLE));
        molecules.put(
            Materials.Isoprene,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(2, -1)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .bond(0, 1, Bond.DOUBLE)
                .bond(1, 2)
                .bond(1, 3)
                .bond(3, 4, Bond.DOUBLE));
        molecules.put(
            Materials.Glyceryl,
            new Molecule().uv()
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
        final var sir = new Molecule().uv()
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
        molecules.put(Materials.RubberSilicone, sir);
        molecules.put(Materials.Polydimethylsiloxane, sir);
        molecules.put(
            Materials.Cumene,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .invAtom(2, -1)
                .invAtom(3, -1)
                .invAtom(2, -2)
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8));
        molecules.put(
            Materials.Tetrafluoroethylene,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.F, -1, 1)
                .atom(Element.F, 0, -1)
                .atom(Element.F, 2, -1)
                .atom(Element.F, 1, 1)
                .bond(0, 1, true, Bond.DOUBLE)
                .bond(0, 2)
                .bond(0, 3)
                .bond(1, 4)
                .bond(1, 5));
        molecules.put(
            Materials.VinylAcetate,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O, 1, 1)
                .invAtom(2, 1)
                .atom(Element.O, 3, 0)
                .invAtom(2, 2)
                .bond(0, 1, Bond.DOUBLE)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4, true, Bond.DOUBLE)
                .bond(3, 5));
        molecules.put(
            Materials.Dimethylhydrazine,
            new Molecule().uv()
                .atom(Element.N, 0, 0)
                .invAtom(1, 0)
                .invAtom(0, -1)
                .atom(Element.N.one(), null, Element.H.count(2), null, null, -1, 1)
                .bond(0, 1)
                .bond(0, 2)
                .bond(0, 3));
        molecules.put(
            Materials.Dimethyldichlorosilane,
            new Molecule().uv()
                .atom(Element.Si, 0, 0)
                .invAtom(1, 0)
                .invAtom(0, 1)
                .atom(Element.Cl, -1, 0)
                .atom(Element.Cl, 0, -1)
                .bond(0, 1)
                .bond(0, 2)
                .bond(0, 3)
                .bond(0, 4));
        molecules.put(
            Materials.Styrene,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .invAtom(2, 1)
                .invAtom(2, 2)
                .bond(4, 6)
                .bond(6, 7, Bond.DOUBLE));
        molecules.put(
            Materials.Dichlorobenzene,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .atom(Element.Cl, 2, -1)
                .atom(Element.Cl, -2, 3)
                .bond(5, 6)
                .bond(2, 7));
        molecules.put(
            Materials.BisphenolA,
            new Molecule().uv()
                .atom(Element.O.one(), null, null, null, Element.H.one(), 0, 0) // 0
                .ring(2, -1, 1, 0) // 1
                .bond(0, 2)
                .invAtom(4, 0) // 7
                .invAtom(5, 0) // 8
                .invAtom(4, -1) // 9
                .bond(5, 7)
                .bond(7, 8)
                .bond(7, 9)
                .ring(4, 1, 3, 2) // 10
                .atom(Element.O.one(), null, Element.H.one(), null, null, 4, 4) // 16
                .bond(7, 10)
                .bond(13, 16));
        molecules.put(
            Materials.VinylChloride,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(0, 1)
                .atom(Element.Cl, -1, 2)
                .bond(0, 1, Bond.DOUBLE)
                .bond(1, 2));
        molecules.put(
            Materials.Diphenylisophthalate,
            new Molecule().uv()
                .ring(0, 0, -1, 1) // 0
                .atom(Element.O, 2, 1) // 6
                .invAtom(2, 2) // 7
                .atom(Element.O, 1, 3) // 8
                .bond(4, 6)
                .bond(6, 7)
                .bond(7, 8)
                .ring(4, 1, 3, 2) // 9
                .bond(7, 10)
                .invAtom(4, 4) // 15
                .atom(Element.O, 3, 5) // 16
                .atom(Element.O, 5, 4) // 17
                .bond(12, 15)
                .bond(15, 16)
                .bond(15, 17)
                .ring(5, 5, 4, 6) // 18
                .bond(17, 18));
        molecules.put(
            Materials.PhthalicAcid,
            new Molecule().uv()
                .ring(0, 0, -1, 1) // 0
                .invAtom(2, -1) // 6
                .atom(Element.O.one(), null, null, null, Element.H.one(), 2, -2) // 7
                .atom(Element.O, 3, -1) // 8
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8, true, Bond.DOUBLE)
                .invAtom(2, 1) // 9
                .atom(Element.O.one(), null, Element.H.one(), null, null, 3, 0) // 10
                .atom(Element.O, 2, 2) // 11
                .bond(4, 9)
                .bond(9, 10)
                .bond(9, 11, true, Bond.DOUBLE));
        molecules.put(
            Materials.Diaminobenzidin,
            new Molecule().uv()
                .ring(0, 0, -1, 1) // 0
                .atom(Element.N.one(), null, null, null, Element.H.count(2), -2, 1) // 6
                .atom(Element.N.one(), null, Element.H.count(2), null, null, -2, 3) // 7
                .bond(1, 6)
                .bond(2, 7)
                .ring(3, 0, 2, 1) // 8
                .bond(4, 9)
                .atom(Element.N.one(), null, Element.H.count(2), null, null, 3, 3) // 14
                .atom(Element.N.one(), null, Element.H.count(2), null, null, 5, 1) // 15
                .bond(11, 14)
                .bond(12, 15));
        molecules.put(
            Materials.Dichlorobenzidine,
            new Molecule().uv()
                .ring(0, 0, -1, 1) // 0
                .atom(Element.N.one(), null, null, null, Element.H.count(2), -2, 1) // 6
                .atom(Element.Cl, 0, -1) // 7
                .bond(1, 6)
                .bond(0, 7)
                .ring(3, 0, 2, 1) // 8
                .bond(4, 9)
                .atom(Element.Cl, 3, 3) // 14
                .atom(Element.N.one(), null, Element.H.count(2), null, null, 5, 1) // 15
                .bond(11, 14)
                .bond(12, 15));
        molecules.put(
            Materials.Nitrochlorobenzene,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .atom(Element.N, 2, -1)
                .atom(Element.O, 2, -2)
                .atom(Element.O, 3, -1)
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8)
                .atom(Element.Cl, -2, 3)
                .bond(2, 9));
        molecules.put(
            Materials.Chlorobenzene,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .atom(Element.Cl, 2, -1)
                .bond(5, 6));
        molecules.put(
            Materials.Octane,
            new Molecule().uv()
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
        molecules.put(
            Materials.AntiKnock,
            new Molecule().uv()
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
        molecules.put(
            Materials.Butyraldehyde,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .atom(Element.O, 2, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(4, 3, Bond.DOUBLE));
        molecules.put(
            MaterialMisc.ACETIC_ANHYDRIDE,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O, 2, -1)
                .atom(Element.O, 1, 1)
                .invAtom(2, 1)
                .atom(Element.O, 3, 0)
                .invAtom(2, 2)
                .bond(0, 1)
                .bond(1, 2, true, Bond.DOUBLE)
                .bond(1, 3)
                .bond(3, 4)
                .bond(4, 5, true, Bond.DOUBLE)
                .bond(4, 6));
        molecules.put(
            Materials.Acetylene,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .bond(0, 1, Bond.TRIPLE));
        molecules.put(
            MaterialMisc.CYANOACETIC_ACID,
            new Molecule().uv()
                .atom(Element.N, 0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .atom(Element.O, 3, 0)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 2, 2)
                .bond(0, 1, Bond.TRIPLE)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4, true, Bond.DOUBLE)
                .bond(3, 5));
        molecules.put(
            MaterialMisc.ETHYL_CYANOACETATE,
            new Molecule().uv()
                .atom(Element.N, 0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .atom(Element.O, 3, 0)
                .atom(Element.O, 2, 2)
                .invAtom(3, 2)
                .invAtom(3, 3)
                .bond(0, 1, Bond.TRIPLE)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4, true, Bond.DOUBLE)
                .bond(3, 5)
                .bond(5, 6)
                .bond(6, 7));
        molecules.put(
            MaterialMisc.ETHYL_CYANOACRYLATE,
            new Molecule().uv()
                .atom(Element.N, 0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .atom(Element.O, 3, 0)
                .atom(Element.O, 2, 2)
                .invAtom(3, 2)
                .invAtom(3, 3)
                .invAtom(0, 2)
                .bond(0, 1, Bond.TRIPLE)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4, true, Bond.DOUBLE)
                .bond(3, 5)
                .bond(5, 6)
                .bond(6, 7)
                .bond(2, 8, true, Bond.DOUBLE));
        /*
         * molecules.put(phthalic anhydride, new Molecule()
         * .xy()
         * .ring(0, 0.5f, 0, -0.5f) // 0
         * .invAtom(2 * MathUtils.COS30f + 0.95f, MathUtils.SIN54f) // 6
         * .invAtom(2 * MathUtils.COS30f + 0.95f, -MathUtils.SIN54f) // 7
         * .atom(Element.O, 2 * MathUtils.COS30f + MathUtils.COS18f * MathUtils.PHIf, 0) // 8
         * .atom(Element.O, 2 * MathUtils.COS30f + 0.95f + MathUtils.SIN18f, MathUtils.SIN54f + MathUtils.COS18f) // 9
         * .atom(Element.O, 2 * MathUtils.COS30f + 0.95f + MathUtils.SIN18f, -MathUtils.SIN54f - MathUtils.COS18f) // 10
         * .bond(3, 7)
         * .bond(7, 8)
         * .bond(8, 6)
         * .bond(6, 4)
         * .bond(7, 10, true, Bond.DOUBLE)
         * .bond(6, 9, true, Bond.DOUBLE));
         */
        molecules.put(
            Materials.Tetranitromethane,
            new Molecule().xy()
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
        molecules
            .put(Materials.Chloromethane, Molecule.tetragonal(Element.C, Element.Cl, Element.H, Element.H, Element.H));
        molecules
            .put(Materials.Chloroform, Molecule.tetragonal(Element.C, Element.H, Element.Cl, Element.Cl, Element.Cl));
        final var sbr = new Molecule().uv()
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
            .ring(-1, 2, -2, 3)
            .ring(1, 5, 0, 6)
            .ring(3, 6, 2, 7)
            .ring(4, 8, 3, 9)
            .invAtom(6, 4)
            .invAtom(6, 3)
            .bond(0, 1)
            .bond(1, 2)
            .bond(2, 3)
            .bond(3, 4)
            .bond(4, 5, Bond.DOUBLE)
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
            .bond(1, 21)
            .bond(7, 27)
            .bond(11, 33)
            .bond(13, 39)
            .bond(10, 40)
            .bond(40, 41, Bond.DOUBLE)
            .add(
                Parens.polymer(
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8,
                    9,
                    10,
                    11,
                    12,
                    13,
                    14,
                    16,
                    17,
                    18,
                    19,
                    20,
                    21,
                    22,
                    23,
                    24,
                    25,
                    26,
                    27,
                    28,
                    29,
                    30,
                    31,
                    32,
                    33,
                    34,
                    35,
                    36,
                    37,
                    38,
                    39,
                    40,
                    41));
        molecules.put(Materials.RawStyreneButadieneRubber, sbr);
        molecules.put(Materials.StyreneButadieneRubber, sbr);
        final var rubber = new Molecule().uv()
            .invAtom(0, 0)
            .invAtom(0, 1)
            .invAtom(1, 1)
            .invAtom(1, 2)
            .invAtom(2, 2)
            .invAtom(2, 3)
            .invAtom(0, 3)
            .bond(0, 1)
            .bond(1, 2)
            .bond(2, 3, Bond.DOUBLE)
            .bond(3, 4)
            .bond(3, 6)
            .bond(4, 5)
            .add(Parens.polymer(1, 2, 3, 4, 6));
        molecules.put(Materials.Rubber, rubber);
        molecules.put(Materials.RubberRaw, rubber);
        molecules.put(
            Materials.Butane,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3));
        /*
         * molecules.put(sodium ethyl xanthate), new Molecule()
         * .uv()
         * .invAtom(0, 0)
         * .invAtom(1, 0)
         * .atom(Element.O, 1, 1)
         * .invAtom(2, 1)
         * .atom(Element.S.negIon().one(), null, Element.Na.posIon().one(), null, null, 2, 2)
         * .atom(Element.S, 3, 0)
         * .bond(0, 1)
         * .bond(1, 2)
         * .bond(2, 3)
         * .bond(3, 4)
         * .bond(3, 5, true, Bond.DOUBLE));
         * molecules.put(potassium ethyl xanthate, new Molecule()
         * .uv()
         * .invAtom(0, 0)
         * .invAtom(1, 0)
         * .atom(Element.O, 1, 1)
         * .invAtom(2, 1)
         * .atom(Element.S.negIon().one(), null, Element.K.posIon().one(), null, null, 2, 2)
         * .atom(Element.S, 3, 0)
         * .bond(0, 1)
         * .bond(1, 2)
         * .bond(2, 3)
         * .bond(3, 4)
         * .bond(3, 5, true, Bond.DOUBLE));
         */
        molecules.put(
            Materials.TerephthalicAcid,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .invAtom(2, -1)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 3, -1)
                .atom(Element.O, 2, -2)
                .invAtom(-2, 3)
                .atom(Element.O.one(), null, null, null, Element.H.one(), -3, 3)
                .atom(Element.O, -2, 4)
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8, true, Bond.DOUBLE)
                .bond(2, 9)
                .bond(9, 10)
                .bond(9, 11, true, Bond.DOUBLE));
        molecules.put(
            Materials.TerephthaloylChloride,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .invAtom(2, -1)
                .atom(Element.Cl, 3, -1)
                .atom(Element.O, 2, -2)
                .invAtom(-2, 3)
                .atom(Element.Cl, -2, 4)
                .atom(Element.O, -3, 3)
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8, true, Bond.DOUBLE)
                .bond(2, 9)
                .bond(9, 10)
                .bond(9, 11, true, Bond.DOUBLE));
        molecules.put(
            Materials.IVNitroaniline,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .atom(Element.N, 2, -1)
                .atom(Element.O, 2, -2)
                .atom(Element.O, 3, -1)
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8)
                .atom(Element.N.one(), null, Element.H.count(2), null, null, -2, 3)
                .bond(2, 9));
        molecules.put(
            Materials.ParaPhenylenediamine,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .atom(Element.N.one(), null, Element.H.count(2), null, null, 2, -1)
                .bond(5, 6)
                .atom(Element.N.one(), null, Element.H.count(2), null, null, -2, 3)
                .bond(2, 7));
        molecules.put(
            Materials.Ethyleneglycol,
            new Molecule().uv()
                .atom(Element.O.one(), null, null, null, Element.H.one(), 0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 2, 1)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3));
        molecules.put(
            Materials.EthyleneOxide,
            new Molecule().xy()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O, 0.5f, MathUtils.COS30f)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 0));
        molecules.put(
            WerkstoffMaterialPool.Butanediol.getBridgeMaterial(),
            new Molecule().uv()
                .atom(Element.O.one(), null, null, null, Element.H.one(), 0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .invAtom(2, 2)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 3, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4)
                .bond(4, 5));
        molecules.put(
            GGMaterial.ferrocene.getBridgeMaterial(),
            new Molecule().xy()
                .invAtom(0, 0.5f)
                .invAtom(0.85f, 0)
                .invAtom(1.7f, 0.5f)
                .invAtom(1.35f, 1.1f)
                .invAtom(0.35f, 1.1f)
                .bond(0, 1, Bond.Line.OUTWARD)
                .bond(2, 1, Bond.Line.OUTWARD)
                .bond(2, 3)
                .bond(3, 4)
                .bond(4, 0)
                .add(
                    new CircleTransformation(
                        new Matrix2f().identity()
                            .scale(0.5f, 0.3f),
                        0,
                        1,
                        2,
                        3,
                        4))
                .invAtom(0, 3 - 0.5f)
                .invAtom(0.85f, 3)
                .invAtom(1.7f, 3 - 0.5f)
                .invAtom(1.35f, 3 - 1.1f)
                .invAtom(0.35f, 3 - 1.1f)
                .bond(5, 6)
                .bond(6, 7)
                .bond(7, 8, Bond.Line.OUTWARD)
                .bond(8, 9, Bond.Line.THICK)
                .bond(5, 9, Bond.Line.OUTWARD)
                .add(
                    new CircleTransformation(
                        new Matrix2f().identity()
                            .scale(0.5f, 0.3f),
                        5,
                        6,
                        7,
                        8,
                        9))
                .invAtom(0.85f, (0.5f + 0 + 0.5f + 1.1f + 1.1f) / 5)
                .invAtom(0.85f, (3 * 5 - 0.5f - 0 - 0.5f - 1.1f - 1.1f) / 5)
                .atom(Element.Fe, 0.85f, 1.5f)
                .bond(10, 12, Bond.Line.DOTTED)
                .bond(11, 12, Bond.Line.DOTTED));
        molecules.put(
            Materials.Dimethylamine,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .atom(Element.N, 2, -1)
                .invAtom(2, -2)
                .invAtom(3, -1)
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8));
        molecules.put(
            Materials.TrimethylBorate,
            new Molecule().uv()
                .atom(Element.B, 0, 0)
                .atom(Element.O, 0, 1)
                .invAtom(-1, 2)
                .atom(Element.O, -1, 0)
                .invAtom(-1, -1)
                .atom(Element.O, 1, -1)
                .invAtom(2, -1)
                .bond(0, 1)
                .bond(1, 2)
                .bond(0, 3)
                .bond(3, 4)
                .bond(0, 5)
                .bond(5, 6));
        molecules.put(
            Materials.Trimethylamine,
            new Molecule().uv()
                .atom(Element.N, 0, 0)
                .invAtom(-1, 0)
                .invAtom(0, 1)
                .invAtom(1, -1)
                .bond(0, 1)
                .bond(0, 2)
                .bond(0, 3));
        molecules.put(
            GGMaterial.oxalate.getBridgeMaterial(),
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .atom(Element.O, -1, 1)
                .atom(Element.O, 2, -1)
                .atom(Element.O.one(), null, null, null, Element.H.one(), 0, -1)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 1, 1)
                .bond(0, 1)
                .bond(0, 4)
                .bond(1, 5)
                .bond(0, 2, true, Bond.DOUBLE)
                .bond(1, 3, true, Bond.DOUBLE));
        molecules.put(
            Materials.Methylamine,
            new Molecule().uv()
                .invAtom(0, 0)
                .atom(Element.N.one(), null, Element.H.count(2), null, null, 1, 0)
                .bond(0, 1));
        return molecules;
    }

    public static Map<Fluid, Molecule> fluidMolecules() {
        final Map<Fluid, Molecule> molecules = new HashMap<>();
        molecules.put(
            GTPPFluids.Ethylbenzene,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .invAtom(2, 1)
                .invAtom(2, 2)
                .bond(4, 6)
                .bond(6, 7));
        molecules.put(
            GTPPFluids.Naphthalene,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .bond(0, 1)
                .bond(1, 2, Bond.DOUBLE)
                .bond(2, 3)
                .bond(3, 4)
                .bond(4, 5)
                .bond(5, 0, Bond.DOUBLE)
                .invAtom(0, 3)
                .invAtom(1, 3)
                .invAtom(2, 2)
                .invAtom(2, 1)
                .bond(3, 6, Bond.DOUBLE)
                .bond(6, 7)
                .bond(7, 8, Bond.DOUBLE)
                .bond(8, 9)
                .bond(9, 4, Bond.DOUBLE));
        molecules.put(
            GTPPFluids.Formaldehyde,
            new Molecule().uv()
                .invAtom(0, 0)
                .atom(Element.O, 1, 0)
                .bond(0, 1, true, Bond.DOUBLE));
        molecules.put(
            GTPPFluids.Cyclohexane,
            new Molecule().uv()
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
        molecules.put(
            GTPPFluids.Nitrobenzene,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .atom(Element.N, 2, -1)
                .atom(Element.O, 2, -2)
                .atom(Element.O, 3, -1)
                .bond(5, 6)
                .bond(6, 7)
                .bond(6, 8));
        molecules.put(
            GTPPFluids.Butanol,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(1, 0)
                .invAtom(1, 1)
                .invAtom(2, 1)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 2, 2)
                .bond(0, 1)
                .bond(1, 2)
                .bond(2, 3)
                .bond(3, 4));
        molecules.put(
            GTPPFluids.Ethylanthraquinone,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .bond(0, 1)
                .bond(1, 2, Bond.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.DOUBLE)
                .invAtom(2, 2)
                .invAtom(1, 3)
                .invAtom(1, 4)
                .invAtom(2, 4)
                .invAtom(3, 3)
                .invAtom(3, 2)
                .bond(6, 7, Bond.DOUBLE)
                .bond(7, 8)
                .bond(8, 9, Bond.DOUBLE)
                .bond(9, 10)
                .bond(10, 11, Bond.DOUBLE)
                .bond(11, 6)
                .invAtom(2, 1)
                .atom(Element.O, 3, 0)
                .bond(4, 12)
                .bond(6, 12)
                .bond(12, 13, true, Bond.DOUBLE)
                .invAtom(0, 3)
                .atom(Element.O, -1, 4)
                .bond(3, 14)
                .bond(7, 14)
                .bond(14, 15, true, Bond.DOUBLE)
                .invAtom(4, 3)
                .invAtom(4, 4)
                .bond(10, 16)
                .bond(16, 17));
        molecules.put(
            GTPPFluids.Ethylanthrahydroquinone,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .bond(0, 1)
                .bond(1, 2, Bond.DOUBLE)
                .bond(2, 3)
                .bond(3, 4, Bond.DOUBLE)
                .bond(4, 5)
                .bond(5, 0, Bond.DOUBLE)
                .invAtom(2, 2)
                .invAtom(1, 3)
                .invAtom(1, 4)
                .invAtom(2, 4)
                .invAtom(3, 3)
                .invAtom(3, 2)
                .bond(6, 7)
                .bond(7, 8)
                .bond(8, 9, Bond.DOUBLE)
                .bond(9, 10)
                .bond(10, 11, Bond.DOUBLE)
                .bond(11, 6)
                .invAtom(2, 1)
                .atom(Element.O.one(), null, Element.H.one(), null, null, 3, 0)
                .bond(4, 12)
                .bond(6, 12, Bond.DOUBLE)
                .bond(12, 13)
                .invAtom(0, 3)
                .atom(Element.O.one(), null, Element.H.one(), null, null, -1, 4)
                .bond(3, 14)
                .bond(14, 7, Bond.DOUBLE)
                .bond(14, 15)
                .invAtom(4, 3)
                .invAtom(4, 4)
                .bond(10, 16)
                .bond(16, 17));
        molecules.put(
            GTPPFluids.Anthracene,
            new Molecule().uv()
                .invAtom(0, 0)
                .invAtom(-1, 1)
                .invAtom(-1, 2)
                .invAtom(0, 2)
                .invAtom(1, 1)
                .invAtom(1, 0)
                .invAtom(2, 2)
                .invAtom(1, 3)
                .invAtom(1, 4)
                .invAtom(2, 4)
                .invAtom(3, 3)
                .invAtom(3, 2)
                .invAtom(0, 3)
                .invAtom(2, 1)
                .bond(0, 1, Bond.DOUBLE)
                .bond(1, 2)
                .bond(2, 3, Bond.DOUBLE)
                .bond(3, 4)
                .bond(4, 5, Bond.DOUBLE)
                .bond(5, 0)
                .bond(3, 12)
                .bond(12, 7, Bond.DOUBLE)
                .bond(7, 6)
                .bond(6, 13, Bond.DOUBLE)
                .bond(13, 4)
                .bond(7, 8)
                .bond(8, 9, Bond.DOUBLE)
                .bond(9, 10)
                .bond(10, 11, Bond.DOUBLE)
                .bond(11, 6));
        molecules.put(
            GTPPFluids.Monomethylhydrazine,
            new Molecule().uv()
                .atom(Element.N.one(), null, null, null, Element.H.count(2), 0, 0)
                .atom(Element.N.one(), Element.H.one(), null, null, null, 1, 0)
                .invAtom(1, 1)
                .bond(0, 1)
                .bond(1, 2));
        molecules.put(
            GTPPFluids.Aniline,
            new Molecule().uv()
                .ring(0, 0, -1, 1)
                .atom(Element.N.one(), null, Element.H.count(2), null, null, 2, -1)
                .bond(5, 6));
        molecules.put(
            GTPPFluids.Cyclohexanone,
            new Molecule().uv()
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
                .atom(Element.O, 2, -1)
                .bond(5, 6, true, Bond.DOUBLE));
        return molecules;
    }
}
