(ns day10.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def input
  (->> "input.txt"
       (io/resource)
       slurp
       (string/split-lines)
       (map #(Integer/parseInt %))
       (vec)
       sort))

(defn find-diffs [input]
  (loop [input (concat [0] input [(+ 3 (last input))])
         result []]
    (if (< (count input) 2) result
                            (recur (rest input) (conj result (- (second input) (first input)))))))

(defn task-1 []
  (->> input
       (find-diffs)
       (group-by identity)
       (map #(vector (key %) (count (val %))))))

(defn walk-choices
  ([input] (walk-choices [0] input))
  ([looked remains]
   (let [next-jolt (first remains)
         nnext-jolt (second remains)
         cur-jolt (last looked)]
     (cond (nil? nnext-jolt) 1
           (<= (- nnext-jolt cur-jolt) 3)
           (+
             (walk-choices looked (rest remains))
             (walk-choices (conj looked next-jolt) (rest remains)))
           :else (walk-choices (conj looked next-jolt) (rest remains))))))

(defn partition-input [input]
  (loop [input input
         prev 0
         result [[]]]
    (let [current (first input)]
      (if (nil? current) result
        (if (= 3 (- current prev))
          (recur (rest input) current (conj result [current]))
          (recur (rest input) current (update result (dec (count result)) conj current)))))))

(defn walk-with-split [input]
  (let [p (partition-input input)
        res (map walk-choices p)]
    (reduce * 1 res)))

(defn task-2 []
  (->> input
       walk-with-split))

(task-2)
