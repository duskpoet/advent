(ns day1.core-test
  (:require [clojure.test :refer :all]
            [day1.core :refer :all]))

(deftest a-test
  (testing "get-seq"
    (is (= (get-seq [1 2 3 2020]) (sorted-set 1 2 3))))
  (testing "find-match-sum"
    (is (= '(20 2000) (find-match-sum (sorted-set 1 2 3 20 2000))))))
