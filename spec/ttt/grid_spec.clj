(ns ttt.grid-spec
  (:require [speclj.core :refer :all]
            [ttt.grid :refer :all]))

(def _ nil)

(def no-winners
  [
   [_ _ _
    _ _ _
    _ _ _],

   [X _ _
    _ _ _
    _ _ _],

   [X X _
    _ _ _
    _ _ _],

   [_ X _
    _ _ _
    _ _ _],

   [_ _ X
    _ _ _
    _ _ _],

   [_ X X
    _ _ _
    _ _ _],

   [_ _ _
    X _ _
    _ _ _],

   [O _ _
    _ _ _
    _ _ _],

   [O O _
    _ _ _
    _ _ _],

   [_ O _
    _ _ _
    _ _ _],

   [_ _ O
    _ _ _
    _ _ _],

   [_ O O
    _ _ _
    _ _ _],

   [_ _ _
    O _ _
    _ _ _],

   ])

(def winners-x
  [
   [X X X
    _ _ _
    _ _ _],

   [_ _ _
    X X X
    _ _ _],

   [_ _ _
    _ _ _
    X X X],

   [X _ _
    X _ _
    X _ _],

   [_ X _
    _ X _
    _ X _],

   [_ _ X
    _ _ X
    _ _ X],

   [X _ _
    _ X _
    _ _ X],

   [_ _ X
    _ X _
    X _ _],

   [X O O
    O X X
    X O X],

   ])

(def winners-o
  [
   [O O O
    _ _ _
    _ _ _],

   [_ _ _
    O O O
    _ _ _],

   [_ _ _
    _ _ _
    O O O],

   [O _ _
    O _ _
    O _ _],

   [_ O _
    _ O _
    _ O _],

   [_ _ O
    _ _ O
    _ _ O],

   [O _ _
    _ O _
    _ _ O],

   [_ _ O
    _ O _
    O _ _],

   [X O O
    O O X
    X O X],

   ])

(describe "Grid Mechanics"

  (it "starts with 9 empty spots"
    (->> (make-grid) (should= [_ _ _
                               _ _ _
                               _ _ _])))

  (it "places Xs"
    (->> (make-grid)
         (place-x 0)
         (should= [X _ _
                   _ _ _
                   _ _ _])))

  (it "places Os"
    (->> (make-grid)
         (place-o 0)
         (should= [O _ _
                   _ _ _
                   _ _ _])))

  (it "preserves Xs"
    (->> (make-grid)
         (place-x 0)
         (place-o 0)
         (should= [X _ _
                   _ _ _
                   _ _ _])))

  (it "preserves Os"
    (->> (make-grid)
         (place-o 0)
         (place-x 0)
         (should= [O _ _
                   _ _ _
                   _ _ _])))

  (it "ignores out-of-bounds"
    (let [empty (make-grid)]
      (->> empty
           (place-o (inc (count empty)))
           (should= empty))))

  )

(defn render [grid]
  (clojure.string/join "" (map #(if (nil? %) "-" %) grid)))

(describe "Winning Conditions"
  (map #(it (str "identifies non-winning conditions " (render %))
          (should-be-nil (winner %))) no-winners)

  (map #(it (str "identifies winning conditions for X " (render %))
          (should= X (winner %))) winners-x)

  (map #(it (str "identifies winning conditions for O " (render %))
          (should= O (winner %))) winners-o))

(describe "Grid Utilities"
  (it "converts rows to columns"
    (->> [O X O
          O X O
          O X O]
         (partition 3)
         rows->columns
         flatten
         (should= [O O O
                   X X X
                   O O O])))

  (it "converts rows to right-leaning diagonals"
    (->> [O X _
          X _ X
          _ X O]
         (partition 3)
         rows->diagonals
         (should= [[O]
                   [X X]
                   [_ _ _]
                   [X X]
                   [O]])))

  (it "converts rows to left-leaning diagonals"
    (->> [_ X O
          X _ X
          O X _]
         (partition 3)
         rows->columns
         rows->diagonals
         (should= [[O]
                   [X X]
                   [_ _ _]
                   [X X]
                   [O]])))
  )