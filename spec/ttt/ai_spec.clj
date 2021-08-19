(ns ttt.ai-spec
  (:require [speclj.core :refer :all]
            [ttt.grid :refer :all]
            [ttt.ai :refer :all]))

(describe "Unbeatable AI"

  (it "suggests the only remaining (non-winning) move"
    (should= 8 (suggest X [O X X
                           X O X
                           X O _])))

  (it "suggests the winning move from two options"
    (should= 8 (suggest X [O X X
                           X O X
                           X _ _])))

  (it "suggests the winning move from many options"
    (should= 6 (suggest X [X O O
                           X _ _
                           _ _ _])))

  (it "suggests a winning move to prevent subsequent loss"
    (should= 6 (suggest O [X X _
                           _ X _
                           _ O O])))

  (it "suggests a blocking move to prevent subsequent loss"
    (should= 6 (suggest O [_ _ X
                           _ X _
                           _ _ O])))

  (it "suggests the best move available, such as a fork"
    (should= 4 (suggest X [X X O
                           O _ _
                           _ _ _])))

  (it "shouldn't let me win"
    (should= 6 (suggest O [_ O X
                           _ X _
                           _ _ _])))

  (it "shouldn't let me win 2"
    (should= 5 (suggest O [O _ _
                           X X _
                           _ O X])))

  )

