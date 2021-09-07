(ns db.datomic-spec
  (:require
    [speclj.core :refer :all]
    [db.datomic :refer :all]
    [datomic.api :as d]))

(def test-uri "datomic:mem://ttt-test-db")
(defn create-empty-in-memory-db []
  (let [schema "resources/datomic/schema.edn"]
    (reset-db test-uri schema)))

(describe "The Database"
  (with-stubs)

  (it "executes the schema to initialize a fresh database"
    (create-empty-in-memory-db))

  (it "establishes a new game"
    (with-redefs [now (fn [] "game-1-start")]
      (let [conn       (create-empty-in-memory-db)
            game-id    (establish-new-game conn "game-1")
            unfinished (get-unfinished-games conn)]
        (should= #{[game-id]} unfinished))))

  (it "stores moves made with the corresponding game"
    (let [conn          (create-empty-in-memory-db)
          game-1        (establish-new-game conn "game-1")
          game-2        (establish-new-game conn "game-2")
          game-1-move-1 (associate-move conn game-1 0 2)
          game-1-move-2 (associate-move conn game-1 1 1)
          game-2-move-1 (associate-move conn game-2 0 5)
          moves         (get-moves conn game-1)]
      (should= [2 1] moves)))

  (it "concludes a game"
    (let [conn       (create-empty-in-memory-db)
          _          (establish-new-game conn "game-1")
          _          (conclude-game conn "game-1")
          unfinished (get-unfinished-games conn)]
      (should= #{} unfinished)))

  )
