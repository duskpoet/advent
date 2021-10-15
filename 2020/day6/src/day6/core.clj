(ns day6.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set]))

(def input-file (io/resource "input.txt"))

(def split-lines
  (mapcat #(string/split % #"")))

(defn as-set [lines]
  (into #{} split-lines lines))
    

(def xf
 (comp
    (map string/split-lines)
    (map as-set)))
 

(defn process-input [input]
  (into [] xf input))

(def input
  (-> input-file
      slurp
      (string/split #"\n\n")))

 
(defn task1 []
  (apply + (map count (process-input input))))

(def xf-2
 (comp 
  (map string/split-lines)
  (map (fn [words] (map #(string/split % #"") words)))
  (map (fn [words] (map set words)))))

(defn process-input-2 [input]
  (into [] xf-2 input))

(defn intersect [words]
  (apply clojure.set/intersection words))

(defn task2 []
  (->> input
      process-input-2
      (map intersect)
      (map count)
      (apply +)))

(task2)