(ns day7.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.spec.alpha :as spec]))

(spec/check-asserts true)

(spec/def ::color
  (spec/and string? #(re-matches #"\w+ \w+" %)))

(spec/def ::amount number?)

(spec/def ::color-with-amount
  (spec/keys :req-un [::color ::amount]))

(spec/def ::color-line
  (spec/cat
    :outer ::color
    :inside (spec/* ::color-with-amount)))

(def input-file (io/resource "input.txt"))

(defn match-color-line [line]
  (let [[_ amount color] (re-find #"(\d+) (\w+ \w+)" line)]
    {:amount (Integer/parseInt amount) :color color}))

(defn process-input [line]
  (let [[out-bag in-bags] (string/split line #" contain ")
        in-bags-split (string/split in-bags #", ")
        empty-bag? (string/starts-with? in-bags "no other")]

    (spec/assert
      ::color-line
      (apply vector
        (re-find #"\w+ \w+" out-bag)
        (if empty-bag? [] (map match-color-line in-bags-split))))))

(spec/fdef process-input
 :args (spec/cat :line string?)
 :ret ::color-line)

(def input
 (->> input-file
      slurp
      string/split-lines
      (map process-input)))

(defn to-bin [b] (if b 1 0))

(defn has-color [colors color]
  (some #(= color (:color %)) colors))

(defn find-color-bags [color]
  (into
    []
    (comp (filter #(has-color % color))
          (map first))
    input))

(defn trace-bags [[f & r]]
  (let [found-colors (find-color-bags f)]
    (if (empty? found-colors) [(concat [f] r)]
      (mapcat #(trace-bags (concat [% f] r)) found-colors))))

(defn count-bags [bags-chains]
  (loop [[f & r] bags-chains
         result {}]
    (if (nil? f) result 
      (recur r 
        (update
          result
          (count f)
          #(if (nil? %) #{(first f)} (conj % (first f))))))))

(defn task-1 
 "Find shiny-bags"
 []
 (->> ["shiny gold"]
      trace-bags
      (mapcat identity)
      set
      count
      dec))


(def input-as-map
  (apply hash-map (mapcat #(vector (first %) (rest %)) input)))

(defn count-bags-deep [color]
  (let [bags (input-as-map color)]
    (apply +
     (for [b bags]
      (+ 
        (:amount b)
        (* (:amount b) (count-bags-deep (:color b))))))))
 
(defn task-2 []
  (count-bags-deep "shiny gold"))

(task-2)