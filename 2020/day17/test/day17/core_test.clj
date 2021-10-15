(ns day17.core-test
  (:require [clojure.test :refer :all]
            [day17.core :refer :all]))

(deftest around-test
  (let [r (around '(0 0 0))]
    (is (= 26 (count (set r))))))

(deftest new-born?-test
  (is (not (new-born? '(0 0 0) (set ['(0 0 0)]))))
  (is (not (new-born? '(0 0 0) (set ['(1 1 1) '(10 10 10) '(5 3 2)]))))
  (is (new-born? '(0 0 0) (set ['(1 1 1) '(-1 0 1) '(1 0 1)]))))
