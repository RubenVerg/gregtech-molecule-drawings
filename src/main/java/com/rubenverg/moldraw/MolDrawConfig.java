package com.rubenverg.moldraw;

import java.io.File;
import java.util.Arrays;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import com.rubenverg.moldraw.component.AlloyTooltipHandler;

public class MolDrawConfig {

    public static MolDrawConfig INSTANCE;
    private static final Object LOCK = new Object();

    public static void init() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = new MolDrawConfig();
                INSTANCE.reload();
            }
        }
    }

    public void reload() {
        final var config = new Configuration(new File(Launch.minecraftHome, "config/moldraw.cfg"));
        config.load();

        enabled = config.getBoolean("enabled", "", true, "");
        onlyShowOnShift = config.getBoolean("onlyShowOnShift", "", false, "");

        color.colors = config.getBoolean("colors", "color", true, "");
        color.useMaterialColors = config.getBoolean("useMaterialColors", "color", true, "");
        color.defaultColor = config.getString("defaultColor", "color", "§e", "");
        color.minimumBrightness = config.getFloat("minimumBrightness", "color", 0.1f, 0, 1, "");

        molecule.showMolecules = config.getBoolean("showMolecules", "molecule", true, "");
        molecule.moleculeScale = config.getInt("moleculeScale", "molecule", 20, 10, 50, "");
        molecule.benzeneCircle = MoleculeConfig.AromaticMode.valueOf(
            config.getString(
                "benzeneCircle",
                "molecule",
                MoleculeConfig.AromaticMode.DOUBLE_BONDS.name(),
                "",
                Arrays.stream(MoleculeConfig.AromaticMode.values())
                    .map(MoleculeConfig.AromaticMode::name)
                    .toArray(String[]::new)));
        molecule.spinMolecules = config.getBoolean("spinMolecules", "molecule", true, "");
        molecule.spinSpeedMultiplier = config.getFloat("spinSpeedMultiplier", "molecule", 1, 0, 5, "");

        alloy.showAlloys = config.getBoolean("showAlloys", "alloy", true, "");
        alloy.pieChartRadius = config.getInt("pieChartRadius", "alloy", 32, 25, 50, "");
        alloy.recursive = config.getBoolean("recursive", "alloy", true, "");
        alloy.partsByMass = config.getBoolean("partsByMass", "alloy", true, "");

        fun.aromanticBenzene = config.getBoolean("aromanticBenzene", "fun", false, "");

        debugMode = config.getBoolean("debugMode", "", false, "");

        if (config.hasChanged()) {
            AlloyTooltipHandler.invalidateComponentsCache();
            config.save();
        }
    }

    public boolean enabled;

    public boolean onlyShowOnShift;

    public final ColorConfig color = new ColorConfig();

    public static class ColorConfig {

        public boolean colors;

        public boolean useMaterialColors;

        public String defaultColor;

        public float minimumBrightness;
    }

    public final MoleculeConfig molecule = new MoleculeConfig();

    public static class MoleculeConfig {

        public boolean showMolecules;

        public int moleculeScale;

        public enum AromaticMode {

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

        public AromaticMode benzeneCircle;

        public boolean spinMolecules;

        public float spinSpeedMultiplier;
    }

    public final AlloyConfig alloy = new AlloyConfig();

    public static class AlloyConfig {

        public boolean showAlloys;

        public int pieChartRadius;

        public boolean recursive;

        public boolean partsByMass;
    }

    public final FunConfig fun = new FunConfig();

    public static class FunConfig {

        public boolean aromanticBenzene;
    }

    public boolean debugMode;
}
