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
  (if (= mark " ") (str n) " "))

(def characters
  {nil " "
   :X "X"
   :O "O"})

(defn render-grid [grid]
  (let [slots (->> (range (g/capacity grid))
                   (map #(get (:filled-by-cell grid) %))
                   (map characters))]
    (str
      (format "%s|%s|%s %s|%s|%s\n"
              (nth slots 0)
              (nth slots 1)
              (nth slots 2)
              (cell-hint 1 (nth slots 0))
              (cell-hint 2 (nth slots 1))
              (cell-hint 3 (nth slots 2)))
      (format "%s+%s+%s %s+%s+%s\n" "-" "-" "-" "-" "-" "-")
      (format "%s|%s|%s %s|%s|%s\n"
              (nth slots 3)
              (nth slots 4)
              (nth slots 5)
              (cell-hint 4 (nth slots 3))
              (cell-hint 5 (nth slots 4))
              (cell-hint 6 (nth slots 5)))
      (format "%s+%s+%s %s+%s+%s\n" "-" "-" "-" "-" "-" "-")
      (format "%s|%s|%s %s|%s|%s\n"
              (nth slots 6)
              (nth slots 7)
              (nth slots 8)
              (cell-hint 7 (nth slots 6))
              (cell-hint 8 (nth slots 7))
              (cell-hint 9 (nth slots 8)))
      (when (:game-over? grid)
        (format "\nWinner: %s\n" (characters (:winner grid)))))))

(defn print-grid [grid]
  (println (render-grid grid)))
