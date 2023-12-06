import { join } from "path";
import challeng from "../day3";
import { Challenge } from "../utils/challenge";
import { split } from "../utils/read";

function solution1(input: string) {
  const [timeLine, distanceLine] = split(input);
  const times = timeLine
    .split(":")[1]
    .split(" ")
    .filter((l) => l.trim() !== "")
    .map((l) => parseInt(l));
  const distances = distanceLine
    .split(":")[1]
    .split(" ")
    .filter((l) => l.trim() !== "")
    .map((l) => parseInt(l));

  let result = 1;

  for (let i = 0; i < times.length; i++) {
    const time = times[i];
    const distance = distances[i] + 1;
    const base = time / 2;
    const discriminantHalf = (time ** 2 - 4 * distance) ** 0.5 / 2;
    const left = Math.ceil(base - discriminantHalf);
    const right = Math.floor(base + discriminantHalf);

    result *= right - left + 1;
  }

  return result;
}

function solution2(input: string) {
  const [timeLine, distanceLine] = split(input);
  const time = parseInt(timeLine.split(":")[1].replaceAll(" ", ""));

  const distance = parseInt(distanceLine.split(":")[1].replaceAll(" ", "")) + 1;
  const base = time / 2;
  const discriminantHalf = (time ** 2 - 4 * distance) ** 0.5 / 2;
  const left = Math.ceil(base - discriminantHalf);
  const right = Math.floor(base + discriminantHalf);

  return right - left + 1;
}

const challenge = new Challenge(
  join(import.meta.dir, "input.txt"),
  solution1,
  solution2
);

challenge.addTest(
  `Time:      7  15   30
Distance:  9  40  200`,
  288,
  71503
);

export default challenge;
