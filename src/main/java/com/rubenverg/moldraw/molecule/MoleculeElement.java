package com.rubenverg.moldraw.molecule;

public interface MoleculeElement<This extends MoleculeElement<?>> {

    int[] coveredAtoms();

    This replaceInOrder(int[] newIndices);
}
