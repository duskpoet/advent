import { split } from "../utils/read";

type Direction = "L" | "R";

export function solution(input: string) {
  const lines = split(input);
  const directions = lines[0];

  const map = {} as Record<string, { L: string; R: string }>;
  for (let i = 2; i < lines.length; i++) {
    const [_, node, left, right] = lines[i].match(
      /(\w{3}) = \((\w{3}), (\w{3})\)/
    )!;
    map[node] = { L: left, R: right };
  }

  let currentNode = "AAA";
  let steps = 0;
  while (currentNode !== "ZZZ") {
    const direction = directions[steps % directions.length] as Direction;
    currentNode = map[currentNode][direction];
    steps++;
  }

  return steps;
}
