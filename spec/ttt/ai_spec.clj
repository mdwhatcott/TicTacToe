(ns ttt.ai-spec
  (:require [speclj.core :refer :all]
            [ttt.grid :refer :all]
            [ttt.grid-spec :refer :all]
            [ttt.ai :refer :all]
            [ttt.game :refer :all]))

(describe "AI"

  (context "Hard/Unbeatable"

    (it "takes the center if available on the second move"
      (should= 4 (hard O (vector->grid [X _ _
                                        _ _ _
                                        _ _ _]))))
    (it "suggests the only remaining (non-winning) move"
      (should= 8 (hard X (vector->grid [O X X
                                        X O X
                                        X O _]))))

    (it "suggests the winning move from two options"
      (should= 8 (hard X (vector->grid [O X X
                                        X O X
                                        X _ _]))))

    (it "suggests the winning move from many options"
      (should= 6 (hard X (vector->grid [X O O
                                        X _ _
                                        _ _ _]))))

    (it "suggests a winning move to prevent subsequent loss"
      (should= 6 (hard O (vector->grid [X X _
                                        _ X _
                                        _ O O]))))

    (it "suggests a blocking move to prevent subsequent loss"
      (should= 6 (hard O (vector->grid [_ _ X
                                        _ X _
                                        _ _ O])))

      (should= 6 (hard O (vector->grid [_ O X
                                        _ X _
                                        _ _ _])))

      (should= 5 (hard O (vector->grid [O _ _
                                        X X _
                                        _ O X]))))

    (it "suggests the best move available, such as a fork"
      (should= 4 (hard X (vector->grid [X X O
                                        O _ _
                                        _ _ _]))))

    ;; LONG-RUNNING
    #_(for [first-move (range 9)]
      (it (format "can't beat itself when starting with X on cell %d" first-move)
        (should-be-nil
          (play (fn [_])
                (place X first-move (new-grid 3))
                O
                hard
                hard)))))
  )

