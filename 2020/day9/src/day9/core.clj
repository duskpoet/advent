(ns day9.core
  (:require [clojure.java.io]
            [clojure.string]))

(def input
  (->> "input.txt"
       (clojure.java.io/resource)
       slurp
       (clojure.string/split-lines)
       (map #(Long/parseLong %))
       vec))

(def PREAMBULE 25)

(defn calc-sums [input preambule]
  (vec (map-indexed
         (fn [i num]
           (-> (partial + num)
               (map (subvec input (max 0 (- i preambule)) i))
               (set)))
         input)))

(defn task-1 []
  (let [sums (calc-sums input PREAMBULE)]
    (loop [i PREAMBULE]
      (cond
        (= i (count input)) nil
        (some #(% (nth input i)) (subvec sums (- i PREAMBULE) i)) (recur (inc i))
        :else (nth input i)))))

(defn find-fix []
  (let [invalid (task-1)]
    (loop [input input
           i 2]
      (let [slice (subvec input 0 i)
            sum (apply + slice)]
        (cond (= sum invalid) slice
              (< sum invalid) (recur input (inc i))
              :else (recur (subvec input 1) 2))))))

(defn task-2 []
  (-> (find-fix)
      (sort)))
