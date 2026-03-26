package com.rubenverg.moldraw.kubejs;

import com.rubenverg.moldraw.MolDraw;
import com.rubenverg.moldraw.data.AlloysData;
import com.rubenverg.moldraw.molecule.*;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.resources.ResourceLocation;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MolDrawKJSPlugin extends KubeJSPlugin {
	@Override
	public void registerEvents() {
		MolDrawEvents.GROUP.register();
	}

	@Override
	public void generateAssetJsons(AssetJsonGenerator generator) {
		final Map<ResourceLocation, Molecule> molecules = new HashMap<>();
		MolDrawEvents.MOLECULES.post(ScriptType.CLIENT, new MolDrawMoleculesJS(molecules::put));
		molecules.forEach((id, molecule) -> {
			final var json = MolDraw.gson.toJsonTree(molecule, Molecule.class);
			generator.json(id.withPrefix("molecules/"), json);
		});
		final Map<ResourceLocation, Optional<List<Pair<ResourceLocation, Long>>>> alloys = new HashMap<>();
		MolDrawEvents.ALLOYS.post(ScriptType.CLIENT, new MolDrawAlloysJS(alloys::put));
		alloys.forEach((id, alloy) -> {
			final var json = AlloysData.write(alloy);
			generator.json(id.withPrefix("alloys/"), json);
		});
	}

	@Override
	public void registerBindings(BindingsEvent event) {
		event.add("Molecule", Molecule.class);
		event.add("MolElement", Element.class);
		event.add("MolAtom", Atom.class);
		event.add("MolBond", Bond.class);
		event.add("MolParens", Parens.class);
		event.add("MolBenzene", BenzeneRing.class);
		event.add("MolCircle", CircleTransformation.class);
	}

	@Override
	public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
		typeWrappers.registerSimple(Element.class, o -> {
			if (o instanceof Element element) return element;
			if (o instanceof com.gregtechceu.gtceu.api.data.chemical.Element element) return Element.create(element.symbol());
			if (o instanceof CharSequence seq) return Element.create(seq.toString());
			return null;
		});
	}
}
