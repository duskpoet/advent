import { Challenge } from "./utils/challenge";

const { argv } = process;

console.log(argv);
const day = argv[2];

import(`./${day}/index.ts`).then(
  (module) => {
    if (module.default instanceof Challenge) {
      module.default.run();
    } else {
      console.error("This module is not a Challenge");
    }
  },
  (err) => console.error("Didn't find this solution", err)
);
