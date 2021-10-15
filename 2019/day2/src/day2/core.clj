(ns day2.core
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split]]))

(def input-file (io/resource "input.txt"))

(def input (slurp input-file))

(def expected 19690720)

(def input-seq
  (let [input-seq-str (split input #",")
        input-seq-int (map #(Integer/parseInt %) input-seq-str)
        input-vec (vec input-seq-int)]
    input-vec))

(defn process-seq [in pos]
  (let [[opc op1 op2 op3] (subvec in pos)
         op (get {1 + 2 *} opc :halt)]
    (if (= op :halt) :halt
      (assoc in op3 (op (in op1) (in op2))))))

(defn calc [input-seq]
  (loop [in input-seq
         pos 0]
    (let [result (process-seq in pos)]
      (if (= result :halt) in
        (recur result (+ pos 4))))))

(defn find-verbs []
  (loop [noun 0
         verb 0]
    (println "Calcing for" noun verb)
    (let [step-input (assoc input-seq 1 noun 2 verb)
          step-result (calc step-input)
          answer (first step-result)]
      (if (= answer expected)
        answer
        (recur
          (+ noun (quot (inc verb) 100))
          (rem (inc verb) 100))))))
