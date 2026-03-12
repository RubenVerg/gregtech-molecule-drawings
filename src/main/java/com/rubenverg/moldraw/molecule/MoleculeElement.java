package com.rubenverg.moldraw.molecule;

public interface MoleculeElement<This extends MoleculeElement<?>> {

    String type();

    int[] coveredAtoms();

    This replaceInOrder(int[] newIndices);

    default void beforeAdd(Molecule to) {}
}
