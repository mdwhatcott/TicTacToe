(ns ttt.grid
  (:require
    [clojure.set :as set]))

(def other
  {:X :O
   :O :X})

(defn- diagonal [rows]
  (loop [row 0, col 0, result []]
    (cond (>= row (count rows)) result
          (>= col (count (first rows))) result
          :else (let [next (conj result (nth (nth rows row) col))]
                  (recur (inc row) (inc col) next)))))

(defn- rows->columns [rows]
  (for [column (range (count rows))]
    (map #(nth % column) rows)))

(defn- all-wins-for-cell [cell all-wins]
  (filter #(contains? % cell) all-wins))

(defn- wins-by-cell [capacity wins]
  (let [keys   (range capacity)
        values (map #(all-wins-for-cell % wins) keys)]
    (zipmap keys values)))

(defn new-grid [width]
  (let [capacity     (* width width)
        rows         (partition width (range capacity))
        cols         (rows->columns rows)
        diagonals    [(diagonal rows)
                      (diagonal (reverse cols))]
        wins         (map set (concat rows cols diagonals))
        wins-by-cell (wins-by-cell capacity wins)]
    {:game-over?     false
     :winner         nil
     :empty-cells    (set (range capacity))
     :width          width
     :capacity       capacity

     ; {1 :X, 2 :O}
     :filled-by-cell {}

     ; {:X #{0 3 8}, :O #{2 5 7}}
     :filled-by-mark {:X #{}
                      :O #{}}

     ; {0 [#{0 1 2} #{0 3 6} #{0 4 8}] ; all wins 0 is part of
     ;  1 [#{0 1 2} #{1 4 7}]          ; all wins 1 is part of, etc..
     :wins-by-cell   wins-by-cell}))

(defn- already-placed? [on grid]
  (let [by-cells (:filled-by-cell grid)]
    (boolean (by-cells on))))

(defn- out-of-bounds? [on grid]
  (or (< on 0)
      (>= on (:capacity grid))))

(defn- winner? [marks combinations]
  (let [is-win?         (fn [combo] (set/superset? marks combo))
        failed-attempts (take-while #(not (is-win? %)) combinations)]
    (< (count failed-attempts) (count combinations))))

(defn place [mark on grid]
  (cond (:game-over? grid) grid
        (nil? mark) grid
        (out-of-bounds? on grid) grid
        (already-placed? on grid) grid
        :else
        (let [old-by-mark     (:filled-by-mark grid)
              old-cells       (old-by-mark mark)

              filled-by-mark  (assoc old-by-mark mark (conj old-cells on))
              filled-by-cells (assoc (:filled-by-cell grid) on mark)
              empty-cells     (disj (:empty-cells grid) on)

              wins-for-cell   (get-in grid [:wins-by-cell on])
              is-winner?      (winner? (filled-by-mark mark) wins-for-cell)
              winner          (if is-winner? mark nil)]
          (assoc grid :filled-by-mark filled-by-mark
                      :filled-by-cell filled-by-cells
                      :empty-cells empty-cells
                      :game-over? (or is-winner? (empty? empty-cells))
                      :winner winner))))
