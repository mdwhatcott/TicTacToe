(ns db.datomic-spec
  (:require
    [speclj.core :refer :all]
    [db.datomic :refer :all]
    [datomic.api :as d]))

(def test-uri "datomic:mem://ttt-test-db")
(defn create-empty-in-memory-db []
  (let [schema "resources/datomic/schema.edn"]
    (reset-db test-uri schema)))

(describe "The Datomic Database"
  (it "executes the schema to initialize a fresh database"
    (create-empty-in-memory-db))

  (it "establishes a new game"
    (let [conn       (create-empty-in-memory-db)
          _          (establish-new-game conn "game-1" 3 :human :easy)
          unfinished (get-unfinished-game conn)]
      (should= {:name       "game-1"
                :grid-width 3
                :x-player   :human
                :o-player   :easy
                :moves      []} unfinished)))

  (it "stores moves made with the corresponding game"
    (let [conn          (create-empty-in-memory-db)
          _             (establish-new-game conn "game-1" 3 :human :human)
          _             (establish-new-game conn "game-2" 3 :human :human)
          game-1-move-1 (associate-move conn "game-1" 0 2)
          game-1-move-2 (associate-move conn "game-1" 1 1)
          game-2-move-1 (associate-move conn "game-2" 0 5)
          _             (conclude-game conn "game-2")]
      (should= [2 1] (get-moves conn "game-1"))
      (should= {:name       "game-1"
                :grid-width 3
                :x-player   :human
                :o-player   :human
                :moves      [2 1]} (get-unfinished-game conn))))

  (it "concludes a game"
    (let [conn (create-empty-in-memory-db)
          _    (establish-new-game conn "game-1" 3 :human :human)
          _    (conclude-game conn "game-1")]
      (should= nil (get-unfinished-game conn))))

  )
