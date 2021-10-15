(ns day11.core
  (:require [clojure.java.io :as io]))

(defn process-input [input]
  (loop [r 0
         c 0
         result {}]
    (cond (= r (count input)) result
          (= c (count (nth input r))) (recur (inc r) 0 result)
          :else (recur r (inc c) (assoc result (list r c) (get-in input [r c]))))))

(def input
  (->> "input.txt"
       (io/resource)
       slurp
       (clojure.string/split-lines)
       (process-input)))

(defn get-surrounds [[r c]]
  (for [r+ '(-1 0 1)
        c+ '(-1 0 1)
        :when (not= 0 r+ c+)]
    (list (+ r r+) (+ c c+))))

(defn map-seat [input [pos v]]
  (let [surrounded (get-surrounds pos)
        vals (map input surrounded)
        occupied (filter #(= \# %) vals)]
    [
     pos
     (case v
       \L (if (empty? occupied) \# \L)
       \# (if (>= (count occupied) 4) \L \#)
       \.)]))

(defn round-1 [input]
  (into {} (map #(map-seat input %)) input))

(defn process-till-settles [round-fn]
  (let [result
        (loop [input input]
          (let [next-input (round-fn input)]
            (if (= next-input input) input
              (recur next-input))))]
    (count (filter #(= \# (val %)) result))))

(defn task-1 []
  (process-till-settles round-1))

(defn get-surrounds-2 [input [r c]]
  (for [r+ '(-1 0 1)
        c+ '(-1 0 1)
        :when (not= 0 r+ c+)]
    (loop [[r c] (list r c)]
      (let [next-pos (list (+ r r+) (+ c c+))
            v (input next-pos)]
        (if (= \. v) (recur next-pos)
          next-pos)))))


(defn map-seat-2 [input [pos v]]
  (let [surrounded (get-surrounds-2 input pos)
        vals (map input surrounded)
        occupied (filter #(= \# %) vals)]
    [
     pos
     (case v
       \L (if (empty? occupied) \# \L)
       \# (if (>= (count occupied) 5) \L \#)
       \.)]))

(defn round-2 [input]
  (into {} (map #(map-seat-2 input %)) input))

(defn task-2 []
  (process-till-settles round-2))

