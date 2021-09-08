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
    (with-redefs [now (fn [] "game-1-start")]
      (let [conn       (create-empty-in-memory-db)
            _          (establish-new-game conn "game-1" "3x3" true true)
            unfinished (get-unfinished-game conn)]
        (should= {:name     "game-1"
                  :grid     "3x3"
                  :x-human? true
                  :o-human? true} unfinished))))

  (it "stores moves made with the corresponding game"
    (let [conn          (create-empty-in-memory-db)
          _             (establish-new-game conn "game-1" "3x3" true true)
          _             (establish-new-game conn "game-2" "3x3" true true)
          game-1-move-1 (associate-move conn "game-1" 0 2)
          game-1-move-2 (associate-move conn "game-1" 1 1)
          game-2-move-1 (associate-move conn "game-2" 0 5)]
      (should= [2 1] (get-moves conn "game-1"))))

  (it "concludes a game"
    (let [conn (create-empty-in-memory-db)
          _    (establish-new-game conn "game-1" "3x3" true true)
          _    (conclude-game conn "game-1")]
      (should= nil (get-unfinished-game conn))))

  )
