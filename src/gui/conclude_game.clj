(ns gui.conclude-game)

(defn update_ [state]
  ;; (db/conclude-game (:game-name game-state))             ; TODO
  (assoc state :screen :choose-grid                         ; TODO: reimplement tests
               :mark :X
               :player1 nil
               :player2 nil
               :game-grid nil
               #_:turn-count #_0
               #_:game-name #_(now)))