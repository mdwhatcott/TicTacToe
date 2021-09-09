(ns ttt.restore-spec
  (:require
    [speclj.core :refer :all]
    [ttt.restore :refer :all]
    [ttt.grid :as grid]))

(describe "Unfinished Game Restoration"
  (it "restores a game that has only just begin"
    (let [saved    {:name       "saved-game"
                    :grid-width 3
                    :x-player   :human
                    :o-player   :easy
                    :moves      []}
          restored (restore-game saved)]
      (->> restored (should= {:mark         :X
                              :grid         (grid/new-grid 3)
                              :player1      :human
                              :player2      :easy
                              :game-name    "saved-game"
                              :turn-counter 0}))))

  (it "restores a game wherein each player has already taken a single turn"
    (let [saved    {:name       "saved-game"
                    :grid-width 3
                    :x-player   :human
                    :o-player   :easy
                    :moves      [8 0]}
          restored (restore-game saved)]
      (->> restored (should= {:mark         :X
                              :grid         (->> (grid/new-grid 3)
                                                 (grid/place :X 8)
                                                 (grid/place :O 0))
                              :player1      :human
                              :player2      :easy
                              :game-name    "saved-game"
                              :turn-counter 2}))))

  (it "restores a game wherein it is now O's turn to move"
    (let [saved    {:name       "saved-game"
                    :grid-width 3
                    :x-player   :human
                    :o-player   :easy
                    :moves      [8]}
          restored (restore-game saved)]
      (->> restored (should= {:mark         :O
                              :grid         (->> (grid/new-grid 3)
                                                 (grid/place :X 8))
                              :player1      :human
                              :player2      :easy
                              :game-name    "saved-game"
                              :turn-counter 1}))))

  )
