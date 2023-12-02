import { readFile } from "node:fs/promises";

export const read = async function* (path: string) {
  const lines = await readFile(path, { encoding: "utf8" });
  for (const l of lines.split("\n")) {
    if (!l) continue;
    yield l;
  }
};
