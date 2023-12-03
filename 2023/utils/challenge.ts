type Solution = (input: string) => string | number;

export class Challenge {
  constructor(
    public inputFilePath: string,
    public part1Solution: Solution,
    public part2Solution?: Solution
  ) {}

  tests: {
    input: string;
    expected1: string | number;
    expected2?: string | number;
  }[] = [];

  public async run(): Promise<void> {
    const input = await Bun.file(this.inputFilePath).text();
    console.log("Part 1 solution:", this.part1Solution(input));

    if (this.part2Solution) {
      console.log("Part 2 solution:", this.part2Solution(input));
    }
  }

  public addTest(
    input: string,
    expected1: string | number,
    expected2?: string | number
  ): void {
    this.tests.push({ input, expected1, expected2 });
  }

  public runTests(): void {
    for (const test of this.tests) {
      const actual1 = this.part1Solution(test.input);
      if (actual1 !== test.expected1) {
        console.error(
          `Test failed. Expected: ${test.expected1}. Actual: ${actual1}`
        );
      } else {
        console.log("Test for part 1 passed ✅");
      }

      if (this.part2Solution && test.expected2) {
        const actual2 = this.part2Solution(test.input);
        if (actual2 !== test.expected2) {
          console.error(
            `Test failed. Expected: ${test.expected2}. Actual: ${actual2}`
          );
        } else {
          console.log("Test for part 2 passed ✅");
        }
      }
    }
  }
}
