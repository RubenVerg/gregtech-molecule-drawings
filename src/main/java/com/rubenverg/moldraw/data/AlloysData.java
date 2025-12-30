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

public class AlloysData {

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
