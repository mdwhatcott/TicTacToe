(ns gui.establish-game)

(defn update_ [state]
  ;; (db/establish-new-game name grid-width x-player o-player) TODO
  ;; (restore/restore-game {:game :stuff}) TODO (or equivalent)
  (assoc state :screen :arena))