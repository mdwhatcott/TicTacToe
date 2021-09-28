(ns tui.game-spec
  (:require
    [speclj.core :refer :all]
    [ttt.grid :refer :all]
    [tui.game :refer :all]
    [db.datomic :as db]
    [tui.grid :as terminal]))

(def fake-players
  {:human  (fn [_mark _grid] 0)
   :easy   (fn [_mark _grid] 1)
   :medium (fn [_mark _grid] 2)
   :hard   (fn [_mark _grid] 3)})

(def O :O)
(def X :X)
(def _ nil)

(describe "Game Play"

  (with-stubs)

  (context "A Single Tick"
    (it "updates and persists game state after one move"
      (let [persistence (stub :persistence)]
        (with-redefs [db/associate-move persistence
                      players           fake-players]
          (let [grid       (new-grid 3)
                game-state {:game-grid         grid
                            :mark         X
                            :player1      :easy
                            :player2      :medium
                            :game-name    "game-name"
                            :turn-count 0}
                result     (tick game-state)]
            (should= (place X 1 grid) (:game-grid result))
            (should= O (:mark result))
            (should-have-invoked :persistence {:with ["game-name" 0 1]})
            (should= 1 (:turn-count result))
            (should= false (:game-over? result))
            (should-be-nil (:winner result))))))

    (it "detects game-over in the case of a tie"
      (let [persistence (stub :persistence)]
        (with-redefs [db/associate-move persistence
                      players           fake-players]
          (let [grid       (vector->grid [_ X O
                                          O O X
                                          X O O])
                game-state {:game-grid         grid
                            :mark         X
                            :player1      :human
                            :player2      :easy
                            :turn-count 0}
                result     (tick game-state)]
            (should= true (:game-over? result))
            (should-be-nil (:winner result))))))

    (it "detects winner in the case of a win"
      (let [persistence (stub :persistence)]
        (with-redefs [db/associate-move persistence
                      players           fake-players]
          (let [grid       (vector->grid [_ X O
                                          O O X
                                          X _ O])
                game-state {:game-grid         grid
                            :mark         O
                            :player1      :easy
                            :player2      :human
                            :turn-count 0}
                result     (tick game-state)]
            (should= true (:game-over? result))
            (should= O (:winner result))))))
    )

  (context "The Game Loop (multiple ticks)"

    (it "ends when the game is over"
      (let [persistence (stub :persistence)
            presenter   (stub :presenter)]
        (with-redefs [db/associate-move   persistence
                      terminal/print-grid presenter]
          (let [ticks  [{:game-grid "the-grid" :game-over? true :winner X}]
                winner (game-loop ticks)]
            (should= X winner)
            (should-have-invoked :presenter {:with ["the-grid"]})))))

    (it "iterates until game is over"
      (let [persistence (stub :persistence)
            presenter   (stub :presenter)]
        (with-redefs [db/associate-move   persistence
                      terminal/print-grid presenter]
          (let [ticks  [{:game-over? false :game-grid "grid1" :winner nil}
                        {:game-over? false :game-grid "grid2" :winner nil}
                        {:game-over? true, :game-grid "grid3" :winner O}]
                winner (game-loop ticks)]
            (should= O winner)
            (should-have-invoked :presenter {:with ["grid1"]})
            (should-have-invoked :presenter {:with ["grid2"]})
            (should-have-invoked :presenter {:with ["grid3"]})))))
    )
  )
