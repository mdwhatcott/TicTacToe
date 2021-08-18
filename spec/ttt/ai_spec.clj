(ns ttt.ai-spec
  (:require [speclj.core :refer :all]
            [ttt.grid :refer :all]
            [ttt.ai :refer :all]))

(describe "Unbeatable AI"

  ;(it "chooses the only remaining position"
  ;  (should= 8 (suggest X [O X X
  ;                         X O X
  ;                         X O _])))

  ;(it "chooses the winning position from two options"
  ;  (should= 8 (suggest X [O X X
  ;                         X O X
  ;                         X _ _])))

  ;(it "chooses the winning position from many options"
  ;  (should= 6 (suggest X [X O O
  ;                         X _ _
  ;                         _ _ _])))

  )
