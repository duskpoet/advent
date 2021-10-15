(ns day17.core
  (:require [clojure.java.io :as io]
            [clojure.set]))

(def input-raw
  (-> "input.txt"
      io/resource
      slurp
      clojure.string/split-lines))

(def input
  (set
    (for [i (range (count input-raw))
          j (range (count (nth input-raw i)))
          :when (= \# (get-in input-raw [i j]))]
      (list j i 0))))

(defn around [[x y z]]
  (for [i [-1 0 1]
        j [-1 0 1]
        k [-1 0 1]
        :when (not= 0 i j k)]
    (list (+ x i) (+ y j) (+ z k))))

(defn new-born? [p s]
  (if (s p) false
    (let [around-p (set (around p))]
      (= 3 (count (clojure.set/intersection around-p s))))))

(defn round [input]
  (into
    #{}
    (mapcat
      (fn [p]
        (let [around-p (set (around p))]
          (concat
            (if (<= 2 (count (clojure.set/intersection around-p input)) 3)
              [p]
              [])
            (filter #(new-born? % input) around-p)))))
    input))


(defn task-1 []
  (count
    (loop [i 0
           result input]
      (if (= 6 i) result
        (recur (inc i) (round result))))))

(defn around-2 [[x y z w]]
  (for [i [-1 0 1]
        j [-1 0 1]
        k [-1 0 1]
        l [-1 0 1]
        :when (not= 0 i j k l)]
    (list (+ x i) (+ y j) (+ z k) (+ w l))))

(defn new-born-2? [p s]
  (if (s p) false
    (let [around-p (set (around-2 p))]
      (= 3 (count (clojure.set/intersection around-p s))))))


(def input-2
  (set
    (for [i (range (count input-raw))
          j (range (count (nth input-raw i)))
          :when (= \# (get-in input-raw [i j]))]
      (list j i 0 0))))

(defn round-2 [input]
  (into
    #{}
    (mapcat
      (fn [p]
        (let [around-p (set (around-2 p))]
          (concat
            (if (<= 2 (count (clojure.set/intersection around-p input)) 3)
              [p]
              [])
            (filter #(new-born-2? % input) around-p)))))
    input))

(defn task-2 []
  (count
    (loop [i 0
           result input-2]
      (if (= 6 i) result
        (recur (inc i) (round-2 result))))))

