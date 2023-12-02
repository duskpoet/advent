export function* byLine(input: string) {
  for (const line of input.split("\n")) {
    if (!line) continue;
    yield line;
  }
}
