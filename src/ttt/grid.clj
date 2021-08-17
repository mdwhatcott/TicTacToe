(ns ttt.grid)

(def X "X")
(def O "O")

(def ttt 3)
(def grid-count (* ttt ttt))

(defn make-grid [] (vec (repeat grid-count nil)))

(defn- other [mark] (if (= mark X) O X))
(defn- place [mark on grid]
  (cond (>= on (count grid)) grid
        (= (grid on) (other mark)) grid
        :else (assoc grid on mark)))
(defn place-x [on grid] (place X on grid))
(defn place-o [on grid] (place O on grid))

(defn threesome [mark row] (= row (repeat ttt mark)))

(defn rows->columns [rows]
  (reverse
    (for [column (range (count rows))]
      (map #(nth % column) rows))))

(defn extract-diagonal [rows row]
  (loop [row row, col 0, out []]
    (if (< row 0)
      (filter #(not (false? %)) out)
      (let [value (nth (nth rows row []) col false)]
        (recur (dec row) (inc col) (conj out value))))))

(defn rows->diagonals [rows]
  (let [length         (count rows)
        height         (count (first rows))
        diagonal-count (dec (+ length height))]
    (->> (range diagonal-count)
         (map #(extract-diagonal rows %)))))

(defn winner [grid]
  (let [rows   (partition 3 grid)
        cols   (rows->columns rows)
        left   (rows->diagonals cols)
        right  (rows->diagonals rows)
        all    (concat rows cols left right)
        x-wins (some (partial threesome X) all)
        o-wins (some (partial threesome O) all)]
    (cond x-wins X o-wins O :else nil)))
