(ns day12.core-test
  (:require [clojure.test :refer :all]
            [day12.core :refer :all]))

(def m1 [[1 2 3] [4 5 6]])
(def m2 [[7 8] [9 10] [11 12]])
(def expected-matrix [[58 64] [139 154]])

(deftest mul-matrix-test
  (is (= expected-matrix (mul-matrix m1 m2))))
