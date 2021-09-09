(ns tui.game
  (:require
    [ttt.grid :as grid]
    [db.datomic :as db]
    [tui.grid :as terminal]
    [tui.human :as human]
    [ttt.ai :as ai]))

(def players
  (assoc ai/players :human human/suggest))

(defn tick [{:keys [game-name turn-count game-grid mark player1 player2] :as game-state}]
  (let [player     (if (= mark :X) player1 player2)
        suggestion ((players player) mark game-grid)
        next-grid  (grid/place mark suggestion game-grid)
        winner     (:winner next-grid)
        game-over? (:game-over? next-grid)]
    (db/associate-move game-name turn-count suggestion)
    (-> game-state
        (update :mark grid/other)
        (assoc :game-grid next-grid
               :winner winner
               :game-over? game-over?
               :turn-count (inc turn-count)))))

(defn game-loop [ticks]
  (loop [ticks ticks]
    (let [tick (first ticks)]
      (terminal/print-grid (:game-grid tick))
      (if (:game-over? tick)
        (:winner tick)
        (recur (rest ticks))))))

(defn play [game-state]
  (game-loop (iterate tick game-state)))
