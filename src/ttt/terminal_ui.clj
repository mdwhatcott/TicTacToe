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

(defn render-grid [grid]
  (let [slots (->> (range (g/capacity grid))
                   (map #(get (:filled-by-cell grid) %))
                   (map {nil " ", :X "X", :O "O"}))]
    (str
      (format "%s|%s|%s\n" (nth slots 0) (nth slots 1) (nth slots 2))
      (format "%s+%s+%s\n" "-" "-" "-")
      (format "%s|%s|%s\n" (nth slots 3) (nth slots 4) (nth slots 5))
      (format "%s+%s+%s\n" "-" "-" "-")
      (format "%s|%s|%s\n" (nth slots 6) (nth slots 7) (nth slots 8)))))

(def decorations
  ["1|2|3"
   "-+-+-"
   "4|5|6"
   "-+-+-"
   "7|8|9"])

(defn decorate-grid [rendered-grid]
  (let [lines    (string/split-lines rendered-grid)
        combined (interleave lines decorations)]
    (str (->> combined
              (partition 2)
              (map #(string/join " " %))
              (string/join "\n")) "\n")))

(defn print-grid [grid]
  (println (decorate-grid (render-grid grid))))
