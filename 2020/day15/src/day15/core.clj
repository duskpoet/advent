(ns day15.core)

(def input-raw "1,2,16,19,18,0")

(def input
  (->> (clojure.string/split input-raw #",")
       (map #(Integer/parseInt %))
       (vec)))

(defn expand-game [{:keys [num pos state]}]
  (let [last-occur (state num)]
    (if (nil? last-occur)
      {:num 0
       :state (assoc state num pos)
       :pos (inc pos)}
      {:num (- pos last-occur)
       :state (assoc state num pos)
       :pos (inc pos)})))

(defn gen-state [input]
  (reduce-kv
    (fn [acc idx n]
      (assoc acc n idx))
    {}
    input))

(def input-size (count input))

(defn task-1 []
  (loop [game {:num (last input) :pos (dec input-size) :state (gen-state (into [] (subvec input 0 (dec input-size))))}]
    (if (= (:pos game) (dec 2020))
      (:num game)
      (recur (expand-game game)))))

(assert (= (task-1) 536))

(defn task-2 []
  (loop [game {:num (last input) :pos (dec input-size) :state (gen-state (into [] (subvec input 0 (dec input-size))))}]
    (if (zero? (rem (:pos game) 100000)) (println (:pos game)))
    (if (= (:pos game) (dec 30000000))
      (:num game)
      (recur (expand-game game)))))


