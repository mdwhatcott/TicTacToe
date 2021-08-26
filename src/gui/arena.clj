(ns gui.arena
  (:require
    [quil.core :as q]
    [gui.common :as c]
    [ttt.grid :as g]))

(defn human-choice [game-grid gui-grid clicked? mx my mark]
  (if (or (not clicked?) (< mx 0) (< my 0))
    game-grid
    (let [hovering? (map #(c/bounded? [mx my] (:box %)) gui-grid)
          indexed   (map-indexed vector hovering?)
          on        (ffirst (drop-while #(not (second %)) indexed))]
      (g/place mark on game-grid))))

(defn ai-choice [grid mark ai]
  (let [choice (ai mark grid)]
    (g/place mark choice grid)))

(defn place [mark player game-grid gui-grid clicked? mx my]
  (if (= player :human)
    (human-choice game-grid gui-grid clicked? mx my mark)
    (ai-choice game-grid mark player)))

(defn play [state]
  (let [mx               (q/mouse-x)
        my               (q/mouse-y)
        mark             (:mark state)
        player1          (:player1 state)
        player2          (:player2 state)
        game-grid        (:game-grid state)
        gui-cells        (:gui-grid state)
        clicked?         (get-in state [:mouse :clicked?])
        player           (if (= :X mark) player1 player2)
        game-grid        (place mark player game-grid gui-cells clicked? mx my)
        winner           (:winner game-grid)
        game-over?       (:game-over? game-grid)
        capacity         (:capacity game-grid)
        game-cells       (:filled-by-cell game-grid)
        game-marks       (for [i (range capacity)] (get game-cells i))
        marked-gui-cells (for [i (range capacity)]
                           (let [cell      (nth gui-cells i)
                                 hovering? (c/bounded? [mx my] (:box cell))
                                 mark-made (nth game-marks i)
                                 cell-mark (if (not hovering?) mark-made (or mark-made mark))
                                 hovering? (and hovering? (nil? mark-made) (= player :human))
                                 winner?   (and (some? winner) (= winner mark-made))
                                 loser?    (and (some? winner) (not= winner mark-made))
                                 tied?     (and game-over? (not winner?) (not loser?))]
                             (assoc cell :mark cell-mark
                                         :hovering? hovering?
                                         :winner? winner?
                                         :loser? loser?
                                         :tied? tied?)))]
    (assoc state :game-grid game-grid
                 :mark (if (zero? (mod (count game-cells) 2)) :X :O)
                 :gui-grid marked-gui-cells)))

(defn reset [state]
  (assoc state :transition true
               :mark :X
               :player1 nil
               :player2 nil
               :game-grid nil))

(defn update [state]
  (let [clicked?   (get-in state [:mouse :clicked?])
        game-over? (get-in state [:game-grid :game-over?])]
    (cond
      (and game-over? clicked?) (reset state)
      game-over? state
      :else (play state))))

(defn calculate-anchors [screen-width]
  {:grid-box {:p1 [0 0] :p2 [screen-width screen-width]}})

(defn draw [state]
  (let [cells (get state :gui-grid)]
    (c/render-grid-cells 10 cells)))
