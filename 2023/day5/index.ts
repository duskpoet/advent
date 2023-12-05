import { join } from "path";
import { Challenge } from "../utils/challenge";
import { split } from "../utils/read";

class Mapping {
  constructor(
    public destination: number,
    public source: number,
    public range: number
  ) {}

  map(item: number) {
    if (item < this.source || item >= this.source + this.range) {
      return null;
    }
    return this.destination + (item - this.source);
  }

  sourceRange() {
    return new Range(this.source, this.source + this.range - 1);
  }
}

function parseMapping(line: string) {
  const [destination, source, range] = line.split(" ").map(Number);
  return new Mapping(destination, source, range);
}

class Section {
  constructor(public name: string, public mappings: Mapping[]) {
    this.mappings.sort((a, b) => a.source - b.source);
  }
}

function parseSection(lines: string[], position: number): [Section, number] {
  const name = lines[position].split(" ")[0];
  position++;
  const mappings: Mapping[] = [];

  while (position < lines.length && lines[position].trim() !== "") {
    mappings.push(parseMapping(lines[position]));
    position++;
  }

  return [new Section(name, mappings), position + 1];
}

function solution1(input: string) {
  const lines = split(input);
  const seeds = lines[0].split(":")[1].trim().split(" ").map(Number);
  let position = 2;
  let currentItems = seeds;
  while (position < lines.length) {
    const [section, newPostion] = parseSection(lines, position);
    position = newPostion;

    for (let itemIdx = 0; itemIdx < currentItems.length; itemIdx++) {
      const item = currentItems[itemIdx];
      for (const mapping of section.mappings) {
        if (item < mapping.source) {
          break;
        }
        const mapped = mapping.map(item);
        if (mapped !== null) {
          currentItems[itemIdx] = mapped;
          break;
        }
      }
    }
  }

  return Math.min(...currentItems);
}

class Range {
  constructor(public start: number, public end: number) {}

  subtract(range: Range): Range[] {
    if (range.end < this.start || range.start > this.end) {
      return [this];
    }

    if (range.start <= this.start && range.end >= this.end) {
      return [];
    }

    if (range.start <= this.start) {
      return [new Range(range.end + 1, this.end)];
    }

    if (range.end >= this.end) {
      return [new Range(this.start, range.start - 1)];
    }

    return [
      new Range(this.start, range.start - 1),
      new Range(range.end + 1, this.end),
    ];
  }

  intersect(range: Range) {
    if (range.end < this.start || range.start > this.end) {
      return null;
    }

    return new Range(
      Math.max(this.start, range.start),
      Math.min(this.end, range.end)
    );
  }
}

function* parseSeeds(line: string) {
  const numbers = line.split(":")[1].trim().split(" ").map(Number);
  for (let i = 0; i < numbers.length; i += 2) {
    const start = numbers[i];
    const end = numbers[i + 1] + start - 1;
    yield new Range(start, end);
  }
}

class Ranges {
  constructor(public ranges: Range[]) {
    this.ranges.sort((a, b) => a.start - b.start);
  }
}

function solution2(input: string) {
  const lines = split(input);
  const seeds = Array.from(parseSeeds(lines[0]));
  let position = 2;
  let currentItems = seeds;
  while (position < lines.length) {
    const [section, newPostion] = parseSection(lines, position);
    position = newPostion;
    let newItems: Range[] = [];
    while (currentItems.length > 0) {
      const item = currentItems.shift()!;
      for (const mapping of section.mappings) {
        const intersection = item.intersect(mapping.sourceRange());
        if (intersection !== null) {
          const mapped = new Range(
            mapping.destination + intersection.start - mapping.source,
            mapping.destination + intersection.end - mapping.source
          );
          newItems.push(mapped);

          const subtracted = item.subtract(intersection);
          currentItems.push(...subtracted);
          item.end = intersection.start - 1;
          break;
        }
      }

      if (item.start <= item.end) {
        newItems.push(item);
      }
    }

    currentItems = newItems;
    currentItems.sort((a, b) => a.start - b.start);
  }

  return currentItems[0].start;
}

const challenge = new Challenge(
  join(import.meta.dir, "input.txt"),
  solution1,
  solution2
);

challenge.addTest(
  `seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4`,
  35,
  46
);

export default challenge;
