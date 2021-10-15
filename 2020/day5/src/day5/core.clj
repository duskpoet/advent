(ns day5.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string])
  (:gen-class))

(def input-file (io/resource "input.txt"))

(def TRANSLATE
  {\F 0
   \B 1
   \L 0
   \R 1})

(defn str-to-bin [word]
  (map TRANSLATE word))

(defn bin-to-dec [seq-num]
  (let [l (count seq-num)]
    (int
      (apply +
        (map-indexed #(* %2 (Math/pow 2 (- l %1 1))) seq-num)))))
(def xp
  (comp
   (map str-to-bin)
   (map bin-to-dec)))

(defn process-input [input]
  (into [] xp input))

(def input
  (-> input-file
      slurp
      string/split-lines
      process-input))

(defn find-missing [input-seq]
  (loop [input input-seq]
    (cond (< (count input) 2) nil
          (= (second input) (inc (first input))) (recur (rest input))
          :else (inc (first input)))))

(defn task2 []
  (-> input
      sort
      find-missing))
   

(defn -main [& args]
 (task2))