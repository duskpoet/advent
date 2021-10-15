(ns day9.core-test
  (:require [clojure.test :as test]
            [day9.core :refer :all]
            [clojure.spec.test.alpha :as stest]))

(stest/instrument)
(test/deftest calc-sums-test
  (test/is (= (seq [#{} #{3} #{5}]) (calc-sums [1 2 3] 1)))
