import { join } from "path";
import { Challenge } from "../utils/challenge";
import { solution as solution1 } from "./part1";
import { solution as solution2 } from "./part2";

const challenge = new Challenge(
  join(import.meta.dir, "input.txt"),
  solution1,
  solution2
);
challenge.addTest(
  `32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483`,
  6440,
  5905
);

export default challenge;
