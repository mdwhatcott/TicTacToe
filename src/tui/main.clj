(ns tui.main
  (:require
    [ttt.grid :refer [new-grid place]]
    [ttt.restore :as restore]
    [tui.game :as game]
    [tui.prompts :as prompts]
    [db.datomic :as db]
    [datomic.api :as d])
  (:import (java.util Date)))

(defn now [] (.toString (Date.)))

(defn prepare-for-new-game []
  {:grid-width (prompts/prompt-grid-size)
   :x-player   (prompts/prompt-player :X)
   :o-player   (prompts/prompt-player :O)
   :name       (now)})

(defn prepare-game []
  (let [unfinished (db/get-unfinished-game)]
    (if (some? unfinished)
      (restore/restore-game unfinished)
      (let [created (prepare-for-new-game)
            {:keys [name grid-width x-player o-player]} created]
        (db/establish-new-game name grid-width x-player o-player)
        (restore/restore-game created)))))

(defn -main []
  (let [game-state (prepare-game)]
    (game/play game-state)
    (db/conclude-game (:game-name game-state))
    (d/shutdown true)))
