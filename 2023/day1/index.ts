import { read } from "../utils/read";
import { join } from "node:path";

const input = read(join(import.meta.dir, "input.txt"));

function getTheNumber(line: string) {
  const l = line.length;
  let result = 0;

  for (let i = 0; i < l; i++) {
    const c = line[i];
    const n = Number(c);
    if (!Number.isNaN(n)) {
      result = n * 10;
      break;
    }
  }

  for (let i = l - 1; i >= 0; i--) {
    const c = line[i];
    const n = Number(c);
    if (!Number.isNaN(n)) {
      result += n;
      break;
    }
  }

  return result;
}

const NUMBERS = {
  zero: 0,
  one: 1,
  two: 2,
  three: 3,
  four: 4,
  five: 5,
  six: 6,
  seven: 7,
  eight: 8,
  nine: 9,
};
const NUMBERS_KEYS = Object.keys(NUMBERS) as (keyof typeof NUMBERS)[];

function getTheNumber2(line: string) {
  const l = line.length;
  let result = 0;

  outer: {
    for (let i = 0; i < l; i++) {
      const c = line[i];
      const n = Number(c);
      if (!Number.isNaN(n)) {
        result = n * 10;
        break outer;
      }
      for (const key of NUMBERS_KEYS) {
        if (line.startsWith(key, i)) {
          result = NUMBERS[key] * 10;
          break outer;
        }
      }
    }
  }

  outer: {
    for (let i = l - 1; i >= 0; i--) {
      const c = line[i];
      const n = Number(c);
      if (!Number.isNaN(n)) {
        result += n;
        break outer;
      }

      for (const key of NUMBERS_KEYS) {
        if (line.startsWith(key, i)) {
          result += NUMBERS[key];
          break outer;
        }
      }
    }
  }

  return result;
}

async function main1() {
  let result = 0;
  for await (const line of input) {
    const n = getTheNumber(line);
    result += n;
  }
  console.log(result);
}

async function main2() {
  let result = 0;
  for await (const line of input) {
    const n = getTheNumber2(line);
    result += n;
  }
  console.log(result);
}

main2();
