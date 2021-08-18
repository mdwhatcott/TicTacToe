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

  (it "suggests the best move available (fork)"
    (should= 4 (suggest O [X X O
                           O _ _
                           _ _ _])))

  )

