(ns ttt.grid)

(def X "X")
(def O "O")

(defn make-grid [] (vec (repeat 9 nil)))

(defn- other [mark] (if (= mark X) O X))
(defn- place [mark on grid]
  (cond (>= on (count grid)) grid
        (= (grid on) (other mark)) grid
        :else (assoc grid on mark)))
(defn place-x [on grid] (place X on grid))
(defn place-o [on grid] (place O on grid))

(defn threesome [mark row] (= row (repeat 3 mark)))

(defn columns-from-rows [rows]
  (for [column (range (count rows))]
    (map #(nth % column) rows)))

(defn winner [grid]
  (let [rows   (partition 3 grid)
        cols   (columns-from-rows rows)
        all    (concat rows cols)
        x-wins (some (partial threesome X) all)
        o-wins (some (partial threesome O) all)]
    (cond x-wins X o-wins O :else nil)))
