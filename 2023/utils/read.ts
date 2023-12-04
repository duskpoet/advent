export function* byLine(input: string) {
  for (const line of input.split("\n")) {
    if (!line) continue;
    yield line;
  }
}

export function split(input: string) {
  return input.trim().split("\n");
}
