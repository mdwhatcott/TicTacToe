(ns ttt.game
  (:require [ttt.grid :refer :all]))

(defn play [presenter grid mark player1-in player2-in]
  (do
    (presenter grid)
    (cond (not (nil? (winner grid))) (other mark)
          (empty? (available-cells grid)) nil
          :else (play presenter
                      (place mark (player1-in mark grid) grid)
                      (other mark)
                      player2-in
                      player1-in))))