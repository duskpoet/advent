(ns day2.core-test
  (:require [clojure.test :refer :all]
            [day2.core :refer :all]))


(deftest a-test
  (testing "count-sym"
   (is (= 3 (count-sym \a "avdfafda"))))
  (testing "new-policy-valid"
    (is (new-policy-valid? {:left 1 :right 3 :match \a :word "abcde"})))
  (testing "safe-sym-get"
    (is (= \c (safe-sym-get "abc" 2)))
    (is (nil? (safe-sym-get "abc" 3))))
  (testing "sym-match"
    (is (= 1 (sym-match? \a \a)))
    (is (= 0 (sym-match? \a nil)))))
