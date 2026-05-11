package com.rubenverg.moldraw.kubejs;

import net.minecraft.resources.ResourceLocation;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MolDrawAlloysJS extends EventJS {

    @HideFromJS
    private final BiConsumer<ResourceLocation, Optional<List<Pair<ResourceLocation, Long>>>> consumer;

    public MolDrawAlloysJS(BiConsumer<ResourceLocation, Optional<List<Pair<ResourceLocation, Long>>>> consumer) {
        this.consumer = consumer;
    }

    public void derive(ResourceLocation id) {
        consumer.accept(id, Optional.empty());
    }

    public Builder create(ResourceLocation id) {
        return new Builder(alloy -> consumer.accept(id, Optional.of(alloy)));
    }

    public static class Builder {

        public final List<Pair<ResourceLocation, Long>> components = new ArrayList<>();

        public Builder(Consumer<List<Pair<ResourceLocation, Long>>> consumer) {
            consumer.accept(components);
        }

        public Builder component(ResourceLocation material, long count) {
            components.add(new Pair<>(material, count));
            return this;
        }
    }
}
