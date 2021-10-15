(ns day1.core-test
  (:require [clojure.test :refer :all]
            [day1.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 50346 (int (calc-total-fuel 100756))))))
