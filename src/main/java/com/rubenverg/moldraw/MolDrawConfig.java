package com.rubenverg.moldraw;

import com.rubenverg.moldraw.component.AlloyTooltipComponent;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;
import dev.toma.configuration.config.validate.IValidationResult;

@Config(id = MolDraw.MOD_ID)
public class MolDrawConfig {

    public static MolDrawConfig INSTANCE;
    private static final Object LOCK = new Object();

    public static void init() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                final var holder = Configuration.registerConfig(MolDrawConfig.class, ConfigFormats.YAML);
                holder.getConfigValue("alloy.recursive", Boolean.class).orElseThrow().addValidator((_v, _x) -> {
                    // This is really dumb, but I can't find a better way
                    AlloyTooltipComponent.invalidateComponentsCache();
                    return IValidationResult.success();
                });
                INSTANCE = holder.getConfigInstance();
            }
        }
    }

    @Configurable
    public boolean enabled = true;

    @Configurable
    public boolean onlyShowOnShift = false;

    @Configurable
    public ColorConfig color = new ColorConfig();

    public static class ColorConfig {

        @Configurable
        public boolean colors = true;

        @Configurable
        public boolean useMaterialColors = true;

        @Configurable
        public String defaultColor = "§e";

        @Configurable
        @Configurable.Range(min = 0, max = 1)
        public float minimumBrightness = 0.1f;
    }

    @Configurable
    public MoleculeConfig molecule = new MoleculeConfig();

    public static class MoleculeConfig {

        @Configurable
        public boolean showMolecules = true;

        @Configurable
        @Configurable.Range(min = 10, max = 50)
        public int moleculeScale = 20;

        public enum AromaticMode {

            DOUBLE_BONDS,
            CIRCLE,
            ;

            @Override
            public String toString() {
                return switch (this) {
                    case DOUBLE_BONDS -> "Double bonds (Kekulé)";
                    case CIRCLE -> "Circle (Thiele)";
                };
            }
        }

        @Configurable
        public AromaticMode benzeneCircle = AromaticMode.DOUBLE_BONDS;

        @Configurable
        public boolean spinMolecules = true;

        @Configurable
        public float spinSpeedMultiplier = 1;
    }

    @Configurable
    public AlloyConfig alloy = new AlloyConfig();

    public static class AlloyConfig {

        @Configurable
        public boolean showAlloys = true;

        @Configurable
        @Configurable.Range(min = 25, max = 50)
        public int pieChartRadius = 32;

        @Configurable
        public boolean recursive = true;

        @Configurable
        public boolean partsByMass = true;
    }

    @Configurable
    public FunConfig fun = new FunConfig();

    public static class FunConfig {

        @Configurable
        public boolean aromanticBenzene = false;
    }

    @Configurable
    public boolean debugMode = false;
}
