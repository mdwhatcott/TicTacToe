(ns tui.prompts)

(def grid-characters
  {nil " "
   :X  "X"
   :O  "O"})

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
  (print message)
  (flush)
  (read-line))

(defn prompt-options [message options-map]
  (loop [message       message
         options-map   options-map
         attempts-left 10]
    (if (zero? attempts-left)
      (throw (Exception. "too many wrong answers"))
      (let [full-message (format "%s %s: " message (keys options-map))
            response     (prompt full-message)]
        (if (contains? options-map response)
          (options-map response)
          (recur message options-map (dec attempts-left)))))))

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

