(ns gui.conclude-game-spec
  (:require
    [speclj.core :refer :all]
    [gui.conclude-game :refer :all]
    [db.datomic :as db]))

(let [db (stub :db)]
  (with-redefs [db/conclude-game db]

    (describe "Concluding the Game"
      (with-stubs)

      (it "resets all transient game state"
        (let [input  {}
              output (update_ input)]
          (should= {:screen    :choose-grid
                    :game-grid nil
                    :mark      :X
                    :player1   nil
                    :player2   nil
                    :gui-grid  nil} output)))

      (it "marks the game concluded in the database"
        (let [input {:game-name "game-name"}]
          (update_ input)
          (should-have-invoked :db {:with ["game-name"]})))

      )))
