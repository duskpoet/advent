import { join } from "node:path";
import { read } from "../utils/read";

const input = read(join(import.meta.dir, "input.txt"));

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
  console.log("Parsing", input);
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

async function main1() {
  const possibleGames: Game[] = [];
  const checkIfGamePossible = isGamePossible(new Handful(12, 13, 14));
  for await (const line of input) {
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

async function main2() {
  let result = 0;
  for await (const line of input) {
    const game = parse(line);
    const min = minPossibleSet(game);
    result += min.power();
  }
  return result;
}

main2().then(console.log);
