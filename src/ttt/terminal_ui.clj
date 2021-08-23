(ns ttt.terminal-ui
  (:require
    [ttt.grid :as g]
    [clojure.string :as string]))

(defn prompt [mark]
  (do
    (print (format "Player %s: Where would you like to move? " mark))
    (flush)
    ; TODO: handle bad input
    (read-line)))

(defn- cell-hint [n mark]
  (if (= mark " ") (str (inc n)) " "))

(def characters
  {nil " "
   :X  "X"
   :O  "O"})

(defn render-winner [grid]
  (if (:game-over? grid)
    (format "\nWinner: %s\n" (characters (:winner grid))) ""))

(defn buffer-row [col-count]
  (let [row (string/join "+" (repeat col-count "-"))]
    (str row " " row)))

(defn render-row [[slots hints & _]]
  (str (string/join "|" slots) " "
       (string/join "|" hints)))

(defn render-grid [grid]
  (let [slots               (->> (range (g/capacity grid))
                                 (map #(get (:filled-by-cell grid) %))
                                 (map characters))
        hints               (map #(cell-hint (first %) (second %))
                                 (partition 2 (interleave (range (g/capacity grid)) slots)))
        slot-rows           (partition (:col-count grid) slots)
        hint-rows           (partition (:col-count grid) hints)
        slot-hint-row-pairs (partition 2 (interleave slot-rows hint-rows))
        rendered-rows       (map render-row slot-hint-row-pairs)
        buffer-rows         (repeat (count rendered-rows) (buffer-row (:col-count grid)))
        total-rows          (dec (+ (count rendered-rows) (count buffer-rows)))
        all-rows            (take total-rows (interleave rendered-rows buffer-rows))
        rendered-grid       (string/join "\n" all-rows)
        winner              (render-winner grid)]
    (format "%s\n%s" rendered-grid winner)))

(defn print-grid [grid]
  (println (render-grid grid)))
