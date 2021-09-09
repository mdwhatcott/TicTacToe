(ns gui.conclude-game
  (:require [db.datomic :as db]))

(defn update_ [state]
  (db/conclude-game (:game-name state))
  (assoc state :screen :choose-grid
               :game-grid nil
               :mark :X
               :player1 nil
               :player2 nil
               :gui-grid nil))