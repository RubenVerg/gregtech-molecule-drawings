# GregTech Molecule Drawings

## Adding your own molecules

Molecules are stored in resource packs under `assets/<namespace>/molecules/<compound>.json`, corresponding to the GT material with ID `<namespace>:<compound>`.

Molecules follow this schema:

```typescript
interface AtomCommon {
  type: 'atom';
  index: number;
  element?: string;
}

interface AtomXY extends AtomCommon {
  x: number;
  y: number;
}

interface AtomUV extends AtomCommon {
  u: number;
  v: number;
}

type BondType = 'single' | 'double' | 'double_centered' | 'triple';

interface Bond {
  type: 'bond';
  a: number;
  b: number;
  bond_type: BondType;
}

interface Parens {
  type: 'parens';
  sup?: string;
  sub?: string;
  atoms: number[];
}

type MoleculeElement = AtomXY | AtomUV | Bond | Parens;

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
      "element": "",
      "u": 0.0,
      "v": 0.0
    },
    {
      "type": "atom",
      "index": 1,
      "element": "",
      "u": -1.0,
      "v": 1.0
    },
    {
      "type": "atom",
      "index": 2,
      "element": "",
      "u": -1.0,
      "v": 2.0
    },
    {
      "type": "atom",
      "index": 3,
      "element": "",
      "u": 0.0,
      "v": 2.0
    },
    {
      "type": "atom",
      "index": 4,
      "element": "",
      "u": 1.0,
      "v": 1.0
    },
    {
      "type": "atom",
      "index": 5,
      "element": "",
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
