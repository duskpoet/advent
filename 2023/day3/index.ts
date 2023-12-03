import { join } from "path";
import { Challenge } from "../utils/challenge";
import { byLine } from "../utils/read";

class Cell {
  constructor(public char: string) {}

  marked = false;

  mark() {
    this.marked = true;
  }

  gears = [] as string[];

  markGear(row: number, col: number) {
    this.gears.push(JSON.stringify([row, col]));
  }

  isDigit() {
    return this.char >= "0" && this.char <= "9";
  }

  isDot() {
    return this.char === ".";
  }

  isSymbol() {
    return !this.isDot() && !this.isDigit();
  }

  isGear() {
    return this.char === "*";
  }
}

function walk(
  grid: Cell[][],
  row: number,
  col: number,
  cb: (cell: Cell | undefined) => void
) {
  for (let i = row - 1; i <= row + 1; i++) {
    for (let j = col - 1; j <= col + 1; j++) {
      if (i === row && j === col) continue;
      cb(grid[i]?.[j]);
    }
  }
}

function solution1(input: string) {
  const grid: Cell[][] = [];
  // parse input
  console.log("Parsing input...");
  for (const line of byLine(input)) {
    const row: Cell[] = [];
    grid.push(row);
    for (const char of line) {
      row.push(new Cell(char));
    }
  }

  // mark all adjacent cells to symbols
  console.log("Marking adjacent cells...");
  for (let i = 0; i < grid.length; i++) {
    for (let j = 0; j < grid[i].length; j++) {
      if (grid[i][j].isSymbol()) {
        walk(grid, i, j, (cell) => cell?.mark());
      }
    }
  }

  let result = 0;
  // collect numbers
  console.log("Collecting numbers...");
  for (let i = 0; i < grid.length; i++) {
    let currentNumber: string[] = [];
    let isAdjacentToSymbol = false;
    for (let j = 0; j < grid[i].length; j++) {
      const cell = grid[i][j];
      if (cell.isDigit()) {
        if (cell.marked) {
          isAdjacentToSymbol = true;
        }
        currentNumber.push(cell.char);
      } else if (currentNumber.length > 0) {
        if (isAdjacentToSymbol) {
          result += parseInt(currentNumber.join(""), 10);
        }
        isAdjacentToSymbol = false;
        currentNumber = [];
      }
    }
    if (currentNumber.length > 0 && isAdjacentToSymbol) {
      result += parseInt(currentNumber.join(""), 10);
    }
  }

  return result;
}

function solution2(input: string) {
  const grid: Cell[][] = [];
  // parse input
  console.log("Parsing input...");
  for (const line of byLine(input)) {
    const row: Cell[] = [];
    grid.push(row);
    for (const char of line) {
      row.push(new Cell(char));
    }
  }

  // mark all cells adjacent to symbols
  for (let i = 0; i < grid.length; i++) {
    for (let j = 0; j < grid[i].length; j++) {
      if (grid[i][j].isGear()) {
        walk(grid, i, j, (cell) => cell?.markGear(i, j));
      }
    }
  }

  // map of gears to numbers. number is represented as it's end position
  const gearsMap = {} as Record<string, string[]>;
  const numbers = {} as Record<string, number>;
  for (let i = 0; i < grid.length; i++) {
    let currentNumber: string[] = [];
    let gears = new Set<string>();
    for (let j = 0; j < grid[i].length; j++) {
      const cell = grid[i][j];
      if (cell.isDigit()) {
        currentNumber.push(cell.char);
        if (cell.gears.length > 0) {
          for (const gear of cell.gears) {
            gears.add(gear);
          }
        }
      } else if (currentNumber.length > 0) {
        if (gears.size > 0) {
          const number = parseInt(currentNumber.join(""), 10);
          const pos = JSON.stringify([i, j - 1]);
          numbers[pos] = number;
          gears.forEach((gear) => {
            if (!gearsMap[gear]) {
              gearsMap[gear] = [];
            }
            gearsMap[gear].push(pos);
          });
        }
        gears = new Set<string>();
        currentNumber = [];
      }
    }
    if (currentNumber.length > 0 && gears.size > 0) {
      const number = parseInt(currentNumber.join(""), 10);
      const pos = JSON.stringify([i, grid[i].length - 1]);
      numbers[pos] = number;
      gears.forEach((gear) => {
        if (!gearsMap[gear]) {
          gearsMap[gear] = [];
        }
        gearsMap[gear].push(pos);
      });
    }
  }

  // collect result from gears map
  let result = 0;
  Object.values(gearsMap).forEach((list) => {
    if (list.length === 2) {
      const [pos1, pos2] = list;
      result += numbers[pos1] * numbers[pos2];
    }
  });

  return result;
}

const challeng = new Challenge(
  join(import.meta.dir, "input.txt"),
  solution1,
  solution2
);

challeng.addTest(
  `467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..`,
  4361,
  467835
);

export default challeng;
