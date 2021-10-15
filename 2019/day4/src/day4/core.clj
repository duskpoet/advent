(ns day4.core
  (:require [clojure.string :refer [split]]))

(def input "123257-647015")

(def input-range
  (let [input-v (->
                 input
                 (split #"-"))
        input-vn (map #(Integer/parseInt %) input-v)]
    (range (nth input-vn 0) (-> input-vn (nth 1) inc))))

(defn get-fig [num pos]
  (-> num
      (quot (Math/pow 10 pos))
      (rem 10)
      (int)))

(defn matches-criteria [num]
  (loop [idx 1
         found-adj false]
    (if (> (Math/pow 10 idx) num) found-adj
      (let [fig (get-fig num idx)
            prev (get-fig num (dec idx))
            prev-prev (get-fig num (dec (dec idx)))]
        (if
         (or
          (= fig prev prev-prev)
          (> fig prev))
         false
         (recur (inc idx) (or found-adj (= fig prev))))))))
          
               