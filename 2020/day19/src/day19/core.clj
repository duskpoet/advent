(ns day19.core
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as s]))

(s/check-asserts true)

(def input
  (->> "input.txt"
       io/resource
       slurp
       clojure.string/split-lines
       (split-with not-empty)))

(defn parse-rule-definition [df]
  (if-let [m (re-find #"\"(\w+)\"" df)]
    (second m)
    (map
       (fn [l] (map #(Integer/parseInt %) (clojure.string/split l #"\s")))
       (clojure.string/split df #" \| "))))

(defn parse-rule [l]
  (let [[num df] (clojure.string/split l #": ")]
    [(Integer/parseInt num) (parse-rule-definition df)]))

(defn parse-rules [rules]
  (into
    {}
    (map parse-rule)
    rules))

(def rules (parse-rules (first input)))

(def words (rest (second input)))

(defn multiply-rules
  ([r] r)
  ([words1 words2] (for [w1 words1
                         w2 words2]
                     (str w1 w2)))
  ([w1 w2 & rest] (apply multiply-rules
                         (multiply-rules w1 w2)
                         rest)))


(defn conjoin-rules [rules rule]
  (let [expand-rules
        (fn [rule]
          (apply
            multiply-rules
            (for [r rule] (conjoin-rules rules (rules r)))))
        or-rules
        (fn [rule]
          (apply
            concat
            (for [r rule]
              (expand-rules r))))]

    (if (string? rule)
      [rule]
      (or-rules rule))))

(s/def ::seq-rule (s/coll-of number?))

(s/def ::rule
  (s/or :seq-rules (s/coll-of ::seq-rule)
        :string-rule string?))

(s/def ::rules (s/map-of number? ::rule))

(s/def ::word (s/coll-of string?))

(s/def ::words (s/coll-of ::word))

(defn match-word [rules words rule]
  (s/assert ::rules rules)
  (s/assert ::words words)
  (s/assert ::rule rule)
  (let [match-expand
        (fn [words rule]
          (cond
            (empty? rule) words
            (empty? words) []
            :else
            (recur
              (match-word rules words (rules (first rule)))
              (rest rule))))]
    (cond
      (string? rule)
      (for [word words
            :let [[w] word]
            :when (= w rule)]
        (rest word))
      :else
      (mapcat
        (fn [rule-seq]
          (match-expand words rule-seq))
        rule))))


      ;(loop [rule rule]
      ;  (if (empty? rule)
      ;    nil
      ;    (let [r (first rule)
      ;          result (match-expand word r)]
      ;      (if (nil? result) (recur (rest rule)) result)))))))

(defn sp [w] [(clojure.string/split w #"")])

(defn has-answer [s]
  (some empty? s))

(defn task-1 []
  (->> words
    (map
      #(match-word rules (sp %) (rules 0)))
    (filter #(= '(()) %))
    (count)))

(def new-rules
  (assoc rules
    8 '((42 8) (42))
    11 '((42 11 31) (42 31))))

(defn task-2 []
  (->> words
    (map
      #(match-word new-rules (sp %) (new-rules 0)))
    (filter has-answer)
    (count)))

(defn reduce-rules [rules]
  (let [word-rules (filter #(string? (val %)) rules)]
    word-rules))

