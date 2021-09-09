(ns gui.establish-game
  (:require [db.datomic :as db])
  (:import (java.util Date)))

(defn now []
  (.toString (new Date)))

(defn update_ [state]
  (let [name  (now)
        width (-> state :game-grid :width)
        x     (:player1 state)
        o     (:player2 state)]
    (db/establish-new-game name width x o)
    (assoc state :game-name name
                 :turn-count 0
                 :screen :arena)))