package com.rubenverg.moldraw;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.gtnewhorizon.gtnhlib.config.Config;

import akka.japi.Pair;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;

@Config(modid = MolDraw.MOD_ID, filename = "moldraw")
@Config.Order(0)
@Config.Entry(GuiConfigEntries.CategoryEntry.class)
public class MolDrawConfig {

    @Config.Comment("Enable mod")
    public static boolean enabled = true;

    @Config.Comment("Only show drawings when pressing Shift")
    public static boolean onlyShowOnShift = false;

    @Config.Comment("Colors")
    @Config.Entry(GuiConfigEntries.CategoryEntry.class)
    public static ColorConfig color = new ColorConfig();

    public static class ColorConfig {

        @Config.Comment("Color element symbols")
        public boolean colors = true;

        @Config.Comment("Use material colors for elements")
        public boolean useMaterialColors = true;

        @Config.Comment("Default element color")
        public String defaultColor = "&e";

        @Config.Comment("Minimum brightness for colored elements")
        @Config.RangeFloat(min = 0, max = 1)
        public float minimumBrightness = 0.1f;
    }

    @Config.Comment("Molecules")
    @Config.Entry(GuiConfigEntries.CategoryEntry.class)
    public static MoleculeConfig molecule = new MoleculeConfig();

    public static class MoleculeConfig {

        @Config.Comment("Show molecule drawings")
        public boolean showMolecules = true;

        @Config.Comment("Molecule scale")
        @Config.RangeInt(min = 10, max = 50)
        public int moleculeScale = 20;

        public static enum AromaticMode {

            DOUBLE_BONDS,
            CIRCLE,;

            @Override
            public String toString() {
                return switch (this) {
                    case DOUBLE_BONDS -> "Double bonds (Kekulé)";
                    case CIRCLE -> "Circle (Thiele)";
                };
            }
        }

        public static class AromaticModeEntry extends GuiConfigEntries.SelectValueEntry {

            public AromaticModeEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList,
                IConfigElement<String> configElement) {
                super(owningScreen, owningEntryList, configElement, getSelectableValues());
            }

            private static Map<Object, String> getSelectableValues() {
                return Arrays.stream(AromaticMode.values())
                    .map(mode -> new Pair<>(mode, mode.toString()))
                    .collect(Collectors.toMap(Pair::first, Pair::second));
            }
        }

        @Config.Comment("Benzene render mode")
        @Config.Entry(AromaticModeEntry.class)
        public AromaticMode benzeneCircle = AromaticMode.DOUBLE_BONDS;

        @Config.Comment("Allow molecules to spin")
        public boolean spinMolecules = true;

        @Config.Comment("Spin speed multiplier")
        public float spinSpeedMultiplier = 1;
    }

    @Config.Comment("Alloys")
    @Config.Entry(GuiConfigEntries.CategoryEntry.class)
    public static AlloyConfig alloy = new AlloyConfig();

    public static class AlloyConfig {

        @Config.Comment("Show alloy composition charts")
        public boolean showAlloys = true;

        @Config.Comment("Radius of the alloy composition pie chart")
        @Config.RangeInt(min = 25, max = 50)
        public int pieChartRadius = 32;

        @Config.Comment("Recursively decompose alloys into their constituent elements")
        public boolean recursive = true;

        @Config.Comment("Show percentages of alloy components by mass")
        public boolean partsByMass = true;
    }

    @Config.Comment("Fun")
    @Config.Entry(GuiConfigEntries.CategoryEntry.class)
    public static FunConfig fun = new FunConfig();

    public static class FunConfig {

        @Config.Comment("Aromantic Benzene")
        public boolean aromanticBenzene = false;
    }

    @Config.Comment("Debug mode")
    public static boolean debugMode = false;
}
