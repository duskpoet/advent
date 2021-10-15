(ns day2.core
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split-lines]]))

(def input-file (io/resource "input.txt"))

(defn parse-input [line]
  (let [match (re-find #"(\d+)-(\d+) (\w): (\w+)" line)]
    {:left (Integer/parseInt (nth match 1))
     :right (Integer/parseInt (nth match 2))
     :match (nth (nth match 3) 0)
     :word (nth match 4)}))

(def input
  (->> input-file
       slurp
       split-lines
       (map parse-input)))

(defn count-sym [s word]
 (loop [word word
        result 0]
  (if (empty? word) result
    (let [[f & rest] word]
     (recur rest (+ result (if (= f s) 1 0)))))))

(defn password-valid? [{:keys [left right match word]}]
  (<= left (count-sym match word) right))

(defn sym-match? [s1 s2]
 (if (= s1 s2) 1 0))

(defn safe-sym-get [word pos]
  (if (>= pos (count word)) nil (nth word pos)))

(defn new-policy-valid? [{:keys [left right match word]}]
  (let [c-left (safe-sym-get word (dec left))
        c-right (safe-sym-get word (dec right))]
    (pos? (bit-xor (sym-match? c-left match) (sym-match? c-right match)))))

(defn main []
 (->> input
   (filter new-policy-valid?)
   count))