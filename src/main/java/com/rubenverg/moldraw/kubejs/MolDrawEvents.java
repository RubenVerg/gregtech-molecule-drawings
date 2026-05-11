package com.rubenverg.moldraw.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public class MolDrawEvents {

    public static final EventGroup GROUP = EventGroup.of("MolDrawEvents");

    public static final EventHandler MOLECULES = GROUP.client("molecules", () -> MolDrawMoleculesJS.class);
    public static final EventHandler ALLOYS = GROUP.client("alloys", () -> MolDrawAlloysJS.class);
}
