package com.rubenverg.moldraw.data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import oshi.util.tuples.Pair;

import java.util.*;

@SuppressWarnings("removal")
public class AlloysData {

    private static ResourceLocation tfg(String path) {
        return new ResourceLocation("tfg", path);
    }

    private static ResourceLocation cosmiccore(String path) {
        return new ResourceLocation("cosmiccore", path);
    }

    private static ResourceLocation gtca(String path) {
        return new ResourceLocation("gtca", path);
    }

    private static ResourceLocation gcyr(String path) {
        return new ResourceLocation("gcyr", path);
    }

    public static Map<ResourceLocation, Optional<List<Pair<ResourceLocation, Long>>>> alloys() {
        final Map<ResourceLocation, Optional<List<Pair<ResourceLocation, Long>>>> alloys = new HashMap<>();
        alloys.put(GTCEu.id("battery_alloy"), Optional.empty());
        alloys.put(GTCEu.id("brass"), Optional.empty());
        alloys.put(GTCEu.id("bronze"), Optional.empty());
        alloys.put(GTCEu.id("cupronickel"), Optional.empty());
        alloys.put(GTCEu.id("electrum"), Optional.empty());
        alloys.put(GTCEu.id("invar"), Optional.empty());
        alloys.put(GTCEu.id("kanthal"), Optional.empty());
        alloys.put(GTCEu.id("magnalium"), Optional.empty());
        alloys.put(GTCEu.id("nichrome"), Optional.empty());
        alloys.put(GTCEu.id("niobium_titanium"), Optional.empty());
        alloys.put(GTCEu.id("sterling_silver"), Optional.empty());
        alloys.put(GTCEu.id("rose_gold"), Optional.empty());
        alloys.put(GTCEu.id("black_bronze"), Optional.empty());
        alloys.put(GTCEu.id("bismuth_bronze"), Optional.empty());
        alloys.put(GTCEu.id("rtm_alloy"), Optional.empty());
        alloys.put(GTCEu.id("ruridit"), Optional.empty());
        alloys.put(GTCEu.id("soldering_alloy"), Optional.empty());
        alloys.put(GTCEu.id("stainless_steel"), Optional.empty());
        alloys.put(GTCEu.id("tin_alloy"), Optional.empty());
        alloys.put(GTCEu.id("ultimet"), Optional.empty());
        alloys.put(GTCEu.id("vanadium_gallium"), Optional.empty());
        alloys.put(GTCEu.id("yttrium_barium_cuprate"), Optional.empty());
        alloys.put(GTCEu.id("osmiridium"), Optional.empty());
        alloys.put(GTCEu.id("gallium_arsenide"), Optional.empty());
        alloys.put(GTCEu.id("indium_gallium_phosphide"), Optional.empty());
        alloys.put(GTCEu.id("nickel_zinc_ferrite"), Optional.empty());
        alloys.put(GTCEu.id("tungsten_carbide"), Optional.empty());
        alloys.put(GTCEu.id("manganese_phosphide"), Optional.empty());
        alloys.put(GTCEu.id("magnesium_diboride"), Optional.empty());
        alloys.put(GTCEu.id("mercury_barium_calcium_cuprate"), Optional.empty());
        alloys.put(GTCEu.id("uranium_triplatinum"), Optional.empty());
        alloys.put(GTCEu.id("samarium_iron_arsenic_oxide"), Optional.empty());
        alloys.put(GTCEu.id("indium_tin_barium_titanium_cuprate"), Optional.empty());
        alloys.put(GTCEu.id("uranium_rhodium_dinaquadide"), Optional.empty());
        alloys.put(GTCEu.id("enriched_naquadah_trinium_europium_duranide"), Optional.empty());
        alloys.put(GTCEu.id("ruthenium_trinium_americium_neutronate"), Optional.empty());
        alloys.put(GTCEu.id("black_steel"), Optional.empty());
        alloys.put(GTCEu.id("tungsten_steel"), Optional.empty());
        alloys.put(GTCEu.id("cobalt_brass"), Optional.empty());
        alloys.put(GTCEu.id("vanadium_steel"), Optional.empty());
        alloys.put(GTCEu.id("potin"), Optional.empty());
        alloys.put(GTCEu.id("naquadah_alloy"), Optional.empty());
        alloys.put(GTCEu.id("rhodium_plated_palladium"), Optional.empty());
        alloys.put(GTCEu.id("red_steel"), Optional.empty());
        alloys.put(GTCEu.id("blue_steel"), Optional.empty());
        alloys.put(GTCEu.id("hssg"), Optional.empty());
        alloys.put(GTCEu.id("red_alloy"), Optional.empty());
        alloys.put(GTCEu.id("hsse"), Optional.empty());
        alloys.put(GTCEu.id("hsss"), Optional.empty());
        alloys.put(GTCEu.id("blue_alloy"), Optional.empty());
        alloys.put(GTCEu.id("tantalum_carbide"), Optional.empty());
        alloys.put(GTCEu.id("hsla_steel"), Optional.empty());
        alloys.put(GTCEu.id("molybdenum_disilicide"), Optional.empty());
        alloys.put(GTCEu.id("zeron_100"), Optional.empty());
        alloys.put(GTCEu.id("watertight_steel"), Optional.empty());
        alloys.put(GTCEu.id("incoloy_ma_956"), Optional.empty());
        alloys.put(GTCEu.id("maraging_steel_300"), Optional.empty());
        alloys.put(GTCEu.id("hastelloy_x"), Optional.empty());
        alloys.put(GTCEu.id("stellite_100"), Optional.empty());
        alloys.put(GTCEu.id("titanium_carbide"), Optional.empty());
        alloys.put(GTCEu.id("titanium_tungsten_carbide"), Optional.empty());
        alloys.put(GTCEu.id("hastelloy_c_276"), Optional.empty());
        alloys.put(GTCEu.id("steel"), Optional.of(List.of(
                new Pair<>(GTMaterials.Iron.getResourceLocation(), 39L),
                new Pair<>(GTMaterials.Carbon.getResourceLocation(), 5L))));
        alloys.put(GTCEu.id("conductive_alloy"), Optional.empty());
        alloys.put(GTCEu.id("sodium_lead_alloy"), Optional.empty());
        alloys.put(GTCEu.id("neptunium_palladium_aluminium"), Optional.empty());
        alloys.put(GTCEu.id("lanthanum_gold_cadmium_curium_sulfate"), Optional.empty());
        alloys.put(GTCEu.id("dark_soularium"), Optional.empty());
        alloys.put(GTCEu.id("microversium"), Optional.empty());
        alloys.put(GTCEu.id("trinaquadalloy"), Optional.empty());
        alloys.put(GTCEu.id("end_steel"), Optional.empty());
        alloys.put(GTCEu.id("lumium"), Optional.empty());
        alloys.put(GTCEu.id("darconite"), Optional.empty());
        alloys.put(GTCEu.id("electrum_flux"), Optional.empty());
        alloys.put(GTCEu.id("electrum_flux"), Optional.empty());
        alloys.put(GTCEu.id("manyullyn"), Optional.empty());
        alloys.put(GTCEu.id("advanced_soldering_alloy"), Optional.empty());
        alloys.put(GTCEu.id("signalum"), Optional.empty());
        alloys.put(GTCEu.id("signalum"), Optional.empty());
        alloys.put(GTCEu.id("energetic_alloy"), Optional.empty());
        alloys.put(GTCEu.id("mythril"), Optional.empty());
        alloys.put(GTCEu.id("vibrant_alloy"), Optional.empty());
        alloys.put(GTCEu.id("enderium"), Optional.empty());
        alloys.put(GTCEu.id("dark_steel"), Optional.empty());
        alloys.put(GTCEu.id("electrical_steel"), Optional.empty());
        alloys.put(GTCEu.id("desh"), Optional.empty());
        alloys.put(GTCEu.id("ostrum"), Optional.empty());
        alloys.put(GTCEu.id("rocket_alloy_t1"), Optional.empty());
        alloys.put(GTCEu.id("ostrum_iodide"), Optional.empty());
        alloys.put(GTCEu.id("rocket_alloy_t2"), Optional.empty());
        alloys.put(GTCEu.id("aluminium_silicate"), Optional.empty());
        alloys.put(tfg("zirconium_diboride"), Optional.empty());
        alloys.put(tfg("tungsten_bismuth_oxide_composite"), Optional.empty());
        alloys.put(GTCEu.id("manasteel"), Optional.empty());
        alloys.put(GTCEu.id("annealed_manasteel"), Optional.empty());
        alloys.put(GTCEu.id("galvanized_ethersteel"), Optional.empty());
        alloys.put(cosmiccore("trinium_naqide"), Optional.empty());
        alloys.put(cosmiccore("prismatic_tungstensteel"), Optional.empty());
        alloys.put(cosmiccore("resonant_virtue_meld"), Optional.empty());
        alloys.put(cosmiccore("naquadric_superalloy"), Optional.empty());
        alloys.put(GTCEu.id("mar_m_200_steel"), Optional.empty());
        alloys.put(GTCEu.id("black_matter"), Optional.empty());
        alloys.put(gtca("artherium_sn"), Optional.empty());
        alloys.put(gtca("hg_1223"), Optional.empty());
        alloys.put(gtca("nimonic_80_a"), Optional.empty());
        alloys.put(gtca("mar_ce_m_200"), Optional.empty());
        alloys.put(gtca("cinobite_a_241"), Optional.empty());
        alloys.put(gtca("adamantium_alloy"), Optional.empty());
        alloys.put(gtca("inconel_792"), Optional.empty());
        alloys.put(gtca("dural"), Optional.empty());
        alloys.put(gtca("berwollium"), Optional.empty());
        alloys.put(gtca("dark_steel"), Optional.empty());
        alloys.put(gtca("incoloy_846"), Optional.empty());
        alloys.put(gtca("melodic_alloy"), Optional.empty());
        alloys.put(gtca("quantum_alloy"), Optional.empty());
        alloys.put(gtca("pikyonium_64_y"), Optional.empty());
        alloys.put(gtca("incoloy_020"), Optional.empty());
        alloys.put(gtca("vitallium"), Optional.empty());
        alloys.put(gtca("inconel_718"), Optional.empty());
        alloys.put(gtca("tm_20_mn_alloy"), Optional.empty());
        alloys.put(gtca("incoloy_ds"), Optional.empty());
        alloys.put(gtca("zirconium_carbide"), Optional.empty());
        alloys.put(gtca("nitinol_60"), Optional.empty());
        alloys.put(gtca("inconel_690"), Optional.empty());
        alloys.put(gtca("neutronex"), Optional.empty());
        alloys.put(gtca("end_steel"), Optional.empty());
        alloys.put(gtca("moltech"), Optional.empty());
        alloys.put(gtca("tantalloy_60"), Optional.empty());
        alloys.put(gtca("incoloy_ma_323"), Optional.empty());
        alloys.put(gtca("hastelloy_n"), Optional.empty());
        alloys.put(gtca("enriched_holmium"), Optional.empty());
        alloys.put(gtca("trinium_naquadah_carbonite"), Optional.empty());
        alloys.put(gtca("stellar_alloy"), Optional.empty());
        alloys.put(gtca("inconel_625"), Optional.empty());
        alloys.put(gtca("electrical_steel"), Optional.empty());
        alloys.put(gtca("silicon_carbide"), Optional.empty());
        alloys.put(gtca("c_n_f_alloy"), Optional.empty());
        alloys.put(gtca("lafium_compound"), Optional.empty());
        alloys.put(gtca("zeron_182"), Optional.empty());
        alloys.put(gtca("mar_m_200"), Optional.empty());
        alloys.put(gtca("trinium_naquadah"), Optional.empty());
        alloys.put(gtca("tantalloy_61"), Optional.empty());
        alloys.put(gtca("stellite_79"), Optional.empty());
        alloys.put(gtca("eglin_steel"), Optional.empty());
        alloys.put(gtca("incoloy_903"), Optional.empty());
        alloys.put(gtca("duranium_x"), Optional.empty());
        alloys.put(gcyr("trinaquadalloy"), Optional.empty());
        alloys.put(gcyr("bisalloy_400"), Optional.empty());
        alloys.put(GTCEu.id("shellite"), Optional.empty());
        alloys.put(GTCEu.id("twinite"), Optional.empty());
        alloys.put(GTCEu.id("dragonsteel"), Optional.empty());
        alloys.put(GTCEu.id("prismalium"), Optional.empty());
        alloys.put(GTCEu.id("melodium"), Optional.empty());
        alloys.put(GTCEu.id("stellarium"), Optional.empty());
        alloys.put(GTCEu.id("ancient_runicalium"), Optional.empty());
        alloys.put(GTCEu.id("austenitic_stainless_steel_304"), Optional.empty());
        alloys.put(GTCEu.id("weapon_grade_naquadah"), Optional.empty());
        alloys.put(GTCEu.id("void"), Optional.empty());
        alloys.put(GTCEu.id("zalloy"), Optional.empty());
        alloys.put(GTCEu.id("zirconium_selenide_diiodide"), Optional.empty());
        alloys.put(GTCEu.id("zircalloy_4"), Optional.empty());
        alloys.put(GTCEu.id("indium_tin_lead_cadmium_soldering_alloy"), Optional.empty());
        alloys.put(GTCEu.id("naquated_soldering_alloy"), Optional.empty());
        alloys.put(GTCEu.id("thorium_plut_duranide_241"), Optional.empty());
        alloys.put(GTCEu.id("sky_steel"), Optional.empty());
        alloys.put(GTCEu.id("gold_skystone_alloy"), Optional.empty());
        alloys.put(GTCEu.id("diamond_skystone_alloy"), Optional.empty());
        alloys.put(GTCEu.id("certus_quartz_skystone_alloy"), Optional.empty());
        alloys.put(GTCEu.id("fluix_steel"), Optional.empty());
        alloys.put(GTCEu.id("netherite_gold_skystone_alloy"), Optional.empty());
        alloys.put(GTCEu.id("netherite_certus_quartz_skystone_alloy"), Optional.empty());
        alloys.put(GTCEu.id("birmabright"), Optional.empty());
        alloys.put(GTCEu.id("duralumin"), Optional.empty());
        alloys.put(GTCEu.id("hydronalium"), Optional.empty());
        alloys.put(GTCEu.id("beryllium_aluminium_alloy"), Optional.empty());
        alloys.put(GTCEu.id("elgiloy"), Optional.empty());
        alloys.put(GTCEu.id("beryllium_bronze"), Optional.empty());
        alloys.put(GTCEu.id("silicon_bronze"), Optional.empty());
        alloys.put(GTCEu.id("kovar"), Optional.empty());
        alloys.put(GTCEu.id("zamak"), Optional.empty());
        alloys.put(GTCEu.id("tumbaga"), Optional.empty());
        alloys.put(GTCEu.id("astrenaloy_nx"), Optional.empty());
        alloys.put(GTCEu.id("thacoloy_nq_42x"), Optional.empty());
        alloys.put(GTCEu.id("titan_steel"), Optional.empty());
        alloys.put(GTCEu.id("lepton_coalescing_superalloy"), Optional.empty());
        alloys.put(GTCEu.id("neutronium_silicon_carbide"), Optional.empty());
        alloys.put(GTCEu.id("cerium_tritelluride"), Optional.empty());
        alloys.put(GTCEu.id("magmada_alloy"), Optional.empty());
        alloys.put(GTCEu.id("mythrolic_alloy"), Optional.empty());
        alloys.put(GTCEu.id("nyanium"), Optional.empty());
        alloys.put(GTCEu.id("starium_alloy"), Optional.empty());
        alloys.put(GTCEu.id("seaborgium_palladium_enriched_estalt_flevorium_alloy"), Optional.empty());
        alloys.put(GTCEu.id("astatium_bioselex_carbonite"), Optional.empty());
        alloys.put(GTCEu.id("astatine_bis_tritelluride_cobo_selenium"), Optional.empty());
        alloys.put(GTCEu.id("iron_titanium_oxide"), Optional.empty());
        alloys.put(GTCEu.id("astatine_bis_tritelluride_cobo_selenium_over_iron_titanium_oxide"), Optional.empty());
        alloys.put(GTCEu.id("polonium_bismide"), Optional.empty());
        alloys.put(GTCEu.id("bismuth_iridate"), Optional.empty());
        alloys.put(GTCEu.id("hafnide_ceramic_base"), Optional.empty());
        alloys.put(GTCEu.id("hafnide_ito_ceramic"), Optional.empty());
        alloys.put(GTCEu.id("polonium_flux"), Optional.empty());
        alloys.put(GTCEu.id("rhenium_super_composite_alloy"), Optional.empty());
        alloys.put(GTCEu.id("abyssal_alloy"), Optional.empty());
        alloys.put(GTCEu.id("chaotixic_alloy"), Optional.empty());
        alloys.put(GTCEu.id("chaotixic_alloy"), Optional.empty());
        alloys.put(GTCEu.id("ohmderblux_alloy"), Optional.empty());
        alloys.put(GTCEu.id("draconyallium"), Optional.empty());
        alloys.put(GTCEu.id("draco_abyssal"), Optional.empty());
        alloys.put(GTCEu.id("expeditalloy_d_17"), Optional.empty());
        alloys.put(GTCEu.id("rhenate_w"), Optional.empty());
        alloys.put(GTCEu.id("borealic_steel"), Optional.empty());
        alloys.put(GTCEu.id("hvga_steel"), Optional.empty());
        alloys.put(GTCEu.id("melastrium_mox"), Optional.empty());
        alloys.put(GTCEu.id("trikoductive_neutro_steel"), Optional.empty());
        alloys.put(GTCEu.id("soul_ascendant_cuperite"), Optional.empty());
        alloys.put(GTCEu.id("mythrotight_carbide_steel"), Optional.empty());
        alloys.put(GTCEu.id("aerorelient_steel"), Optional.empty());
        alloys.put(GTCEu.id("vastaqalloy_cr_4200x"), Optional.empty());
        alloys.put(GTCEu.id("ultispestalloy_cmsh"), Optional.empty());
        alloys.put(GTCEu.id("zerodic_trinate_steel"), Optional.empty());
        alloys.put(GTCEu.id("cast_iron"), Optional.empty());

        return alloys;
    }

    public static Optional<List<Pair<ResourceLocation, Long>>> read(JsonElement json) {
        final var obj = json.getAsJsonObject();
        if (obj.has("derive") && obj.getAsJsonPrimitive("derive").getAsBoolean()) return Optional.empty();
        if (obj.has("components") && obj.get("components").isJsonArray()) {
            final List<Pair<ResourceLocation, Long>> list = new ArrayList<>();
            for (final var e : obj.getAsJsonArray("components").asList()) {
                if (e.isJsonPrimitive()) list.add(new Pair<>(ResourceLocation.parse(e.getAsString()), 1L));
                else if (e.isJsonArray()) {
                    final var arr = e.getAsJsonArray().asList();
                    if (arr.size() != 2) throw new JsonParseException("Invalid alloy component");
                    list.add(new Pair<>(ResourceLocation.parse(arr.get(0).getAsString()),
                            arr.size() == 1 ? 1 : arr.get(1).getAsLong()));
                }
            }
            return Optional.of(list);
        }
        throw new JsonParseException("Invalid alloy JSON");
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static JsonObject write(Optional<List<Pair<ResourceLocation, Long>>> alloy) {
        final var obj = new JsonObject();
        if (alloy.isEmpty()) {
            obj.addProperty("derive", true);
        } else {
            final var arr = new JsonArray();
            for (final var component : alloy.get()) {
                final var pair = new JsonArray();
                pair.add(component.getA().toString());
                pair.add(component.getB());
                arr.add(pair);
            }
            obj.add("components", arr);
        }
        return obj;
    }
}
