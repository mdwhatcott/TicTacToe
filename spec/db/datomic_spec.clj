(ns db.datomic-spec
  (:require
    [speclj.core :refer :all]
    [db.datomic :refer :all]))

(defn empty-database [] (reset-db test-uri schema))

(describe "The Datomic Database"

  (it "establishes a new game"
    (with-redefs [conn (empty-database)]
      (let [_          (establish-new-game "game-1" 3 :human :easy)
            unfinished (get-unfinished-game)]
        (should= {:name       "game-1"
                  :grid-width 3
                  :x-player   :human
                  :o-player   :easy
                  :moves      []} unfinished))))

  (it "stores moves made with the corresponding game"
    (with-redefs [conn (empty-database)]
      (let [_             (establish-new-game "game-1" 3 :human :human)
            _             (establish-new-game "game-2" 3 :human :human)
            game-1-move-1 (associate-move "game-1" 0 2)
            game-1-move-2 (associate-move "game-1" 1 1)
            game-2-move-1 (associate-move "game-2" 0 5)
            _             (conclude-game "game-2")]
        (should= [2 1] (get-moves "game-1"))
        (should= {:name       "game-1"
                  :grid-width 3
                  :x-player   :human
                  :o-player   :human
                  :moves      [2 1]} (get-unfinished-game)))))

  (it "concludes a game"
    (with-redefs [conn (empty-database)]
      (let [_ (establish-new-game "game-1" 3 :human :human)
            _ (conclude-game "game-1")]
        (should= nil (get-unfinished-game)))))

  )
