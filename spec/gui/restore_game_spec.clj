(ns gui.restore-game-spec
  (:require
    [speclj.core :refer :all]
    [gui.restore-game :refer :all]
    [db.datomic :as db]))

(def unfinished-game
  {:name       "unfinished"
   :grid-width 3
   :x-player   :human
   :o-player   :easy
   :moves      [0 1 2]})

(describe "Restoring Unfinished Games"
  (with-stubs)

  (it "transitions to the choose-grid phase without an unfinished game to restore"
    (let [db-restore (stub :db {:return nil})]
      (with-redefs [db/get-unfinished-game db-restore]
        (let [input {}
              output (update_ input)]
          (should= :choose-grid (:screen output))))))

  (it "restores unfinished game and skips to the arena"
    (let [db-restore (stub :db {:return unfinished-game})]
      (with-redefs [db/get-unfinished-game db-restore]
        (let [input {:already-present :should-stay
                     :screens {:screen-width 5}}
              output (update_ input)]
          (should-contain :game-grid output)
          (should-contain :mark output)
          (should-contain :player1 output)
          (should-contain :player2 output)
          (should-contain :game-name output)
          (should-contain :turn-count output)
          (should-contain :already-present output)
          (should-contain :gui-grid output)
          (should= :arena (:screen output))))))

  )
