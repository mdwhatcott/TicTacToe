(ns gui.store-turn
  (:require [ttt.grid :as grid]))

(defn update_ [state]
  (assoc state :screen :arena
               :mark (grid/other (:mark state))))

;; (db/associate-move game-name turn-counter suggestion) ; TODO
;; increment turn count
;; alternate mark
;; (if game-over? :conclude-game :arena)