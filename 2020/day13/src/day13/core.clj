(ns day13.core
  (:require [clojure.java.io :as io]))

(def input
  (-> "input.txt"
      (io/resource)
      slurp
      (clojure.string/split-lines)))

(def arrival (Integer/parseInt (first input)))

(defn extract-buses [input]
  (->> (clojure.string/split input #",")
       (map #(if (= "x" %) % (Integer/parseInt %)))))

(def buses
  (extract-buses (second input)))

(def active-buses (vec (filter number? buses)))

(defn task-1 []
  (transduce
    (comp (filter number?)
          (map #(hash-map % (* % (Math/ceil (/ arrival %))))))
    (fn
      ([] {0 Long/MAX_VALUE})
      ([val] val)
      ([acc val]
       (let [[b1 v1] (first acc)
             [b2 v2] (first val)]
         (if (> v1 v2) {b2 v2} {b1 v1}))))
    buses))

(def largest (bigint (apply max active-buses)))

(defn buses-map [buses]
  (into
      {}
      (comp
        (map-indexed (fn [idx v] (if (number? v) [v idx])))
        (filter identity))
      buses))
(def b-map (buses-map buses))

(defn validate-t [t buses]
  (every?
    (fn [[id idx]] (zero? (rem (+ t idx) id)))
    (buses-map buses)))

(defn mul-coefs [coefs]
  (->> coefs
    (map-indexed #(apply * %2 (subvec active-buses 0 (inc %1))))
    (apply +)))

(assert (= (mul-coefs [1]) 17))

(defn calc-n-coef [coef]
  (let [cur (nth active-buses (inc (count coef)))]
    (loop [idx 0]
      (let [t (+ (mul-coefs (conj coef idx)) (b-map cur))]
        (if (and
              (>= t cur)
              (zero? (rem t cur)))
          (conj coef idx)
          (recur (inc idx)))))))

(defn task-2 []
  (loop [idx 0
         coef []]
    (println coef)
    (if (= (inc idx) (count active-buses)) (mul-coefs coef)
      (recur (inc idx) (calc-n-coef coef)))))


