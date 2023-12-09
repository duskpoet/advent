import { split } from "../utils/read";

const calcGcd = (a: number, b: number): number => {
  if (a === 1) {
    return 1;
  }
  if (b === 1) {
    return 1;
  }
  if (a === b) {
    return a;
  }
  if (a % 2 === 0 && b % 2 === 0) {
    return 2 * calcGcd(a / 2, b / 2);
  }
  if (a % 2 === 0) {
    return calcGcd(a / 2, b);
  }
  if (b % 2 === 0) {
    return calcGcd(a, b / 2);
  }
  if (a > b) {
    return calcGcd((a - b) / 2, b);
  }
  return calcGcd((b - a) / 2, a);
};

const calcLcm = (a: number, b: number): number => {
  return (a * b) / calcGcd(a, b);
};

type Direction = "L" | "R";

export function solution(input: string) {
  const lines = split(input);
  const directions = lines[0];

  const map = {} as Record<string, { L: string; R: string; idx: number }>;
  for (let i = 2; i < lines.length; i++) {
    const [_, node, left, right] = lines[i].match(
      /(\w{3}) = \((\w{3}), (\w{3})\)/
    )!;
    map[node] = { L: left, R: right, idx: i - 2 };
  }

  const beginning = Object.keys(map).filter((key) => key.at(-1) === "A");
  console.log("Beginning", beginning);
  let result = 1;
  for (
    let currentPathIdx = 0;
    currentPathIdx < beginning.length;
    currentPathIdx++
  ) {
    let current = beginning[currentPathIdx];
    console.log("Current", current);
    let step = 0;

    while (current.at(-1) !== "Z") {
      current = map[current][directions[step % directions.length] as Direction];
      step++;
    }
    console.log("Found finish", current, step);

    result = calcLcm(result, step);
  }
  return result;
}
