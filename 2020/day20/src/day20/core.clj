(ns day20.core
  (:require [clojure.java.io :as io]))

(defn parse-header [header]
  (-> (re-find #"\d+" header)
      (Integer/parseInt)))

(defn parse-body [body]
  (set
    (for [i (range (count body))
          j (range (count (nth body i)))
          :when (= \# (nth (nth body i) j))]
      (list i j))))


(defn parse-tile [[header & body]]
  (list
    (parse-header header)
    (parse-body body)))

(def input
  (->> "input.txt"
       (io/resource)
       (slurp)
       (clojure.string/split-lines)
       (partition-by empty?)
       (filter #(not= % '("")))
       (mapcat parse-tile)
       (apply hash-map)))

