(ns tui.main-spec
  (:require
    [speclj.core :refer :all]
    [tui.main :refer :all]
    [tui.prompts :as prompts]
    [db.datomic :as db]
    [ttt.grid :as grid]
    [ttt.ai :as ai]
    [tui.human :as human]))

(describe "Preparing the game"
  (with-stubs)

  (it "prepares a new game when there is no unfinished game in the db"
    (let [namer         (stub :namer {:return "new-game"})
          grid-prompt   (stub :grid-prompt {:return 4})
          player-prompt (stub :player {:return :medium})
          db-reader     (stub :db-reader {:return nil})
          db-writer     (stub :db-writer)]
      (with-redefs [now                      namer
                    prompts/prompt-grid-size grid-prompt
                    prompts/prompt-player    player-prompt
                    db/get-unfinished-game   db-reader
                    db/establish-new-game    db-writer]
        (should= {:mark         :X
                  :grid         (grid/new-grid 4)
                  :player1      ai/medium
                  :player2      ai/medium
                  :game-name    "new-game"
                  :turn-counter 0} (prepare-game))
        (should-have-invoked :db-writer {:with ["new-game" 4 :medium :medium] :times 1}))))

  (it "restores the unfinished game from the database"
    (let [db-read   {:name       "saved-game"
                     :grid-width 3
                     :x-player   :human
                     :o-player   :easy
                     :moves      []}
          db-reader (stub :db-reader {:return db-read})
          db-writer (stub :db-writer)]
      (with-redefs [now                      (stub :namer)
                    prompts/prompt-grid-size (stub :grid-prompt)
                    prompts/prompt-player    (stub :player)
                    db/get-unfinished-game   db-reader
                    db/establish-new-game    db-writer]
        (should= {:mark         :X
                  :grid         (grid/new-grid 3)
                  :player1      human/suggest
                  :player2      ai/easy
                  :game-name    "saved-game"
                  :turn-counter 0} (prepare-game)))))

  )

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
                              :player1      human/suggest
                              :player2      ai/easy
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
                              :player1      human/suggest
                              :player2      ai/easy
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
                              :player1      human/suggest
                              :player2      ai/easy
                              :game-name    "saved-game"
                              :turn-counter 1}))))

  )
