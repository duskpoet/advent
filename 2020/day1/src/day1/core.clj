(ns day1.core
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split-lines]]))

(def TARGET_SUM 2020)
(def input-file (io/resource "input.txt"))

(def input
  (->> input-file
       slurp
       split-lines
       (map #(Integer/parseInt %))))

(defn get-seq [input]
  (->> input
     (apply sorted-set)))

(def iterations (atom 0))

(defn find-match-sum [input target]
  (swap! iterations inc)
  (if (< (count input) 2) nil
       (let [f (first input)
             l (last input)
             sum (+ f l)]
        (cond
            (< sum target) (recur (disj input f) target)
            (> sum target) (recur (disj input l) target)
            :else (list f l)))))

(defn find-match-sum3 [input target]
  (loop [input input]
     (if (empty? input) nil
           (let [i (last input)
                 rest (disj input i)
                 found (find-match-sum rest (- target i))]
             (if found (conj found i)
               (recur rest))))))

(defn main []
  (reset! iterations 0)
  (println (-> input
                get-seq
                (find-match-sum3 TARGET_SUM)))
  (println "Iterations:" @iterations))