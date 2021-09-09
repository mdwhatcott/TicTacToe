(ns tui.main
  (:require
    [ttt.grid :refer [new-grid place]]
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

(defn apply-moves [grid moves]
  (loop [grid  grid
         pairs (partition 2 (interleave moves (cycle [:X :O])))]
    (if (empty? pairs)
      grid
      (let [[spot mark] (first pairs)]
        (recur (place mark spot grid) (rest pairs))))))

(defn restore-game [unfinished]
  (let [{:keys [name grid-width x-player o-player moves]} unfinished
        turn-count (count moves)
        mark       (if (zero? (mod turn-count 2)) :X :O)
        grid       (apply-moves (new-grid grid-width) moves)]
    {:mark         mark
     :grid         grid
     :player1      x-player
     :player2      o-player
     :game-name    name
     :turn-counter turn-count}))

(defn prepare-game []
  (let [unfinished (db/get-unfinished-game)]
    (if (some? unfinished)
      (restore-game unfinished)
      (let [created (prepare-for-new-game)
            {:keys [name grid-width x-player o-player]} created]
        (db/establish-new-game name grid-width x-player o-player)
        (restore-game created)))))

(defn -main []
  #_(db/reset-db db/prod-uri db/schema)                     ; TODO: disable
  (with-redefs [db/conn (d/connect db/prod-uri)]
    (let [game-state (prepare-game)]
      (game/play game-state)
      (db/conclude-game (:game-name game-state))
      (d/shutdown true))))
