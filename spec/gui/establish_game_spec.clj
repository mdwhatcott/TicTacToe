(ns gui.establish-game-spec
  (:require
    [speclj.core :refer :all]
    [gui.establish-game :refer :all]
    [db.datomic :as db]))

(describe "Establishing a new game"
  (with-stubs)

  (it "saves the game to the database"
    (let [input {:game-grid  {:width 42}
                 :player1    :human
                 :player2    :easy
                 :turn-count 298374}
          db    (stub :db)]
      (with-redefs [now                   (stub :now {:return "game-name"})
                    db/establish-new-game db]
        (let [output (update_ input)]
          (should= (:turn-count output) 0)
          (should= (:game-name output) "game-name")
          (should= (:screen output) :arena)
          (should-have-invoked :db {:with ["game-name" 42 :human :easy]}))))))
