(ns day14.core)

(defn process-mask [operand]
  {:1s (Long/parseLong (clojure.string/replace operand #"[^1]" "0") 2)
   :0s (Long/parseLong (clojure.string/replace operand #"[^0]" "1") 2)})

(defn process-mem [cmd operand]
  {:cell (Long/parseLong (second (re-find #"mem\[(\d+)\]" cmd)))
   :op (Long/parseLong operand)})

(defn process-lines [line]
  (let [[cmd operand] (clojure.string/split line #" = ")]
    (if (= cmd "mask")
      [:mask (process-mask operand)]
      [:mem (process-mem cmd operand)])))

(def input-raw
  (->> "input.txt"
       (clojure.java.io/resource)
       (slurp)
       (clojure.string/split-lines)))

(def input-v1 (vec (map process-lines input-raw)))

(defn sum-with-mask [op mask]
  (bit-or (bit-and op (:0s mask)) (:1s mask)))

(defn fill-memory [input]
  (loop [result {}
         mask nil
         input input]
    (let [[cmd op] (first input)]
      (if (empty? input) result
        (recur
          (if (= cmd :mask) result
            (assoc result (:cell op) (sum-with-mask (:op op) mask)))
          (if (= cmd :mask) op mask)
          (rest input))))))

(defn task-1 []
  (apply + (vals (fill-memory input-v1))))

(defn to-binary-str [n mask]
  (let [bin (Long/toBinaryString n)]
    (concat (repeat (- (count mask) (count bin)) "0") (clojure.string/split bin #""))))

(assert (= (to-binary-str 3 ["1" "0" "1" "0"]) ["0" "0" "1" "1"]))

(defn process-mask-v2 [op]
  (clojure.string/split op #""))

(defn process-v2 [line]
  (let [[cmd operand] (clojure.string/split line #" = ")]
    (if (= cmd "mask")
      [:mask (process-mask-v2 operand)]
      [:mem (process-mem cmd operand)])))

(def input-v2 (vec (map process-v2 input-raw)))

(defn expand-mem [mask cell]
  (let [vcell (to-binary-str cell mask)]
    (reduce-kv
      (fn [acc idx b]
        (case b
          "1" (map #(str % "1") acc)
          "0" (map #(str % (nth vcell idx)) acc)
          "X" (mapcat #(list (str % "0") (str % "1")) acc)))
      [""]
      mask)))

(defn mem-assocs [mask {:keys [cell op]}]
  (mapcat
    #(list % op)
    (expand-mem mask cell)))

(defn fill-memory-v2 [input]
  (loop [result {}
         mask nil
         input input-v2]
    (let [[cmd op] (first input)]
      (if (empty? input) result
        (recur
          (if (= cmd :mask) result
            (apply assoc result (mem-assocs mask op)))
          (if (= cmd :mask) op mask)
          (rest input))))))

(defn task-2 []
  (apply + (vals (fill-memory-v2 input-v2))))
