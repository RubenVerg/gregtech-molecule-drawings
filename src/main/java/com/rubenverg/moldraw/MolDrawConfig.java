package com.rubenverg.moldraw;

import com.rubenverg.moldraw.component.AlloyTooltipComponent;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.client.IValidationHandler;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = MolDraw.MOD_ID)
public class MolDrawConfig {

    public static MolDrawConfig INSTANCE;
    private static final Object LOCK = new Object();

    public static void init() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = Configuration.registerConfig(MolDrawConfig.class, ConfigFormats.yaml()).getConfigInstance();
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
        @Configurable.ValueUpdateCallback(method = "invalidateAlloyCache")
        public boolean recursive = true;

        @Configurable
        @Configurable.ValueUpdateCallback(method = "invalidateAlloyCache")
        public boolean partsByMass = true;

        @SuppressWarnings("unused")
        private void invalidateAlloyCache(boolean value, IValidationHandler handler) {
            AlloyTooltipComponent.invalidateComponentsCache();
        }
    }

    @Configurable
    public boolean debugMode = false;
}
