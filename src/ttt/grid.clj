(ns ttt.grid
  (:require [clojure.set :as set]))

(def _ nil)
(def X "X") ;; TODO: consider using :X instead
(def O "O") ;; TODO: consider using :O instead

(defn other [mark]
  (if (= mark X) O X))

(defn diagonal [rows]
  (loop [row 0, col 0, result []]
    (cond (>= row (count rows)) result
          (>= col (count (first rows))) result
          :else (let [next (conj result (nth (nth rows row) col))]
                  (recur (inc row) (inc col) next)))))

(defn rows->columns [rows]
  (for [column (range (count rows))]
    (map #(nth % column) rows)))

(defn new-grid [width]
  (let [capacity  (* width width)
        rows      (partition width (range capacity))
        cols      (rows->columns rows)
        diagonals [(diagonal rows)
                   (diagonal (reverse cols))]
        wins      (map set (concat rows cols diagonals))]
    {:empty-cell-count capacity
     :empty-cells      (set (range capacity))
     :row-count        width
     :col-count        width
     :filled-by-cell   {}
     :filled-by-mark   {X #{}
                        O #{}}
     :wins             wins}))

(defn capacity [grid]
  (* (:row-count grid) (:col-count grid)))

(defn already-placed? [on grid]
  (let [by-cells (:filled-by-cell grid)]
    (boolean (by-cells on))))

(defn out-of-bounds? [on grid]
  (or (< on 0)
      (>= on (capacity grid))))

(defn place2 [mark on grid]
  (cond (nil? mark) grid
        (out-of-bounds? on grid) grid
        (already-placed? on grid) grid
        :else
        (let [old-by-mark      (:filled-by-mark grid)
              old-cells        (old-by-mark mark)

              filled-by-mark   (assoc old-by-mark mark (conj old-cells on))
              filled-by-cells  (assoc (:filled-by-cell grid) on mark)
              empty-cell-count (dec (:empty-cell-count grid))
              empty-cells      (disj (:empty-cells grid) on)]
          (-> grid
              (assoc :filled-by-mark filled-by-mark
                     :filled-by-cell filled-by-cells
                     :empty-cell-count empty-cell-count
                     :empty-cells empty-cells)))))

(defn winner2? [marks combinations]
  (let [is-win?         (fn [combo] (set/superset? marks combo))
        failed-attempts (take-while #(not (is-win? %)) combinations)]
    (< (count failed-attempts) (count combinations))))

(defn winner2 [grid]
  (let [combinations (:wins grid)
        by-marks     (:filled-by-mark grid)
        x-marks      (by-marks X)
        o-marks      (by-marks O)
        x-count      (count x-marks)
        o-count      (count o-marks)]
    (cond (and (>= x-count 3) (winner2? x-marks combinations)) X
          (and (>= o-count 3) (winner2? o-marks combinations)) O
          :else nil)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Deprecated
(def ttt 3)

;; Deprecated
(defn make-grid
  ([] (make-grid ttt))
  ([side-length] (vec (repeat (* side-length side-length) nil))))

;; Deprecated
(defn place [mark on grid]
  (cond (>= on (count grid)) grid
        (= (grid on) (other mark)) grid
        :else (assoc grid on mark)))

;; Deprecated
(defn winner? [grid mark]
  (or (and (= (grid 0) mark) (= (grid 1) mark) (= (grid 2) mark))
      (and (= (grid 3) mark) (= (grid 4) mark) (= (grid 5) mark))
      (and (= (grid 6) mark) (= (grid 7) mark) (= (grid 8) mark))

      (and (= (grid 0) mark) (= (grid 3) mark) (= (grid 6) mark))
      (and (= (grid 1) mark) (= (grid 4) mark) (= (grid 7) mark))
      (and (= (grid 2) mark) (= (grid 5) mark) (= (grid 8) mark))

      (and (= (grid 0) mark) (= (grid 4) mark) (= (grid 8) mark))
      (and (= (grid 2) mark) (= (grid 4) mark) (= (grid 6) mark))))

;; Deprecated
(defn winner [grid]
  (cond (winner? grid X) X
        (winner? grid O) O
        :else nil))

;; Deprecated
(defn available-cells [grid]
  (->> grid
       count
       range
       (filter #(nil? (nth grid %)))))