(ns day18.core
  (:require [clojure.java.io :as io]))

(defn parse-list [l]
  (cond
    (number? l) l
    (= 1 (count l)) (parse-list (first l))
    :else (let [size  (count l)
                op1 (last l)
                oper (nth l (- size 2))
                rest (drop-last 2 l)]
           (list oper (parse-list op1) (parse-list rest)))))

(defn wrap-par [s]
  (str "(" s ")"))

(def input-raw
  (->> "input.txt"
      (io/resource)
      (slurp)
      (clojure.string/split-lines)))

(def input-1
  (->> input-raw
      (map (comp parse-list read-string wrap-par))))

(defn calculus [l]
  (cond
    (number? l) l
    (= 1 (count l)) (calculus (first l))
    :else (let [size (count l)
                [head [op1 oper op2]] (split-at (- size 3) l)]
            (if (= oper '+)
              (calculus (concat head [(+ (calculus op1) (calculus op2))]))
              (* (calculus op2) (calculus (concat head [(calculus op1)])))))))

(defn parse-list-2 [l]
  (cond
    (number? l) l
    (= 1 (count l)) (parse-list-2 (first l))
    :else (let [size (count l)
                [head [op1 oper op2]] (split-at (- size 3) l)]
            (list oper (parse-list op1) (parse-list rest)))))


(def input-2
  (->> input-raw
       (map (comp calculus read-string wrap-par))))

(defn task-1 []
  (->> input-1
       (map eval)
       (apply +)))

(defn task-2 []
  (->> input-2
       (apply +)))
