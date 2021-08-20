(ns ttt.game-spec
  (:require [speclj.core :refer :all]
            [ttt.game :refer :all]
            [ttt.grid :refer :all]))

(describe "Game Play"

  (with-stubs)

  (context "A Single Tick"
    (it "updates game state after one move"
      (let [player1    (stub :player1 {:return 1})
            grid       (make-grid)
            game-state {:grid    grid
                        :mark    X
                        :player1 player1
                        :player2 :player2}
            result     (tick game-state)]
        (should= (place X 1 grid) (:grid result))
        (should= O (:mark result))
        (should-have-invoked :player1 {:with [X grid]})
        (should= player1 (:player2 result))
        (should= :player2 (:player1 result))
        (should= false (:game-over? result))
        (should-be-nil (:winner result))))

    (it "detects game-over in the case of a tie"
      (let [player1    (stub :player1 {:return 0})
            grid       [_ X O
                        O O X
                        X O O]
            game-state {:grid    grid
                        :mark    X
                        :player1 player1
                        :player2 :player2}
            result     (tick game-state)]
        (should= true (:game-over? result))
        (should-be-nil (:winner result))))

    (it "detects winner in the case of a win"
      (let [player1    (stub :player1 {:return 0})
            grid       [_ X O
                        O O X
                        X _ O]
            game-state {:grid    grid
                        :mark    O
                        :player1 player1
                        :player2 :player2}
            result     (tick game-state)]
        (should= true (:game-over? result))
        (should= O (:winner result))))
    )

  (context "The Game Loop (multiple ticks)"

    (it "ends when the game is over"
      (let [ticks  [{:grid "the-grid" :game-over? true :winner X}]
            winner (game-loop (stub :presenter) ticks)]
        (should= X winner)
        (should-have-invoked :presenter {:with ["the-grid"]})))

    (it "iterates until game is over"
      (let [ticks  [{:game-over? false :grid "grid1" :winner nil}
                    {:game-over? false :grid "grid2" :winner nil}
                    {:game-over? true, :grid "grid3" :winner O}]
            winner (game-loop (stub :presenter) ticks)]
        (should= O winner)
        (should-have-invoked :presenter {:with ["grid1"]})
        (should-have-invoked :presenter {:with ["grid2"]})
        (should-have-invoked :presenter {:with ["grid3"]})))
    )
  )
