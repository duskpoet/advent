(ns day3.core
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split-lines]]))

(def input-file (io/resource "input.txt"))

(defn process-trees [input]
  (loop [i 0
         j 0
         result #{}]
    (cond (= i (count input)) result
          (= j (count (nth input i))) (recur (inc i) 0 result)
          :else 
          (let [result (if (= \# (get-in input [i j]))
                        (conj result (list (inc i) (inc j)))
                        result)]
            (recur i (inc j) result)))))

(defn process-input [input]
 {:height (count input)
  :width (count (first input))
  :input input})

(def input
  (-> input-file
      slurp
      split-lines
      process-input))

(defn traverse-map [from-x from-y right down]
  (let [sum-x (+ from-x right)
        sum-y (+ from-y down)]
    (cond (> sum-y (:height input)) :end
          (> sum-x (:width input)) (list (rem sum-x (:width input)) sum-y)
          :else (list sum-x sum-y))))

(defn sig [v] (if v 1 0))

(defn count-trees [slope-x slope-y]
 (loop [x 1
        y 1
        result 0]
  (let [new-coord (traverse-map x y slope-x slope-y)]
    (if (= new-coord :end) result
      (let [[x y] new-coord]
        (recur
          x
          y
          (+ result (sig (= \# (get-in input [:input (dec y) (dec x)]))))))))))
    
(def slopes `((1 1) (3 1) (5 1) (7 1) (1 2)))

(defn main []
  (apply *
    (for [[x y] slopes]
      (count-trees x y))))