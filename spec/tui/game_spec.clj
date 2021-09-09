(ns tui.game-spec
  (:require
    [speclj.core :refer :all]
    [ttt.grid :refer :all]
    [ttt.grid-spec :refer :all]
    [tui.game :refer :all]
    [db.datomic :as db]
    [tui.grid :as terminal]))

(describe "Game Play"

  (with-stubs)

  (context "A Single Tick"
    (it "updates and persists game state after one move"
      (let [persistence (stub :persistence)]
        (with-redefs [db/associate-move persistence]
          (let [player1    (stub :player1 {:return 1})
                grid       (new-grid 3)
                game-state {:grid         grid
                            :mark         X
                            :player1      player1
                            :player2      :player2
                            :game-name    "game-name"
                            :turn-counter 0}
                result     (tick game-state)]
            (should= (place X 1 grid) (:grid result))
            (should= O (:mark result))
            (should-have-invoked :player1 {:with [X grid]})
            (should-have-invoked :persistence {:with ["game-name" 0 1]})
            (should= 1 (:turn-counter result))
            (should= false (:game-over? result))
            (should-be-nil (:winner result))))))

    (it "detects game-over in the case of a tie"
      (let [persistence (stub :persistence)]
        (with-redefs [db/associate-move persistence]
          (let [player1    (stub :player1 {:return 0})
                grid       (vector->grid [_ X O
                                          O O X
                                          X O O])
                game-state {:grid    grid
                            :mark    X
                            :player1 player1
                            :player2 :player2
                            :turn-counter 0}
                result     (tick game-state)]
            (should= true (:game-over? result))
            (should-be-nil (:winner result))))))

    (it "detects winner in the case of a win"
      (let [persistence (stub :persistence)]
        (with-redefs [db/associate-move persistence]
          (let [player2    (stub :player2 {:return 0})
                grid       (vector->grid [_ X O
                                          O O X
                                          X _ O])
                game-state {:grid    grid
                            :mark    O
                            :player1 :player1
                            :player2 player2
                            :turn-counter 0}
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
          (let [ticks  [{:grid "the-grid" :game-over? true :winner X}]
                winner (game-loop ticks)]
            (should= X winner)
            (should-have-invoked :presenter {:with ["the-grid"]})))))

    (it "iterates until game is over"
      (let [persistence (stub :persistence)
            presenter   (stub :presenter)]
        (with-redefs [db/associate-move   persistence
                      terminal/print-grid presenter]
          (let [ticks  [{:game-over? false :grid "grid1" :winner nil}
                        {:game-over? false :grid "grid2" :winner nil}
                        {:game-over? true, :grid "grid3" :winner O}]
                winner (game-loop ticks)]
            (should= O winner)
            (should-have-invoked :presenter {:with ["grid1"]})
            (should-have-invoked :presenter {:with ["grid2"]})
            (should-have-invoked :presenter {:with ["grid3"]})))))
    )
  )
