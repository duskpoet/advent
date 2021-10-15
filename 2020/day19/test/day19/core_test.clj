(ns day19.core-test
  (:require [clojure.test :refer :all]
            [day19.core :refer :all]
            [clojure.java.io :as io]))

(def rules-test
  {0 '((4 1 5))
   1 '((2 3) (3 2))
   2 '((4 4) (5 5))
   3 '((4 5) (5 4))
   4 "a"
   5 "b"})

(deftest match-word-test
  (is (not
        (nil? (match-word rules-test (sp "aaaabb") (rules-test 0))))))

(def rules-2 (-> "input-test.txt"
                  (io/resource)
                  (slurp)
                  (clojure.string/split-lines)
                  (parse-rules)))

(def rules-2-new (assoc rules-2
                  8 '((42 8) (42))
                  11 '((42 11 31) (42 31))))



(deftest second-task
      (is
        (has-answer
          (match-word
            rules-2
            (sp "bbabbbbaabaabba")
            (rules 0))))
      (is
        (not= '(())
          (match-word
            rules-2
            (sp "abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa")
            (rules 0))))
      (is
        (has-answer
          (match-word
            rules-2-new
            (sp "aaabbbbbbaaaabaababaabababbabaaabbababababaaa")
            (rules 0))))
      (is
        (has-answer
          (match-word
            rules-2-new
            (sp "bbabbbbaabaabba")
            (rules 0))))
      (is
        (has-answer
          (match-word
            rules-2-new
            (sp "aaaaabbaabaaaaababaa")

            (rules 0)))))
