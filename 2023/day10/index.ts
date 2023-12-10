import { join } from "path";
import { Challenge } from "../utils/challenge";
import { split } from "../utils/read";

class Pos {
  constructor(public row: number, public col: number) {}

  isSame(pos: Pos) {
    return this.row === pos.row && this.col === pos.col;
  }

  toString() {
    return `(${this.row}, ${this.col})`;
  }
}

function searchStartPos(lines: string[]) {
  for (let row = 0; row < lines.length; row++) {
    for (let col = 0; col < lines[row].length; col++) {
      if (lines[row][col] === "S") {
        return new Pos(row, col);
      }
    }
  }
}

function possibleMotions(lines: string[], pos: Pos) {
  const char = lines[pos.row][pos.col];
  switch (char) {
    case "S":
      return [
        new Pos(pos.row - 1, pos.col),
        new Pos(pos.row, pos.col + 1),
        new Pos(pos.row + 1, pos.col),
        new Pos(pos.row, pos.col - 1),
      ];
    case ".":
    case undefined:
      return [];
    case "|":
      return [new Pos(pos.row - 1, pos.col), new Pos(pos.row + 1, pos.col)];
    case "-":
      return [new Pos(pos.row, pos.col - 1), new Pos(pos.row, pos.col + 1)];
    case "L":
      return [new Pos(pos.row - 1, pos.col), new Pos(pos.row, pos.col + 1)];
    case "J":
      return [new Pos(pos.row - 1, pos.col), new Pos(pos.row, pos.col - 1)];
    case "7":
      return [new Pos(pos.row, pos.col - 1), new Pos(pos.row + 1, pos.col)];
    case "F":
      return [new Pos(pos.row, pos.col + 1), new Pos(pos.row + 1, pos.col)];
    default:
      throw new Error(`Unknown char ${char}`);
  }
}

function possibleWays(lines: string[], pos: Pos) {
  const motions = possibleMotions(lines, pos);
  return motions.filter((motion) =>
    possibleMotions(lines, motion).find((m) => m.isSame(pos))
  );
}

class Chain {
  path: Pos[] = [];

  visited: Set<string> = new Set();

  add(pos: Pos) {
    this.path.push(pos);
    this.visited.add(pos.toString());
  }

  has(pos: Pos) {
    return this.visited.has(pos.toString());
  }
}

export function solution1(input: string) {
  const lines = split(input);
  const startPos = searchStartPos(lines)!;
  const ways = possibleWays(lines, startPos);
  for (const way of ways) {
    let currentPos = way;
    let prevPos = startPos;
    let steps = 1;
    while (true) {
      const nextPrev = currentPos;
      const nextCurrentPos = possibleWays(lines, currentPos).find(
        (pos) => !pos.isSame(prevPos)
      );
      if (!nextCurrentPos) {
        break;
      }
      currentPos = nextCurrentPos;
      prevPos = nextPrev;
      steps++;
      if (lines[currentPos.row][currentPos.col] === "S") {
        return Math.ceil(steps / 2);
      }
    }
  }
  return 0;
}

function solution2(input: string) {
  throw new Error("Not implemented");
  // I wanted to get some overview so I started painting the stuff, and then I had a sudden idea, and I kind of used built-in browser functionality to calc my answer... So here is my part 2 solution: https://observablehq.com/d/0985cc11fce816f7
}

const challenge = new Challenge(join(import.meta.dir, "input.txt"), solution1);

challenge.addTest(
  `-L|F7
7S-7|
L|7||
-L-J|
L|-JF`,
  4
);

export default challenge;
