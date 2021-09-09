(ns gui.store-turn
  (:require [ttt.grid :as grid]
            [db.datomic :as db]))

(defn update_ [state]
  (let [{:keys [game-name turn-count last-move]} state
        game-over? (-> state :game-grid :game-over?)]
    (db/associate-move game-name turn-count last-move)
    (-> state
        (update :turn-count inc)
        (update :mark grid/other)
        (assoc :screen (if game-over? :conclude-game :arena)))))
