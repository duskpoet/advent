import { Challenge } from "./utils/challenge";

const { argv } = process;

let runTests = false;

let idx = 3;
while (idx < argv.length) {
  if (argv[idx] === "--help") {
    console.log(
      `To run a solution, use the following syntax:
bun run cli.ts day2
To run tests:
bun run cli.ts day2 --test
`
    );
    process.exit(0);
  }
  if (argv[idx] === "--test") {
    runTests = true;
  }
  idx++;
}

const day = argv[2];
import(`./${day}/index.ts`).then(
  (module) => {
    const c = module.default;
    if (c instanceof Challenge) {
      if (runTests) {
        c.runTests();
      } else {
        c.run();
      }
    } else {
      console.error("This module is not a Challenge");
    }
  },
  (err) => console.error("Didn't find this solution", err)
);
