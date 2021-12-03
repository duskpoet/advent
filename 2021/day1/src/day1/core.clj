(ns day1.core
  (:require [clojure.java.io :as io]))

(def input (io/resource "input.txt"))

(def input-seq (-> input io/reader line-seq))

(def input-as-num-seq (map #(java.lang.Integer/parseInt %) input-seq))

(second input-as-num-seq)

(defn count-incs
  ([seq] (count-incs seq 0))
  ([seq acc]
   (if (empty? seq) acc
       (let [[f & rest] seq]
         (if (empty? rest) acc
             (count-incs rest
                         (if (> (first rest) f) (inc acc) acc)))))))

(defn sum [v] (apply + v))

(defn count-triple-incs
  ([seq] (count-triple-incs seq 0))
  ([seq acc]
   (let [f (take 3 seq)
         r (rest seq)
         s (take 3 r)]
     (if
      (< (count s) 3)
      acc
      (count-triple-incs r (if (> (sum s) (sum f)) (inc acc) acc))))))

(defn task-1 [] (count-incs input-as-num-seq))
(defn task-2 [] (count-triple-incs input-as-num-seq))
