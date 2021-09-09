(ns gui.store-turn-spec
  (:require
    [speclj.core :refer :all]
    [gui.store-turn :refer :all]
    [db.datomic :as db]
    [ttt.grid :as grid]))

(def input-not-over
  {:game-name  "game-name"
   :turn-count 7
   :mark       :X
   :last-move  5
   :game-grid  (grid/new-grid 3)})

(def input-mark-o
  (assoc input-not-over :mark :O))

(let [db (stub :db)]
  (with-redefs [db/associate-move db]

    (describe "Storing Turns"
      (with-stubs)

      (it "saves the turn information to the database"
        (let [_ (update_ input-not-over)]
          (should-have-invoked :db {:with ["game-name" 7 5] :times 1})))

      (it "increments the turn counter"
        (let [output (update_ input-not-over)]
          (should= 8 (:turn-count output))))

      (it "alternates the mark from :X to :O"
        (let [output (update_ input-not-over)]
          (should= :O (:mark output))))

      (it "alternates the mark from :O to :X"
        (let [output (update_ input-mark-o)]
          (should= :X (:mark output))))

      (it "transitions back to the arena"
        (let [output (update_ input-not-over)]
          (should= :arena (:screen output))))

      )))
