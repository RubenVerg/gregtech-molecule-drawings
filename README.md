# GregTech Molecule Drawings

> *This branch is for GregTech 1.6.x*

This clientside only mod adds molecule drawings for organic molecules from GregTech CEu and its addons and modpacks in tooltips.

## Examples

|Polyethylene|Polybenzimidazole|Chloroform|
|---|---|---|
|![polyethylene tooltip screenshot](https://raw.githubusercontent.com/RubenVerg/gregtech-molecule-drawings/refs/heads/main/images/polyethylene.png)|![polybenzimidazole tooltip screenshot](https://raw.githubusercontent.com/RubenVerg/gregtech-molecule-drawings/refs/heads/main/images/polybenzimidazole.png)|![chloroform tooltip screenshot](https://raw.githubusercontent.com/RubenVerg/gregtech-molecule-drawings/refs/heads/main/images/chloroform.png)|

## Additional content support

MolDraw comes out of the box with support for not only most of the organic materials from base GTCEu, but also a bunch of addons and modpacks. The currently supported extensions are:

* GT--
* GregTech Community Additions
* Gregicality Rocketry
* Monifactory
* Star Technology
* Cosmic Frontiers
* Phoenix Forge Technologies

## What people say about this mod

> Probably the coolest thing I've ever seen anyone do with tooltips.

> My daily reminder that basically all organic chemistry is benzene with bits sticking out.

> The only thing I have to say is that it's peak.

> Now I can appreciate how complex the polymers I'm building are.

## Adding your own molecules

Molecules are stored in resource packs under `assets/<namespace>/molecules/<compound>.json`, corresponding to the GT material with ID `<namespace>:<compound>`.

Molecules follow this schema:

```typescript
// Amount of atoms defaults to 1
type Element = string | [string, number];

interface AtomCommon {
  type: 'atom';
  index: number; // The identifier used for referring to this atom
  element?: Element; // The main element of the atom, not present if the atom should be an invisible carbon
  above?: Element; // An element to show above the main one
  right?: Element; // etc
  below?: Element;
  left?: Element;
}

// An atom can either specify X and Y coordinates...
interface AtomXY extends AtomCommon {
  x: number;
  y: number;
}

// or U and V coordinates, which are unit vectors tilted 30 degrees respectively anticlockwise and clockwise from the positive X semiaxis.
interface AtomUV extends AtomCommon {
  u: number;
  v: number;
}

// A double bond has the first line like a single bond and the second line shifted rightwards from the starting atom; use double centered bonds if you want them to be both offset.
// A one-and-a-half bond is laid out like a double bond but the second line is dotted.
// Inward and outward bonds grow in size towards the second atom.
type BondType = 'single' | 'double' | 'double_centered' | 'triple' | 'inward' | 'outward' | 'thick' | 'one_and_half';

interface Bond {
  type: 'bond';
  a: number; // The first atom of the bond
  b: number; // The second atom of the bond
  bond_type: BondType;
}

// Parens are square brackets around certain atoms that show subscripts and/or superscripts.
interface Parens {
  type: 'parens';
  sup?: string; // Superscript text
  sub?: string; // Subscript text
  atoms: number[]; // List of atoms that are to be surrounded
}

type MoleculeElement = AtomXY | AtomUV | Bond | Parens;

// A molecule JSON file is of type Molecule.
interface Molecule {
  contents: MoleculeElement[];
}
```

An atom's position can either be stored as a pair of `x` and `y` coordinates or a pair of `u` and `v` coordinates, which are the unit vectors tilted 30 degrees respectively anticlockwise and clockwise from the positive horizontal semiaxis. Most often, encoding positions with `u` and `v` is easier since organic compounds' skeletal diagrams follow a hexagonal grid as much as possible.

For example, this is the encoding of benzene:

```json
{
  "contents": [
    {
      "type": "atom",
      "index": 0,
      "u": 0.0,
      "v": 0.0
    },
    {
      "type": "atom",
      "index": 1,
      "u": -1.0,
      "v": 1.0
    },
    {
      "type": "atom",
      "index": 2,
      "u": -1.0,
      "v": 2.0
    },
    {
      "type": "atom",
      "index": 3,
      "u": 0.0,
      "v": 2.0
    },
    {
      "type": "atom",
      "index": 4,
      "u": 1.0,
      "v": 1.0
    },
    {
      "type": "atom",
      "index": 5,
      "u": 1.0,
      "v": 0.0
    },
    {
      "type": "bond",
      "a": 0,
      "b": 1,
      "bond_type": "single"
    },
    {
      "type": "bond",
      "a": 1,
      "b": 2,
      "bond_type": "double"
    },
    {
      "type": "bond",
      "a": 2,
      "b": 3,
      "bond_type": "single"
    },
    {
      "type": "bond",
      "a": 3,
      "b": 4,
      "bond_type": "double"
    },
    {
      "type": "bond",
      "a": 4,
      "b": 5,
      "bond_type": "single"
    },
    {
      "type": "bond",
      "a": 5,
      "b": 0,
      "bond_type": "double"
    }
  ]
}
```
