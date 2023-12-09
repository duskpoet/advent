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
  `RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)`,
  2
);

challenge.addTest(
  `LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)`,
  null,
  6
);

export default challenge;
