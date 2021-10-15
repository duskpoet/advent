(ns day1.core
  (:require [clojure.java.io :as io]))

(def input-file (io/resource "input.txt"))

(defn calc-fuel [input]
  (-> input
      (/ 3)
      Math/floor
      (- 2)
      (max 0)))

(defn calc-total-fuel [mass]
  (loop [result 0
         input mass]
    (if (= input 0) result
        (let [req-fuel (calc-fuel input)]
          (recur (+ result req-fuel) req-fuel)))))

(defn result-fn [acc val]
  (-> val
      Integer/parseInt
      calc-total-fuel
      (+ acc)))


(with-open [f (io/reader input-file)]
  (int (reduce result-fn 0 (line-seq f))))