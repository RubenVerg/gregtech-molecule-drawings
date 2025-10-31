package com.rubenverg.moldraw;

import dev.toma.configuration.Configuration;
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
    public boolean debugMode = false;

    @Configurable
    public boolean coloredAtoms = true;

    @Configurable
    public String defaultColor = "§e";

    @Configurable
    @Configurable.Range(min = 10, max = 50)
    public int scale = 20;
}
