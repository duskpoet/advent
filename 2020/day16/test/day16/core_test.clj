(ns day16.core-test
  (:require [clojure.test :refer :all]
            [day16.core :refer :all]))

(deftest matches-field-test
  (is (matches-field 100 (first {"test" [20 40 100 102]}))))


