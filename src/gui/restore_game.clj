(ns gui.restore-game
  (:require [ttt.restore :as restore]
            [db.datomic :as db]
            [gui.common :as c]))

(defn update_ [state]
  (let [unfinished (db/get-unfinished-game)]
    (if (nil? unfinished)
      (assoc state :screen :choose-grid)
      (let [grid-width   (:grid-width unfinished)
            screen-width (get-in state [:screens :screen-width])
            restored     (restore/restore-game unfinished)
            gui-grid     (c/assemble-grid-cells grid-width [0 0] [screen-width screen-width])]
        (-> state
            (merge restored)
            (assoc :screen :arena
                   :gui-grid gui-grid))))))