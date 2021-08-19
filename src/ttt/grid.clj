(ns ttt.grid)

(def _ nil)
(def X "X")
(def O "O")

(defn other [mark]
  (if (= mark X) O X))

(def ttt 3)

(defn make-grid
  ([] (make-grid ttt))
  ([side-length] (vec (repeat (* side-length side-length) nil))))

(defn place [mark on grid]
  (cond (>= on (count grid)) grid
        (= (grid on) (other mark)) grid
        :else (assoc grid on mark)))

(defn is-winner [grid mark]
  (or (and (= (grid 0) mark) (= (grid 1) mark) (= (grid 2) mark))
      (and (= (grid 3) mark) (= (grid 4) mark) (= (grid 5) mark))
      (and (= (grid 6) mark) (= (grid 7) mark) (= (grid 8) mark))

      (and (= (grid 0) mark) (= (grid 3) mark) (= (grid 6) mark))
      (and (= (grid 1) mark) (= (grid 4) mark) (= (grid 7) mark))
      (and (= (grid 2) mark) (= (grid 5) mark) (= (grid 8) mark))

      (and (= (grid 0) mark) (= (grid 4) mark) (= (grid 8) mark))
      (and (= (grid 2) mark) (= (grid 4) mark) (= (grid 6) mark))))

(defn winner [grid]
  (cond (is-winner grid X) X
        (is-winner grid O) O
        :else nil))

(defn available-cells [grid]
  (->> grid
       count
       range
       (filter #(nil? (nth grid %)))))