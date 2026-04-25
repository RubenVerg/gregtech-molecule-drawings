package com.rubenverg.moldraw.data;

import java.util.*;

import akka.japi.Pair;
import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IOreMaterial;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;

@SuppressWarnings("removal")
public class Alloys {

    public static Map<IOreMaterial, Optional<List<Pair<IOreMaterial, Long>>>> alloys() {
        final Map<IOreMaterial, Optional<List<Pair<IOreMaterial, Long>>>> alloys = new HashMap<>();
        alloys.put(Materials.BatteryAlloy, Optional.empty());
        alloys.put(Materials.Brass, Optional.empty());
        alloys.put(Materials.Bronze, Optional.empty());
        alloys.put(Materials.Cupronickel, Optional.empty());
        alloys.put(Materials.Electrum, Optional.empty());
        alloys.put(Materials.Invar, Optional.empty());
        alloys.put(Materials.Kanthal, Optional.empty());
        alloys.put(Materials.Magnalium, Optional.empty());
        alloys.put(Materials.Nichrome, Optional.empty());
        alloys.put(Materials.NiobiumTitanium, Optional.empty());
        alloys.put(Materials.SterlingSilver, Optional.empty());
        alloys.put(Materials.RoseGold, Optional.empty());
        alloys.put(Materials.BlackBronze, Optional.empty());
        alloys.put(Materials.BismuthBronze, Optional.empty());
        alloys.put(WerkstoffLoader.Ruridit, Optional.empty());
        alloys.put(Materials.SolderingAlloy, Optional.empty());
        alloys.put(Materials.StainlessSteel, Optional.empty());
        alloys.put(Materials.TinAlloy, Optional.empty());
        alloys.put(Materials.Ultimet, Optional.empty());
        alloys.put(Materials.VanadiumGallium, Optional.empty());
        alloys.put(Materials.YttriumBariumCuprate, Optional.empty());
        alloys.put(Materials.Osmiridium, Optional.empty());
        alloys.put(Materials.GalliumArsenide, Optional.empty());
        alloys.put(Materials.IndiumGalliumPhosphide, Optional.empty());
        alloys.put(Materials.NickelZincFerrite, Optional.empty());
        alloys.put(Materials.TungstenCarbide, Optional.empty());
        alloys.put(Materials.BlackSteel, Optional.empty());
        alloys.put(Materials.TungstenSteel, Optional.empty());
        alloys.put(Materials.CobaltBrass, Optional.empty());
        alloys.put(Materials.VanadiumSteel, Optional.empty());
        alloys.put(MaterialsAlloy.POTIN, Optional.empty());
        alloys.put(Materials.NaquadahAlloy, Optional.empty());
        alloys.put(WerkstoffLoader.LuVTierMaterial, Optional.empty());
        alloys.put(Materials.RedSteel, Optional.empty());
        alloys.put(Materials.BlueSteel, Optional.empty());
        alloys.put(Materials.HSSG, Optional.empty());
        alloys.put(Materials.RedAlloy, Optional.empty());
        alloys.put(Materials.HSSE, Optional.empty());
        alloys.put(Materials.HSSS, Optional.empty());
        alloys.put(Materials.BlueAlloy, Optional.empty());
        alloys.put(MaterialsAlloy.TANTALUM_CARBIDE, Optional.empty());
        alloys.put(Materials.HSLA, Optional.empty());
        alloys.put(MaterialsAlloy.ZERON_100, Optional.empty());
        alloys.put(MaterialsAlloy.AQUATIC_STEEL, Optional.empty());
        alloys.put(MaterialsAlloy.INCOLOY_MA956, Optional.empty());
        alloys.put(MaterialsAlloy.MARAGING300, Optional.empty());
        alloys.put(MaterialsAlloy.HASTELLOY_X, Optional.empty());
        alloys.put(MaterialsAlloy.STELLITE, Optional.empty());
        alloys.put(MaterialsAlloy.TUNGSTEN_TITANIUM_CARBIDE, Optional.empty());
        alloys.put(MaterialsAlloy.HASTELLOY_C276, Optional.empty());
        alloys.put(Materials.Steel, Optional.empty());
        alloys.put(Materials.ConductiveIron, Optional.empty());
        alloys.put(MaterialsAlloy.TRINIUM_NAQUADAH, Optional.empty());
        alloys.put(Materials.EndSteel, Optional.empty());
        alloys.put(GGMaterial.lumiium, Optional.empty());
        alloys.put(Materials.Manyullyn, Optional.empty());
        alloys.put(GGMaterial.signalium, Optional.empty());
        alloys.put(Materials.EnergeticAlloy, Optional.empty());
        alloys.put(Materials.VibrantAlloy, Optional.empty());
        alloys.put(Materials.Enderium, Optional.empty());
        alloys.put(Materials.DarkSteel, Optional.empty());
        alloys.put(Materials.ElectricalSteel, Optional.empty());
        alloys.put(GGMaterial.marM200, Optional.empty());
        alloys.put(GGMaterial.artheriumSn, Optional.empty());
        alloys.put(MaterialsAlloy.HG1223, Optional.empty());
        alloys.put(GGMaterial.marCeM200, Optional.empty());
        alloys.put(MaterialsAlloy.CINOBITE, Optional.empty());
        alloys.put(GGMaterial.adamantiumAlloy, Optional.empty());
        alloys.put(MaterialsAlloy.INCONEL_792, Optional.empty());
        alloys.put(Materials.Duralumin, Optional.empty());
        alloys.put(Materials.MelodicAlloy, Optional.empty());
        alloys.put(MaterialsAlloy.PIKYONIUM, Optional.empty());
        alloys.put(MaterialsAlloy.INCOLOY_020, Optional.empty());
        alloys.put(MaterialsAlloy.INCOLOY_DS, Optional.empty());
        alloys.put(MaterialsAlloy.ZIRCONIUM_CARBIDE, Optional.empty());
        alloys.put(MaterialsAlloy.NITINOL_60, Optional.empty());
        alloys.put(MaterialsAlloy.INCONEL_690, Optional.empty());
        alloys.put(MaterialsAlloy.TANTALLOY_60, Optional.empty());
        alloys.put(MaterialsAlloy.HASTELLOY_N, Optional.empty());
        alloys.put(
            Materials.EnrichedHolmium,
            Optional.of(List.of(new Pair<>(Materials.NaquadahEnriched, 4L), new Pair<>(Materials.Holmium, 1L))));
        alloys.put(MaterialsAlloy.TRINIUM_NAQUADAH_CARBON, Optional.empty());
        alloys.put(Materials.StellarAlloy, Optional.empty());
        alloys.put(MaterialsAlloy.INCONEL_625, Optional.empty());
        alloys.put(MaterialsAlloy.SILICON_CARBIDE, Optional.empty());
        alloys.put(MaterialsAlloy.LAFIUM, Optional.empty());
        alloys.put(MaterialsAlloy.TANTALLOY_61, Optional.empty());
        alloys.put(MaterialsAlloy.EGLIN_STEEL, Optional.empty());
        alloys.put(GGMaterial.incoloy903, Optional.empty());
        alloys.put(MaterialsAlloy.TUMBAGA, Optional.empty());
        alloys.put(MaterialsAlloy.ABYSSAL, Optional.empty());
        alloys.put(
            Materials.Mellion,
            Optional.of(
                List.of(
                    new Pair<>(Materials.Tritanium, 11L),
                    new Pair<>(GGMaterial.orundum, 8L),
                    new Pair<>(Materials.Rubidium, 11L),
                    new Pair<>(Materials.FierySteel, 7L),
                    new Pair<>(Materials.Firestone, 13L),
                    new Pair<>(GGMaterial.atomicSeparationCatalyst, 13L))));
        alloys.put(Materials.SuperconductorMVBase, Optional.empty());
        alloys.put(Materials.SuperconductorHVBase, Optional.empty());
        alloys.put(Materials.SuperconductorEVBase, Optional.empty());
        alloys.put(Materials.SuperconductorIVBase, Optional.empty());
        alloys.put(Materials.SuperconductorLuVBase, Optional.empty());
        alloys.put(Materials.SuperconductorZPMBase, Optional.empty());
        alloys.put(Materials.SuperconductorUVBase, Optional.empty());
        alloys.put(Materials.SuperconductorUHVBase, Optional.empty());
        alloys.put(
            Materials.SuperconductorUEVBase,
            Optional.of(
                List.of(
                    new Pair<>(Materials.DraconiumAwakened, 5L),
                    new Pair<>(Materials.Infinity, 5L),
                    new Pair<>(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN, 1L),
                    new Pair<>(MaterialsElements.STANDALONE.ADVANCED_NITINOL, 1L))));
        alloys.put(
            Materials.NaquadahAlloy,
            Optional.of(
                List.of(
                    new Pair<>(Materials.Naquadah, 2L),
                    new Pair<>(Materials.Trinium, 1L),
                    new Pair<>(Materials.Carbon, 1L))));
        alloys.put(Materials.Thaumium, Optional.empty());
        alloys.put(Materials.Mithril, Optional.empty());
        alloys.put(Materials.AstralSilver, Optional.empty());
        alloys.put(
            Materials.DamascusSteel,
            Optional.of(
                List.of(
                    new Pair<>(Materials.Iron, 450L),
                    new Pair<>(Materials.Manganese, 4L),
                    new Pair<>(Materials.Chrome, 4L),
                    new Pair<>(Materials.Carbon, 10L),
                    new Pair<>(Materials.Silicon, 1L),
                    new Pair<>(Materials.Vanadium, 1L))));
        alloys.put(Materials.ShadowIron, Optional.empty());
        alloys.put(Materials.IronWood, Optional.empty());
        alloys.put(Materials.ShadowSteel, Optional.empty());
        alloys.put(Materials.Knightmetal, Optional.empty());
        alloys.put(Materials.PulsatingIron, Optional.empty());
        alloys.put(Materials.EnderiumBase, Optional.empty());
        alloys.put(Materials.RedstoneAlloy, Optional.empty());
        alloys.put(Materials.Bedrockium, Optional.empty());
        alloys.put(Materials.Alumite, Optional.empty());
        alloys.put(Materials.ClayCompound, Optional.empty());
        alloys.put(Materials.CrystallineAlloy, Optional.empty());
        alloys.put(Materials.CrystallinePinkSlime, Optional.empty());
        alloys.put(Materials.EnergeticSilver, Optional.empty());
        alloys.put(Materials.VividAlloy, Optional.empty());
        alloys.put(Materials.TPV, Optional.empty());
        alloys.put(Materials.NickelAluminide, Optional.empty());
        alloys.put(Materials.Void, Optional.empty());
        alloys.put(MaterialsAlloy.BLOODSTEEL, Optional.empty());
        alloys.put(MaterialsAlloy.STABALLOY, Optional.empty());
        alloys.put(MaterialsAlloy.MARAGING250, Optional.empty());
        alloys.put(MaterialsAlloy.MARAGING350, Optional.empty());
        alloys.put(MaterialsAlloy.TALONITE, Optional.empty());
        alloys.put(MaterialsAlloy.HASTELLOY_W, Optional.empty());
        alloys.put(MaterialsAlloy.NIOBIUM_CARBIDE, Optional.empty());
        alloys.put(MaterialsAlloy.ARCANITE, Optional.empty());
        alloys.put(MaterialsAlloy.LEAGRISIUM, Optional.empty());
        alloys.put(MaterialsAlloy.EGLIN_STEEL_BASE, Optional.empty());
        alloys.put(MaterialsAlloy.HS188A, Optional.empty());
        alloys.put(MaterialsAlloy.TRINIUM_TITANIUM, Optional.empty());
        alloys.put(MaterialsAlloy.TRINIUM_REINFORCED_STEEL, Optional.empty());
        alloys.put(MaterialsAlloy.KOBOLDITE, Optional.empty());
        alloys.put(MaterialsAlloy.HELICOPTER, Optional.empty());
        alloys.put(MaterialsAlloy.LAURENIUM, Optional.empty());
        alloys.put(MaterialsAlloy.BOTMIUM, Optional.empty());
        alloys.put(MaterialsAlloy.TITANSTEEL, Optional.empty());
        alloys.put(MaterialsAlloy.OCTIRON, Optional.empty());
        alloys.put(MaterialsAlloy.BLACK_TITANIUM, Optional.empty());
        alloys.put(MaterialsAlloy.BABBIT_ALLOY, Optional.empty());
        alloys.put(MaterialsAlloy.INDALLOY_140, Optional.empty());
        alloys.put(MaterialsAlloy.QUANTUM, Optional.empty());
        alloys.put(WerkstoffLoader.HDCS, Optional.empty());
        alloys.put(WerkstoffLoader.AdemicSteel, Optional.empty());
        alloys.put(
            WerkstoffLoader.TantalumHafniumCarbide,
            Optional.of(
                List.of(
                    new Pair<>(Materials.Tantalum, 4L),
                    new Pair<>(MaterialsElements.getInstance().HAFNIUM, 1L),
                    new Pair<>(Materials.Carbon, 5L))));
        alloys.put(GGMaterial.zincThoriumAlloy, Optional.empty());
        alloys.put(GGMaterial.zircaloy2, Optional.empty());
        alloys.put(GGMaterial.zircaloy4, Optional.empty());
        alloys.put(GGMaterial.titaniumBetaC, Optional.empty());
        alloys.put(GGMaterial.dalisenite, Optional.empty());
        alloys.put(GGMaterial.hikarium, Optional.empty());
        alloys.put(GGMaterial.tairitsu, Optional.empty());
        alloys.put(GGMaterial.preciousMetalAlloy, Optional.empty());
        alloys.put(GGMaterial.enrichedNaquadahAlloy, Optional.empty());

        return alloys;
    }
}
