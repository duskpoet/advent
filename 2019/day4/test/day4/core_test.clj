(ns day4.core-test
  (:require [clojure.test :refer :all]
            [day4.core :refer :all]))

(deftest get-fig-test
    (is (= 3 (get-fig 234 1)))
    (is (= 4 (get-fig 234 0)))
    (is (= 1 (get-fig 123456 5))))

(deftest matches-criteria-test
 (is (= true (matches-criteria 112233)))
 (is (= false (matches-criteria 123444)))
 (is (= true (matches-criteria 123445)))
 (is (= false (matches-criteria 223450)))
 (is (= false (matches-criteria 123789))))

