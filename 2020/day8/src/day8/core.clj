(ns day8.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def input-resource (io/resource "input.txt"))

(defn process-line [line]
  (let [[command num] (string/split line #"\s")]
    (list (keyword command) (Integer/parseInt num))))

(def input
 (->> input-resource
      slurp
      string/split-lines
      (map process-line)
      (vec)))

(defn find-loop [input]
  (loop [acc 0
         cur 0
         visited #{}]
    (let [[cmd num] (nth input cur)
          new-state
          (case cmd
            :acc (list (+ acc num) (inc cur) (conj visited cur))
            :jmp (list acc (+ cur num) (conj visited cur))
            :nop (list acc (inc cur) (conj visited cur)))]
      (cond (visited cur) (list :loop acc)
            (= (inc cur) (count input)) (list :done (nth new-state 0))
            :else (recur (nth new-state 0) (nth new-state 1) (nth new-state 2))))))

(defn task-1 []
  (find-loop input))

(defn task-2 []
  (loop [cur 0]
    (let [[cmd num] (nth input cur)]
      (case cmd 
        :nop
        (let [[res acc] (find-loop (assoc input cur (list :jmp num)))]
          (if (= res :done) acc
              (recur (inc cur))))
        :jmp
        (let [[res acc] (find-loop (assoc input cur (list :nop num)))]
          (if (= res :done) acc
              (recur (inc cur))))
        (recur (inc cur))))))

(task-2)