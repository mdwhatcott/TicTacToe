(ns tui.grid
  (:require
    [clojure.string :as string]
    [tui.prompts :as prompts]))

(def values
  (concat (range 1 10)
          (seq "abcdefghijklmnopqrstuvwxyz")))

(def keys
  (->> (range 1 (count values))
       (map #(format "%d" %))))

;(def hint-characters
;  {"1"  "1"
;   "2"  "2"
;   "3"  "3"
;   "4"  "4"
;   "5"  "5"
;   "6"  "6"
;   "7"  "7"
;   "8"  "8"
;   "9"  "9"
;
;   "10" "a"
;   "11" "b"
;   "12" "c"
;   "13" "d"
;   "14" "e"
;   "15" "f"
;   "16" "g"})

(def hint-characters
  (zipmap keys values))

(defn- cell-hint [n mark]
  (if (= mark " ") (hint-characters (str (inc n))) " "))

(defn render-winner [grid]
  (cond (not (:game-over? grid)) ""
        (nil? (:winner grid)) "\nWinner: none\n"
        :else (format "\nWinner: %s\n" (prompts/grid-characters (:winner grid)))))

(defn render-row [[slots hints & _]]
  (str (string/join "|" slots) "  "
       (string/join "|" hints)))

(defn buffer-row [col-count]
  (let [row (repeat col-count "-")]
    (string/replace (render-row [row row]) "|" "+")))

(defn render-grid [grid]
  (let [slots               (->> (range (:capacity grid))
                                 (map #(get (:filled-by-cell grid) %))
                                 (map prompts/grid-characters))
        hints               (map #(cell-hint (first %) (second %))
                                 (partition 2 (interleave (range (:capacity grid)) slots)))
        slot-rows           (partition (:width grid) slots)
        hint-rows           (partition (:width grid) hints)
        slot-hint-row-pairs (partition 2 (interleave slot-rows hint-rows))
        rendered-rows       (map render-row slot-hint-row-pairs)
        buffer-rows         (repeat (count rendered-rows) (buffer-row (:width grid)))
        total-rows          (dec (+ (count rendered-rows) (count buffer-rows)))
        all-rows            (take total-rows (interleave rendered-rows buffer-rows))
        rendered-grid       (string/join "\n" all-rows)
        winner              (render-winner grid)]
    (format "%s\n%s" rendered-grid winner)))

(defn print-grid [grid]
  (println (render-grid grid)))
