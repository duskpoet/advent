(ns day12.core
  (:require [clojure.java.io :as io]
            [clojure.string]))

(def input
  (->> "input.txt"
       (io/resource)
       (slurp)
       (clojure.string/split-lines)
       (map #(re-find #"(\w)(\d+)" %))
       (map #(list (nth % 1) (Integer/parseInt (nth % 2))))))

(defn rotation-matrix [angle]
  (let [r (Math/toRadians angle)]
    [[(Math/cos r) (- (Math/sin r))]
     [(Math/sin r) (Math/cos r)]]))

(defn get-row [mat i]
  (nth mat i))

(defn get-col [mat j]
  (map #(nth % j) mat))

(defn mul-matrix [m1 m2]
  (let [rows (count m1)
        cols (count (first m2))
        raw (for [i (range rows)
                  j (range cols)]
              (reduce + 0 (map * (get-row m1 i) (get-col m2 j))))]
    (partition cols raw)))

(defn rotate [angle vx vy]
  (apply concat (mul-matrix (rotation-matrix angle) [[vx] [vy]])))

(defn move [[[x y] [vx vy]] [side steps]]
  (case side
    "N" [[x (+ y steps)] [vx vy]]
    "E" [[(+ x steps) y] [vx vy]]
    "S" [[x (- y steps)] [vx vy]]
    "W" [[(- x steps) y] [vx vy]]
    "F" [[(+ x (* vx steps)) (+ y (* vy steps))] [vx vy]]
    "L" [[x y] (rotate steps vx vy)]
    "R" [[x y] (rotate (- steps) vx vy)]))

(defn task-1 []
  (loop [input input
         pos [[0 0] [1 0]]]
    (if (empty? input) pos
      (recur (rest input) (move pos (first input))))))

(defn move-wp [[[x y] [wx wy]] [side steps]]
  (case side
    "N" [[x y] [wx (+ wy steps)]]
    "E" [[x y] [(+ wx steps) wy]]
    "S" [[x y] [wx (- wy steps)]]
    "W" [[x y] [(- wx steps) wy]]
    "F" [[(+ x (* wx steps)) (+ y (* wy steps))] [wx wy]]
    "L" [[x y] (rotate steps wx wy)]
    "R" [[x y] (rotate (- steps) wx wy)]))

(defn task-2 []
  (loop [input input
         pos [[0 0] [10 1]]]
    (if (empty? input) pos
      (recur (rest input) (move-wp pos (first input))))))

