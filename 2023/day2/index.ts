import { join } from "node:path";
import { byLine } from "../utils/read";
import { Challenge } from "../utils/challenge";

type Color = "red" | "green" | "blue";
class Handful {
  constructor(
    public red: number = 0,
    public green: number = 0,
    public blue: number = 0
  ) {}
  power() {
    return this.red * this.green * this.blue;
  }
}

class Game {
  constructor(public id: number, public hauls: Handful[]) {}
}

const parse = (input: string): Game => {
  // console.log("Parsing", input);
  const [game, haul] = input.split(":");
  const gameId = game.substring(5).trim();
  const hauls = haul
    .trim()
    .split(";")
    .map((handful) => {
      handful = handful.trim();
      const result = new Handful();
      const reg = /(\d+) (\w+)/g;
      while (true) {
        const match = reg.exec(handful);
        if (!match) break;
        const [_, count, color] = match;
        result[color as Color] = Number(count);
      }
      return result;
    });
  return new Game(Number(gameId), hauls);
};

const isGamePossible = (limiting: Handful) => (game: Game) => {
  for (const haul of game.hauls) {
    if (
      haul.red > limiting.red ||
      haul.green > limiting.green ||
      haul.blue > limiting.blue
    )
      return false;
  }
  return true;
};

function solution1(input: string) {
  const possibleGames: Game[] = [];
  const checkIfGamePossible = isGamePossible(new Handful(12, 13, 14));
  for (const line of byLine(input)) {
    const game = parse(line);
    if (checkIfGamePossible(game)) possibleGames.push(game);
  }

  return possibleGames.reduce((acc, game) => acc + game.id, 0);
}

function minPossibleSet(game: Game) {
  const result = new Handful();
  for (const haul of game.hauls) {
    result.red = Math.max(result.red, haul.red);
    result.green = Math.max(result.green, haul.green);
    result.blue = Math.max(result.blue, haul.blue);
  }
  return result;
}

function solution2(input: string) {
  let result = 0;
  for (const line of byLine(input)) {
    const game = parse(line);
    const min = minPossibleSet(game);
    result += min.power();
  }
  return result;
}

const challenge = new Challenge(
  join(import.meta.dir, "input.txt"),
  solution1,
  solution2
);

export default challenge;
