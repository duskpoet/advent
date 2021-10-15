(ns day10.core
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split-lines]]
            [clojure.data.priority-map :refer [priority-map]]))

(def input-file (io/resource "input.txt"))

(def input
  (-> input-file
      slurp
      split-lines))

(def input-data
  (loop [result nil
         y 0
         x 0]
    (cond
      (= y (count input)) result
      (= x (count (get input y))) (recur result (inc y) 0)
      :else
      (let [c (get-in input [y x])
            a (if (= c \#) (list [x y]) nil)]
        (recur
         (concat result a) y (inc x))))))

(defn get-diffs [from to]
  (println "gettin diffs" from to)
  (let [[fX fY] from
        [tX tY] to
        dX (- fX tX)
        dY (- fY tY)]
    [dX dY]))

(defn distance [from to]
  (let [[dX dY] (get-diffs from to)]
    (Math/sqrt (+ (* dX dX) (* dY dY)))))

(defn calc-tan [from to]
  (let [[dX dY] (get-diffs from to)]
    (if (zero? dY) :inf (/ dX dY))))

(defprotocol ISegment
  (tan [s])
  (length [s]))

(defrecord Segment [from to]
  ISegment
  (tan [s] (calc-tan from to))
  (length [s] (distance from to)))

(defn inside-x? [x segment]
  (let [[fX] (:from segment)
        [tX] (:to segment)]
    (if (> fX tX)
      (>= fX x tX)
      (>= tX x fX))))

(defn inside-y? [y segment]
  (let [[_ fY] (:from segment)
        [_ tY] (:to segment)]
    (if (> fY tY)
      (>= fY y tY)
      (>= tY y fY))))

(defn lies-on? [point segment]
  (let [[x y] point
        from (:from segment)
        to (:to segment)]
    (and
     (not= point from)
     (not= point to)
     (inside-x? x segment)
     (inside-y? y segment)
     (=
      (tan (->Segment from point))
      (tan segment)))))

(defn asteroid-visible? [from to]
  (if (= from to) false
      (let [segment (->Segment from to)]
        (loop [idx 0]
          (if (= idx (count input-data)) true
              (and
               (not (lies-on? (nth input-data idx) segment))
               (recur (inc idx))))))))

(defn count-visible-asteroids [x y]
  (->> input-data
       (filter (partial asteroid-visible? [x y]))
       count))

(defn map-asteroids []
  (for [aster input-data]
    [aster (apply count-visible-asteroids aster)]))

(defn find-all-segments []
  (loop [points input-data
         result (list)]
    (if (empty? points) result
        (let [[point & rest] points]
          (println "looping for" point (count rest))
          (recur
           rest
           (concat
            result
            (for [match rest
                  :when (asteroid-visible? point match)]
              [point match])))))))

(defn count-hits [in-segments]
  (loop [segments in-segments
         result (priority-map)]
    (if (empty? segments) result
        (let [[[from to] & rest] segments]
          (recur
           rest
           (-> result
               (update from #(if (nil? %) 1 (inc %)))
               (update to #(if (nil? %) 1 (inc %)))))))))

(defn solve []
  (-> (find-all-segments)
      count-hits))

(def MONITORING_STATION [23 19])