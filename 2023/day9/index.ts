import { join } from "path";
import { Challenge } from "../utils/challenge";
import { split } from "../utils/read";

function extrapolateValues(nums: number[]) {
  const current = [...nums];
  const lastNums: number[] = [];

  while (current.length) {
    lastNums.push(current.at(-1)!);
    let item = current.shift()!;
    for (let i = 0; i < current.length; i++) {
      const newItem = current[i];
      current[i] = newItem - item;
      item = newItem;
    }

    while (current.length && current[0] === 0) current.shift();
  }

  return lastNums.reduce((acc, item) => acc + item, 0);
}

function extrapolateBackwards(nums: number[]) {
  const current = [...nums];
  const firstNums: number[] = [];

  while (!current.every((x) => x === 0)) {
    firstNums.push(current[0]);
    let item = current.shift()!;
    for (let i = 0; i < current.length; i++) {
      const newItem = current[i];
      current[i] = newItem - item;
      item = newItem;
    }
  }

  let result = firstNums.pop()!;
  while (firstNums.length) {
    result = firstNums.pop()! - result;
  }

  return result;
}

function solution1(input: string) {
  const lines = split(input);

  let result = 0;
  for (const line of lines) {
    const nums = line.split(" ").map(Number);
    result += extrapolateValues(nums);
  }

  return result;
}

function solution2(input: string) {
  const lines = split(input);

  let result = 0;
  for (const line of lines) {
    const nums = line.split(" ").map(Number);
    result += extrapolateBackwards(nums);
  }

  return result;
}
const challenge = new Challenge(
  join(import.meta.dir, "input.txt"),
  solution1,
  solution2
);

challenge.addTest(
  `0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45`,
  114,
  2
);

export default challenge;
