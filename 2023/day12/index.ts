import { join } from "path";
import { List } from "immutable";
import { Challenge } from "../utils/challenge";
import { split } from "../utils/read";

function parseLine(input: string) {
  const [record, groups] = input.split(" ");
  return [record, groups.split(",").map(Number)] as const;
}

function getCacheKey(record: string, groups: List<number>) {
  return record.replace(/\.{2,}/g, ".") + "|" + groups.join(",");
}

function calcPossibilities(
  record: string,
  groups: List<number>,
  cache: Record<string, number>
): number {
  const cacheKey = getCacheKey(record, groups);
  if (cache[cacheKey] !== undefined) {
    return cache[cacheKey];
  }
  if (record === "") {
    return groups.size === 0 ? 1 : 0;
  }

  switch (record[0]) {
    case ".": {
      return calcPossibilities(record.substring(1), groups, cache);
    }
    case "#": {
      if (groups.size === 0) {
        return 0;
      }
      for (var i = 0; i < groups.first()!; i++) {
        if (record[i] === "." || record[i] === undefined) {
          return 0;
        }
      }
      if (record[i] === "#") {
        return 0;
      }
      if (record[i] === undefined) {
        return groups.size === 1 ? 1 : 0;
      }
      const r = calcPossibilities(
        "." + record.substring(groups.first()! + 1),
        groups.slice(1),
        cache
      );
      cache[cacheKey] = r;
      return r;
    }
    case "?": {
      const r =
        calcPossibilities("#" + record.substring(1), groups, cache) +
        calcPossibilities("." + record.substring(1), groups, cache);
      cache[cacheKey] = r;
      return r;
    }
  }
  throw new Error("unexpected sym");
}

function solution1(input: string) {
  let result = 0;
  for (const line of split(input)) {
    const [record, groups] = parseLine(line);
    result += calcPossibilities(record, List(groups), {});
  }
  return result;
}

function solution2(input: string) {
  let result = 0;
  const lines = split(input);
  for (let i = 0; i < lines.length; i++) {
    const [record, groups] = parseLine(lines[i]);
    let newRecord = "";
    let newGroups: number[] = [];
    for (let j = 0; j < 5; j++) {
      newRecord += record + "?";
      newGroups = newGroups.concat(groups);
    }
    newRecord = newRecord.substring(0, newRecord.length - 1);
    console.log("CALCULATING", i, lines.length, newRecord, newGroups);
    result += calcPossibilities(newRecord, List(newGroups), {});
  }

  return result;
}

const challenge = new Challenge(
  join(import.meta.dir, "input.txt"),
  solution1,
  solution2
);

challenge.addTest(
  `???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1`,
  21,
  525152
);

export default challenge;
