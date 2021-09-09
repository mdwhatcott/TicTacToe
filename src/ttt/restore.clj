(ns ttt.restore
  (:require
    [ttt.grid :refer [new-grid place]]))

(defn apply-moves [grid moves]
  (loop [grid  grid
         pairs (partition 2 (interleave moves (cycle [:X :O])))]
    (if (empty? pairs)
      grid
      (let [[spot mark] (first pairs)]
        (recur (place mark spot grid) (rest pairs))))))

(defn restore-game [unfinished]
  (let [{:keys [name grid-width x-player o-player moves]} unfinished
        turn-count (count moves)
        mark       (if (zero? (mod turn-count 2)) :X :O)
        grid       (apply-moves (new-grid grid-width) moves)]
    {:mark         mark
     :grid         grid
     :player1      x-player
     :player2      o-player
     :game-name    name
     :turn-counter turn-count}))

