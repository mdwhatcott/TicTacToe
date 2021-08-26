(ns ttt.terminal-ui
  (:require
    [clojure.string :as string]))

(def grid-characters
  {nil " "
   :X  "X"
   :O  "O"})

(def hint-characters
  {"1"  "1"
   "2"  "2"
   "3"  "3"
   "4"  "4"
   "5"  "5"
   "6"  "6"
   "7"  "7"
   "8"  "8"
   "9"  "9"

   "10" "a"
   "11" "b"
   "12" "c"
   "13" "d"
   "14" "e"
   "15" "f"
   "16" "g"})

(def input-characters
  {"1" "1"
   "2" "2"
   "3" "3"
   "4" "4"
   "5" "5"
   "6" "6"
   "7" "7"
   "8" "8"
   "9" "9"
   "a" "10"
   "b" "11"
   "c" "12"
   "d" "13"
   "e" "14"
   "f" "15"
   "g" "16"})

(defn prompt [message]
  (do
    (print message)
    (flush)
    (read-line)))

(defn prompt-options [message options-map]
  (let [full-message (format "%s %s: " message (keys options-map))
        response     (prompt full-message)]
    (if (contains? options-map response)
      (options-map response)
      (recur message options-map))))

(defn prompt-player [mark]
  (let [question (format "Who will be playing '%s'?" (grid-characters mark))]
    (prompt-options question {"human"    :human
                              "computer" :computer})))

(defn prompt-difficulty []
  (prompt-options "What difficulty level?" {"easy"   :easy
                                            "medium" :medium
                                            "hard"   :hard}))

(defn prompt-grid-size []
  (prompt-options "What size grid?" {"3x3" 3
                                     "4x4" 4}))

(defn prompt-player-move [mark]
  (let [message  (format "Player %s: Where would you like to move? " (grid-characters mark))
        response (prompt message)
        result   (get input-characters response)]
    (if (nil? result) (recur mark) result)))

(defn- cell-hint [n mark]
  (if (= mark " ") (hint-characters (str (inc n))) " "))

(defn render-winner [grid]
  (cond (not (:game-over? grid)) ""
        (nil? (:winner grid)) "\nWinner: none\n"
        :else (format "\nWinner: %s\n" (grid-characters (:winner grid)))))

(defn render-row [[slots hints & _]]
  (str (string/join "|" slots) "  "
       (string/join "|" hints)))

(defn buffer-row [col-count]
  (let [row (repeat col-count "-")]
    (string/replace (render-row [row row]) "|" "+")))

(defn render-grid [grid]
  (let [slots               (->> (range (:capacity grid))
                                 (map #(get (:filled-by-cell grid) %))
                                 (map grid-characters))
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
