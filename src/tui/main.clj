(ns tui.main
  (:require
    [ttt.ai :as ai]
    [ttt.grid :as grid]
    [tui.game :as game]
    [tui.human :as human]
    [tui.prompts :as prompts]
    [db.datomic :as db]
    [datomic.api :as d])
  (:import (java.util Date)))

(def players
  {:human  human/suggest
   :easy   ai/easy
   :medium ai/medium
   :hard   ai/hard})

(defn now [] (.toString (Date.)))

(defn prepare-new-game []
  (let [game-name  (now)
        grid-width (prompts/prompt-grid-size)
        player-x   (prompts/prompt-player :X)
        player-o   (prompts/prompt-player :O)]
    (db/establish-new-game game-name grid-width player-x player-o)
    {:mark         :X
     :grid         (grid/new-grid grid-width)
     :player1      (players player-x)
     :player2      (players player-o)
     :game-name    game-name
     :turn-counter 0}))

(defn restore-game [unfinished]
  ; TODO: multiple moves from db, O's turn, etc...
  {:mark :X
   :grid (grid/new-grid (:grid-width unfinished))
   :player1 (players (:x-player unfinished))
   :player2 (players (:o-player unfinished))
   :game-name (:name unfinished)
   :turn-counter (count (:moves unfinished))})

(defn prepare-game []
  (let [unfinished (db/get-unfinished-game)]
    (if (some? unfinished)
      (restore-game unfinished)
      (prepare-new-game))))

(defn -main []
  #_(db/reset-db db/prod-uri db/schema)                       ; TODO: disable
  (with-redefs [db/conn (d/connect db/prod-uri)]
    (let [game-state (prepare-game)]
      (game/play game-state)
      (db/conclude-game (:game-name game-state))
      (d/shutdown true))))
