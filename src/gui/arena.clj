(ns gui.arena
  (:require
    [gui.common :refer [bounded?]]
    [gui.render :refer [render-grid-cells]]
    [ttt.grid :refer [place]]))

(defn human-choice [game-grid gui-grid mx my mark]
  (let [hovering? (map #(bounded? [mx my] (:box %)) gui-grid)
        indexed   (map-indexed vector hovering?)
        on        (ffirst (drop-while #(not (second %)) indexed))]
    (place mark on game-grid)))

(defn ai-choice [grid mark ai]
  (let [choice (ai mark grid)]
    (place mark choice grid)))

(defn take-turn [state]
  (let [mx        (get-in state [:mouse :x])
        my        (get-in state [:mouse :y])
        mark      (:mark state)
        player1   (:player1 state)
        player2   (:player2 state)
        game-grid (:game-grid state)
        gui-grid  (:gui-grid state)
        player    (if (= :X mark) player1 player2)]
    (if (= player :human)
      (human-choice game-grid gui-grid mx my mark)
      (ai-choice game-grid mark player))))

(defn waiting-for-human? [state]
  (let [mark     (:mark state)
        player1  (:player1 state)
        player2  (:player2 state)
        player   (if (= :X mark) player1 player2)
        clicked? (get-in state [:mouse :clicked?])]
    (and (= player :human) (not clicked?))))

(defn update-gui-grid-with-hovered-cell [state]
  (let [mx         (get-in state [:mouse :x])
        my         (get-in state [:mouse :y])
        mark       (:mark state)
        game-grid  (:game-grid state)
        gui-cells  (:gui-grid state)
        capacity   (:capacity game-grid)
        game-cells (:filled-by-cell game-grid)
        game-marks (for [i (range capacity)] (get game-cells i))]
    (for [i (range capacity)]
      (let [cell      (nth gui-cells i)
            hovering? (bounded? [mx my] (:box cell))
            mark-made (nth game-marks i)
            cell-mark (if (not hovering?) mark-made (or mark-made mark))
            hovering? (and hovering? (nil? mark-made))]
        (assoc cell :mark cell-mark
                    :hovering? hovering?)))))

(defn update-gui-grid-with-game-grid [state]
  (let [mark       (:mark state)
        game-grid  (:game-grid state)
        gui-cells  (:gui-grid state)
        winner     (:winner game-grid)
        game-over? (:game-over? game-grid)
        capacity   (:capacity game-grid)
        game-cells (:filled-by-cell game-grid)
        game-marks (for [i (range capacity)] (get game-cells i))]
    (for [i (range capacity)]
      (let [cell      (nth gui-cells i)
            mark-made (nth game-marks i)
            cell-mark (if mark-made (or mark-made mark))
            winner?   (and (some? winner) (= winner mark-made))
            loser?    (and (some? winner) (not= winner mark-made))
            tied?     (and game-over? (not winner?) (not loser?))]
        (assoc cell :mark cell-mark
                    :hovering? false
                    :winner? winner?
                    :loser? loser?
                    :tied? tied?)))))

(defn set-mark-for-upcoming-frame [game-grid]
  (if (zero? (mod (count (:filled-by-cell game-grid)) 2)) :X :O))

(defn play [state]
  (if (waiting-for-human? state)
    (assoc state :gui-grid (update-gui-grid-with-hovered-cell state))
    (let [game-grid (take-turn state)
          state     (assoc state :game-grid game-grid)
          gui-grid  (update-gui-grid-with-game-grid state)
          mark      (set-mark-for-upcoming-frame game-grid)]
      (assoc state :game-grid game-grid
                   :gui-grid gui-grid
                   :mark mark))))

(defn reset [state]
  (assoc state :transition? true
               :mark :X
               :player1 nil
               :player2 nil
               :game-grid nil))

(defn update_ [state]
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
    (render-grid-cells cells)))
