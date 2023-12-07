import { split } from "../utils/read";

const CARDS = [
  "A",
  "K",
  "Q",
  "J",
  "T",
  "9",
  "8",
  "7",
  "6",
  "5",
  "4",
  "3",
  "2",
] as const;

type Card = (typeof CARDS)[number];

type Combination = {
  name: string;
  match: (hand: [Card, number][]) => boolean;
  rank: number;
};

const Combinations: Combination[] = (
  [
    { name: "5 of a kind", match: (hand) => hand[0][1] === 5 },
    { name: "4 of a kind", match: (hand) => hand[0][1] === 4 },
    {
      name: "full house",
      match: (hand) => hand[0][1] === 3 && hand[1][1] === 2,
    },
    { name: "three of a kind", match: (hand) => hand[0][1] === 3 },
    {
      name: "two pairs",
      match: (hand) => hand[0][1] === 2 && hand[1][1] === 2,
    },
    { name: "one pair", match: (hand) => hand[0][1] === 2 },
    { name: "high card", match: () => true },
  ] satisfies Omit<Combination, "rank">[]
).map((combination, index, arr) => ({
  ...combination,
  rank: arr.length - index,
}));

const RANKS = Object.fromEntries(
  CARDS.map((card, index) => [card, CARDS.length - index])
) as Record<Card, number>;

class Hand {
  qNotation: [Card, number][];
  constructor(public cards: Card[]) {
    const count = {} as Record<Card, number>;
    for (const card of cards) {
      if (!count[card]) count[card] = 0;
      count[card]++;
    }
    this.qNotation = Object.entries(count).sort((a, b) => b[1] - a[1]) as [
      Card,
      number
    ][];
  }
}

function whoWins(a: Hand, b: Hand) {
  const aCombination = Combinations.find((combination) =>
    combination.match(a.qNotation)
  )!;
  const bCombination = Combinations.find((combination) =>
    combination.match(b.qNotation)
  )!;

  if (aCombination.rank > bCombination.rank) return 1;
  if (aCombination.rank < bCombination.rank) return -1;
  for (let i = 0; i < a.cards.length; i++) {
    const aCard = a.cards[i];
    const bCard = b.cards[i];
    if (RANKS[aCard] > RANKS[bCard]) return 1;
    if (RANKS[aCard] < RANKS[bCard]) return -1;
  }
  return 0;
}

class HandAndBid {
  constructor(public hand: Hand, public bid: number) {}
}

export function solution(input: string) {
  const lines = split(input);
  const hands: HandAndBid[] = [];
  for (const line of lines) {
    const parts = line.split(" ");
    hands.push(
      new HandAndBid(new Hand(parts[0].split("") as Card[]), parseInt(parts[1]))
    );
  }

  hands.sort((a, b) => whoWins(a.hand, b.hand));
  return hands.reduce((acc, handAndBid, idx) => {
    return acc + handAndBid.bid * (idx + 1);
  }, 0);
}
