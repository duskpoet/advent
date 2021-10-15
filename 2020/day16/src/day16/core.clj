(ns day16.core
  (:require [clojure.java.io :as io]))

(def input-raw
  (-> "input.txt"
      (io/resource)
      slurp
      (clojure.string/split-lines)))

(def fields (volatile! {}))

(def your (volatile! []))
(def nearby (volatile! []))

(defn parse-tickets [line]
  (map #(Integer/parseInt %) (clojure.string/split line #",")))

(defn parse-field [line]
  (let [[_ field from1 to1 from2 to2] (re-find #"([^:]*): (\d+)-(\d+) or (\d+)-(\d+)" line)]
    [field (map #(Integer/parseInt %) [from1 to1 from2 to2])]))

(loop [remains input-raw
       mode :fields]
  (let [cur (first remains)]
    (cond (nil? cur) nil
          (empty? cur) (recur (rest remains) mode)
          (= "your ticket:" cur) (recur (rest remains) :your)
          (= "nearby tickets:" cur) (recur (rest remains) :nearby)
          (= mode :fields) (do (vswap! fields #(apply assoc % (parse-field cur))) (recur (rest remains) mode))
          (= mode :your) (do (vreset! your (parse-tickets cur)) (recur (rest remains) mode))
          (= mode :nearby) (do (vswap! nearby conj (parse-tickets cur)) (recur (rest remains) mode)))))

(defn matches-field [ticket field]
  (let [[from1 to1 from2 to2] (val field)]
    (or (<= from1 ticket to1) (<= from2 ticket to2))))

(defn invalid? [ticket]
  (not (some #(matches-field ticket %) @fields)))

(defn get-invalids [tickets]
  (filter invalid? tickets))

(defn leave-valids [tickets]
  (filter #(not (invalid? %)) tickets))

(defn task-1 []
  (loop [remains @nearby
         invalids []]
    (let [cur (first remains)]
      (cond (nil? cur) (apply + invalids)
            :else (recur (rest remains) (apply conj invalids (get-invalids cur)))))))

(def valid-nearby
  (->> @nearby
       (map leave-valids)))

(def valid-your (leave-valids @your))

(defn valid-fields [i]
  (loop [fields @fields
         remains (conj @nearby @your)]
    (if (empty? remains) fields
      (let [cur (first remains)]
        (recur
          (filter #(or (invalid? (nth cur i)) (matches-field (nth cur i) %)) fields)
          (rest remains))))))

(def places
  (for [i (range (count @your))]
    (valid-fields i)))

(defn places-as-map [places]
  (apply sorted-map
    (mapcat identity (map-indexed #(list (count %2) %1) places))))

(defn find-ones [places]
  (into
    #{}
    (comp (filter #(= 1 (count %)))
          cat
          (map first))
    places))

(defn filter-ones [ones place]
  (if (= 1 (count place)) place
    (filter #(not (ones (first %))) place)))

(def clean-places
  (loop [i 0
         result places]
    (if (= i (count places))
      result
      (let [ones (find-ones result)]
        (recur
          (inc i)
          (map #(filter-ones ones %) result))))))

(def my-departure
  (for [i (range (count @your))
        :let [field (first (nth clean-places i))]
        :when (clojure.string/starts-with? (first field) "departure")]
    (nth @your i)))

(defn task-2 []
  (apply * my-departure))