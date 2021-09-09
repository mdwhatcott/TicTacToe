(ns gui.restore-game)

(defn update_ [state]
  ;; (restore/restore-game (db/get-unfinished-game)) TODO
  (assoc state :screen :choose-grid))                       ; (when unfinished game-loaded? :arena)