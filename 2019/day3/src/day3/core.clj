(ns day3.core
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split split-lines join]]))

(def input-file (io/resource "input.txt"))

(def input (slurp input-file))

(def raw-wires (split-lines input))

(defn parse-raw-wire [wire-in]
  (let [splitted (split wire-in #",")]
    (loop [res [[0 0]]
           input splitted]
      (if (empty? input) res
        (let [item-in (first input)
              [x y] (last res)
              [dir & steps-str] (split item-in #"")
              steps (-> steps-str join Integer/parseInt)
              res-point (case dir
                          "U" [x (+ y steps)]
                          "R" [(+ x steps) y]
                          "D" [x (- y steps)]
                          "L" [(- x steps) y])]
          (recur
            (conj res res-point)
            (rest input)))))))

(def first-wire (parse-raw-wire (first raw-wires)))
(def second-wire (parse-raw-wire (second raw-wires)))
