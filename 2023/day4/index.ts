import { join } from "path";
import { Challenge } from "../utils/challenge";
import { byLine, split } from "../utils/read";

class Card {
  winningNumbers: Set<number>;
  gotNumbers: Array<number>;

  constructor(
    public cardNumber: number,
    winningNumbers: number[],
    gotNumbers: number[]
  ) {
    this.winningNumbers = new Set(winningNumbers);
    this.gotNumbers = gotNumbers;
  }

  whichNumbersWin() {
    return this.gotNumbers.filter((n) => this.winningNumbers.has(n));
  }
}

function parseLine(line: string) {
  const [title, numbers] = line.split(":");
  const cardNumber = /Card\s+(\d+)/.exec(title)![1];
  const [winningNumbers, gotNumbers] = numbers.split("|");

  return new Card(
    parseInt(cardNumber),
    winningNumbers
      .trim()
      .split(" ")
      .filter((n) => n.trim() !== "")
      .map((n) => parseInt(n)),
    gotNumbers
      .trim()
      .split(" ")
      .filter((n) => n.trim() !== "")
      .map((n) => parseInt(n))
  );
}

function solution1(input: string) {
  let result = 0;
  for (const line of split(input)) {
    const card = parseLine(line);
    const count = card.whichNumbersWin().length;
    if (count) {
      result += 1 << (count - 1);
    }
  }

  return result;
}

function solution2(input: string) {
  const lines = split(input);
  const numberOfCards = new Array(lines.length);
  numberOfCards.fill(1);
  for (const line of lines) {
    const card = parseLine(line);
    const count = card.whichNumbersWin().length;
    for (
      let i = card.cardNumber;
      i < Math.min(card.cardNumber + count, lines.length);
      i++
    ) {
      numberOfCards[i] += numberOfCards[card.cardNumber - 1];
    }
  }

  return numberOfCards.reduce((acc, n) => acc + n, 0);
}

const challenge = new Challenge(
  join(import.meta.dir, "input.txt"),
  solution1,
  solution2
);
challenge.addTest(
  `Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11`,
  13,
  30
);

export default challenge;
