(ns ttt.grid)

(def _ nil)
(def X "X")
(def O "O")

(defn other [mark]
  (if (= mark X) O X))

;; This hard-coded value is the only thing preventing
;; a general solution to game variants that require
;; more than 3-in-a-row as winning conditions.
(def ttt 3)

(defn make-grid
  ([] (make-grid ttt))
  ([side-length] (vec (repeat (* side-length side-length) nil))))

(defn place [mark on grid]
  (cond (>= on (count grid)) grid
        (= (grid on) (other mark)) grid
        :else (assoc grid on mark)))
(defn place-x [on grid] (place X on grid))
(defn place-o [on grid] (place O on grid))

(defn tic-tac-toe [mark row] (= row (repeat ttt mark)))

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

;; winner scans grid for a winning tic-tac-toe (3 in a row).
;; It currently employs a brute-force approach. For boards
;; larger than 3x3, it would be better to index the grid and
;; only scan from populated cells.
;; Also, this implementation currently assumes that the rows
;; and columns are as comprised of only 3 cells since it
;; passes them wholesale to the tic-tac-toe function, which
;; is currently hard-coded to look for 3-in-a-row conditions.
(defn winner [grid]
  (let [length (Math/sqrt (count grid))
        rows   (partition (int length) grid)
        cols   (rows->columns rows)
        left   (rows->diagonals cols)
        right  (rows->diagonals rows)
        all    (concat rows cols left right)
        x-wins (some (partial tic-tac-toe X) all)
        o-wins (some (partial tic-tac-toe O) all)]
    (cond x-wins X
          o-wins O
          :else nil)))

(defn available-cells [grid]
  (->> grid
       count
       range
       (filter #(nil? (nth grid %)))))