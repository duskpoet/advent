(ns day2.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def input (-> "input.txt" io/resource io/reader line-seq))

(def parsed-in
  (map
    (fn [s]
     (let [[c v] (string/split s #"\s")]
       (list c (java.lang.Integer/parseInt v))))
    input))

(defn sum [v1 v2]
  (map-indexed (fn [idx v] (+ v (nth v2 idx)))) v1)

(defn process [command pos]
  (let [[c v] command
        [x y] pos]
       (case c 
         "forward" (list (+ x v) y)
         "up" (list x (- y v))
         "down" (list x (+ y v)))))

(defn travers [seq processor]
 (loop [pos (list 0 0 0)
        path seq]
       (if (empty? path) pos
        (recur (processor (first path) pos) (rest path)))))

(defn process-with-aim [command pos]
 (let [[c v] command
       [x y aim] pos]
    (case c
      "forward" (list (+ v x) (+ y (* aim v)) aim)
      "up" (list x y (- aim v))
      "down" (list x y (+ aim v)))))

(defn task-1 []
 (apply * (travers parsed-in process)))

(defn task-2 []
  (let [[x y] (travers parsed-in process-with-aim)]
    (* x y)))
