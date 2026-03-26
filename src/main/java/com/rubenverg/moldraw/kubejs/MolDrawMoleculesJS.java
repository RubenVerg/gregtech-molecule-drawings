package com.rubenverg.moldraw.kubejs;

import com.rubenverg.moldraw.molecule.Molecule;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import lombok.experimental.Delegate;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MolDrawMoleculesJS extends EventJS {
	@HideFromJS
	private final BiConsumer<ResourceLocation, Molecule> consumer;

	public MolDrawMoleculesJS(BiConsumer<ResourceLocation, Molecule> consumer) {
		this.consumer = consumer;
	}

	public Builder create(ResourceLocation id) {
		return new Builder(mol -> consumer.accept(id, mol));
	}

	public static class Builder {
		@Delegate
		public final Molecule molecule = new Molecule();

		public Builder(Consumer<Molecule> consumer) {
			consumer.accept(molecule);
		}
	}
}
