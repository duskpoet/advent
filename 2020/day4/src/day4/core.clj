(ns day4.core
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split-lines split]]
            [clojure.spec.alpha :as s]))

(def input-file (io/resource "input.txt"))

(defn to-pair [word]
  (let [[k v] (split word #":")]
    [(keyword k) v]))

(defn get-entries [line]
  (let [words (split line #"\s")]
    (mapcat to-pair words)))

(defn convert-lines [lines]
  (apply hash-map (mapcat get-entries lines)))

(def xprocess
 (comp (partition-by #(not= "" %))
       (filter #(not= '("") %))
       (map convert-lines)))

(defn process-input [lines]
  (into [] xprocess lines))

(def input-lines
  (-> input-file
      slurp
      split-lines))

(def input (process-input input-lines))

(s/def ::to-int (s/conformer #(Integer/parseInt %)))
(s/def ::dim-val (s/and 
                  (s/conformer #(re-find #"(\d+)(\w+)" %))
                  (s/cat :_ any? :num ::to-int :dim string?)))

(defmulti height :dim)
(defmethod height "cm" [_]
 #(<= 150 (:num %) 193))
(defmethod height "in" [_]
  #(<= 59 (:num %) 76))
 
(s/def ::byr (s/and ::to-int #(<= 1920 % 2002)))
(s/def ::iyr (s/and ::to-int #(<= 2010 % 2020)))
(s/def ::eyr (s/and ::to-int #(<= 2020 % 2030)))
(s/def ::hgt (s/and 
              ::dim-val
              (s/multi-spec height :dim)))
(s/def ::hcl #(re-matches #"#[0-9a-f]{6}" %))
(s/def ::ecl #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"})
(s/def ::pid #(re-matches #"\d{9}" %))
(s/def ::cid string?)

(s/def ::passport (s/keys :req-un [::byr ::iyr ::eyr ::hgt ::hcl ::ecl ::pid]
                          :opt-un [::cid]))

(defn task []
  (->> input
       (filter #(s/valid? ::passport %))
       (count)))
