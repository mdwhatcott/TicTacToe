(ns tui.game
  (:require
    [ttt.grid :as grid]))

(defn tick [{:keys [grid mark player1 player2] :as game-state}]
  (let [suggestion (player1 mark grid)
        next-grid  (grid/place mark suggestion grid)
        winner     (:winner next-grid)
        game-over? (:game-over? next-grid)]
    (-> game-state
        (update :mark grid/other)
        (assoc :grid next-grid
               :player1 player2
               :player2 player1
               :winner winner
               :game-over? game-over?))))

(defn game-loop [presenter ticks]
  (loop [ticks ticks]
    (let [tick (first ticks)]
      (presenter (:grid tick))
      (if (:game-over? tick)
        (:winner tick)
        (recur (rest ticks))))))


(defn play2 [presenter game-state]
  (game-loop presenter (iterate tick game-state)))

;; TODO: remove
;; DEPRECATED
(defn play [presenter grid mark player1-in player2-in]
  (let [game-state {:grid    grid
                    :mark    mark
                    :player1 player1-in
                    :player2 player2-in}]
    (game-loop presenter (iterate tick game-state))))