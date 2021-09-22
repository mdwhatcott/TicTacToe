(ns ttt.grid-spec
  (:require
    [speclj.core :refer :all]
    [ttt.grid :refer :all]))

(def _ nil)
(def X :X)
(def O :O)

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

;; Transforms a simple vector representing a grid
;; (like those above) into a grid structure.
(defn vector->grid [v]
  (loop [v    v
         on   0
         grid (new-grid (int (Math/sqrt (count v))))]
    (if (empty? v)
      grid
      (recur (rest v)
             (inc on)
             (place (first v) on grid)))))

(defn render [grid]
  (->> grid
       (map {nil "-" :X "X" :O "O"})
       (apply str)))

(describe "Grid Data Structure"
  (context "Upon Construction (3X3)"
    (it "contains metadata to facilitate book-keeping"
      (let [grid (new-grid 3)]
        (should= false (:game-over? grid))
        (should= nil (:winner grid))
        (should= 3 (:width grid))
        (should= 9 (:capacity grid))
        (should= [] (:moves grid))
        (should= #{0 1 2 3 4 5 6 7 8} (:empty-cells grid))
        (should= {} (:filled-by-cell grid))
        (should= {X #{}
                  O #{}} (:filled-by-mark grid))))

    (it "precomputes all winning combinations"
      (let [grid (new-grid 3)]
        (->> (:wins-by-cell grid)
             (should= {0 [#{0 1 2} #{0 6 3} #{0 4 8}]
                       1 [#{0 1 2} #{7 1 4}]
                       2 [#{0 1 2} #{2 5 8} #{4 6 2}]
                       3 [#{4 3 5} #{0 6 3}]
                       4 [#{4 3 5} #{7 1 4} #{0 4 8} #{4 6 2}]
                       5 [#{4 3 5} #{2 5 8}]
                       6 [#{7 6 8} #{0 6 3} #{4 6 2}]
                       7 [#{7 6 8} #{7 1 4}]
                       8 [#{7 6 8} #{2 5 8} #{0 4 8}]}))))
    )

  (context "Upon Construction (4X4)"
    (it "contains metadata to facilitate book-keeping"
      (let [grid (new-grid 4)]
        (should= false (:game-over? grid))
        (should= nil (:winner grid))
        (should= 4 (:width grid))
        (should= [] (:moves grid))
        (should= #{0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15}
                 (:empty-cells grid))
        (should= {} (:filled-by-cell grid))
        (should= {X #{}
                  O #{}} (:filled-by-mark grid))))

    (it "precomputes all winning combinations"
      (let [grid (new-grid 4)]
        (->> (:wins-by-cell grid)
             (should= {0  [#{0 1 3 2} #{0 4 12 8} #{0 15 5 10}]
                       1  [#{0 1 3 2} #{1 13 9 5}]
                       2  [#{0 1 3 2} #{6 2 14 10}]
                       3  [#{0 1 3 2} #{7 15 3 11} #{6 3 12 9}]
                       4  [#{7 4 6 5} #{0 4 12 8}]
                       5  [#{7 4 6 5} #{1 13 9 5} #{0 15 5 10}]
                       6  [#{7 4 6 5} #{6 2 14 10} #{6 3 12 9}]
                       7  [#{7 4 6 5} #{7 15 3 11}]
                       8  [#{11 9 10 8} #{0 4 12 8}]
                       9  [#{11 9 10 8} #{1 13 9 5} #{6 3 12 9}]
                       10 [#{11 9 10 8} #{6 2 14 10} #{0 15 5 10}]
                       11 [#{11 9 10 8} #{7 15 3 11}]
                       12 [#{15 13 12 14} #{0 4 12 8} #{6 3 12 9}]
                       13 [#{15 13 12 14} #{1 13 9 5}]
                       14 [#{15 13 12 14} #{6 2 14 10}]
                       15 [#{15 13 12 14} #{7 15 3 11} #{0 15 5 10}]}))))
    )

  (context "Mark Placement"
    (it "manages book-keeping associated with placements"
      (let [grid    (new-grid 3)
            updated (place X 0 grid)]
        (should= false (:game-over? grid))
        (should= nil (:winner grid))
        (should= {X #{0} O #{}} (:filled-by-mark updated))
        (should= {0 X} (:filled-by-cell updated))
        (should= #{1 2 3 4 5 6 7 8} (:empty-cells updated))
        (should= [0] (:moves updated))))

    (it "detects game-over because of winning conditions with each placement"
      (let [grid    (vector->grid [_ O O
                                   _ X X
                                   _ _ _])
            updated (place O 0 grid)]

        (should= false (:game-over? grid))
        (should= true (:game-over? updated))

        (should= nil (:winner grid))
        (should= O (:winner updated))))

    (it "detects game-over because of drawn game conditions with each placement"
      (let [grid    (vector->grid [_ X O
                                   X O O
                                   X O X])
            updated (place O 0 grid)]

        (should= false (:game-over? grid))
        (should= true (:game-over? updated))

        (should= nil (:winner grid))
        (should= nil (:winner updated))))

    (it "rejects out-of-bounds placements (too low)"
      (let [grid    (new-grid 3)
            updated (place X -1 grid)]
        (should= grid updated)))

    (it "rejects out-of-bounds placements (too high)"
      (let [grid    (new-grid 3)
            updated (place X (inc (count (:empty-cells grid))) grid)]
        (should= grid updated)))

    (it "rejects placements that would overwrite previous placements"
      (let [grid      (new-grid 3)
            updated-x (place X 1 grid)
            updated-o (place O 1 updated-x)]
        (should= {X #{1} O #{}} (:filled-by-mark updated-o))
        (should= {1 X} (:filled-by-cell updated-o))))

    (it "rejects placements after the game is over"
      (let [grid    (vector->grid [O O O
                                   X _ X
                                   _ _ _])
            updated (place X 4 grid)]
        (should= grid updated)))
    )

  (context "Win Detection"
    (for [grid no-winners]
      (it (str "identifies non-winning conditions " (render grid))
        (should= nil (:winner (vector->grid grid)))))

    (for [grid winners-x]
      (it (str "identifies winning conditions for X " (render grid))
        (should= X (:winner (vector->grid grid)))))

    (for [grid winners-o]
      (it (str "identifies winning conditions for O " (render grid))
        (should= O (:winner (vector->grid grid)))))
    )
  )