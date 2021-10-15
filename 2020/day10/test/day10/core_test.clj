(ns day10.core-test
  (:require [clojure.test :refer :all]
            [day10.core :refer :all]
            [clojure.java.io :as io]))

(def sample (sort [16 10 15 5 1 11 7 19 6 12 4]))

(deftest walk-choices-test
  (is (= 8 (walk-choices sample)))
  (is (= 8 (walk-with-split sample))))

(def input-test
  (->> "test.txt"
      (io/resource)
      (slurp)
      (clojure.string/split-lines)
      (map #(Integer/parseInt %))
      (sort)))

(deftest second-sample-test
  (is (= 19208 (walk-choices input-test)))
  (is (= 19208 (walk-with-split input-test))))