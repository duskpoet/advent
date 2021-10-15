(ns day4.core-test
  (:require [clojure.test :refer :all]
            [day4.core :as c]
            [clojure.spec.alpha :as s]))

(deftest get-entries-test
  (is (= [:a "1" :b "2"] (c/get-entries "a:1 b:2"))))


(deftest passport-test
  (testing "byr"
    (is (s/valid? ::c/byr "2000"))
    (is (not (s/valid? ::c/byr "2003"))))
  (testing "hgt"
    (is (s/valid? ::c/hgt "60in"))
    (is (s/valid? ::c/hgt "190cm"))
    (is (not (s/valid? ::c/hgt "190in")))
    (is (not (s/valid? ::c/hgt "60"))))
  (testing "hcl"
    (is (s/valid? ::c/hcl "#123abc"))
    (is (not (s/valid? ::c/hcl "#123abz")))
    (is (not (s/valid? ::c/hcl "123abc"))))
  (testing "ecl"
    (is (s/valid? ::c/ecl "brn"))
    (is (not (s/valid? ::c/ecl "wtf"))))
  (testing "pid"
    (is (s/valid? ::c/pid "000000001"))
    (is (not (s/valid? ::c/pid "0123456789")))))