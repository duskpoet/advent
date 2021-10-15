(ns day13.core-test
  (:require [clojure.test :refer :all]
            [day13.core :refer :all]))

(def sample "7,13,x,x,59,x,31,19")

(deftest a-test
  (is (validate-t 1068781 (extract-buses sample))))
