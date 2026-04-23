package com.rubenverg.moldraw.molecule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public interface CompositeElement<This extends CompositeElement<?>> extends MoleculeElement<This> {

    Collection<MoleculeElement<?>> children();

    default Collection<MoleculeElement<?>> flatChildren() {
        final List<MoleculeElement<?>> children = new ArrayList<>();
        for (final var child : children())
            if (child instanceof CompositeElement<?>composite) children.addAll(composite.flatChildren());
            else children.add(child);
        return children;
    }

    @Override
    default int[] coveredAtoms() {
        final IntList covered = new IntArrayList();
        for (final var child : children()) for (final var atom : child.coveredAtoms()) covered.add(atom);
        return covered.toIntArray();
    }

    @Override
    default void beforeAdd(Molecule to) {
        for (final var child : children()) child.beforeAdd(to);
    }
}
