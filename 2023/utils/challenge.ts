type Solution = (input: string) => string | number;

export class Challenge {
  constructor(
    public inputFilePath: string,
    public part1Solution: Solution,
    public part2Solution?: Solution
  ) {}

  public async run(): Promise<void> {
    const input = await Bun.file(this.inputFilePath).text();
    console.log("Part 1 solution:", this.part1Solution(input));

    if (this.part2Solution) {
      console.log("Part 2 solution:", this.part2Solution(input));
    }
  }
}
