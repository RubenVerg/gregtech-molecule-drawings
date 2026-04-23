package com.rubenverg.moldraw.molecule;

import java.util.Optional;

import org.joml.Vector3f;

import com.google.gson.*;

public record Atom(int index, Element.Counted element, Optional<Element.Counted> above, Optional<Element.Counted> right,
    Optional<Element.Counted> below, Optional<Element.Counted> left, Vector3f position, int spinGroup)
    implements MoleculeElement<Atom> {

    @Override
    public String type() {
        return "atom";
    }

    @Override
    public int[] coveredAtoms() {
        return new int[] { index };
    }

    @Override
    public Atom replaceInOrder(int[] newIndices) {
        return new Atom(newIndices[0], element, above, right, below, left, new Vector3f(position), spinGroup);
    }

    @Override
    public void beforeAdd(Molecule to) {
        position.mul(to.transformation());
    }

    public boolean isInvisible() {
        return element.element().invisible;
    }
}
