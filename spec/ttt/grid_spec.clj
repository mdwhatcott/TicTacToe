(ns ttt.grid-spec
  (:require [speclj.core :refer :all]
            [ttt.grid :refer :all]))

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
  (apply str (map #(if (nil? %) "-" %) grid)))

(describe "Grid Data Structure"
  (context "Upon Construction (3X3)"
    (it "contains metadata to facilitate book-keeping"
      (let [grid (new-grid 3)]
        (should= 3 (:row-count grid))
        (should= 3 (:col-count grid))
        (should= #{0 1 2 3 4 5 6 7 8} (:empty-cells grid))
        (should= {} (:filled-by-cell grid))
        (should= {X #{}
                  O #{}} (:filled-by-mark grid))))

    (it "precomputes all winning combinations"
      (let [grid (new-grid 3)]
        (->> (:wins grid)
             (should= [#{0 1 2}                             ; row 1
                       #{3 4 5}                             ; row 2
                       #{6 7 8}                             ; row 3
                       #{0 3 6}                             ; col 1
                       #{1 4 7}                             ; col 2
                       #{2 5 8}                             ; col 3
                       #{0 4 8}                             ; dia 1
                       #{2 4 6}                             ; dia 2
                       ]))))
    )

  (context "Upon Construction (4X4)"
    (it "contains metadata to facilitate book-keeping"
      (let [grid (new-grid 4)]
        (should= 4 (:row-count grid))
        (should= 4 (:col-count grid))
        (should= #{0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15} (:empty-cells grid))
        (should= {} (:filled-by-cell grid))
        (should= {X #{}
                  O #{}} (:filled-by-mark grid))))

    (it "precomputes all winning combinations"
      (let [grid (new-grid 4)]
        (->> (:wins grid)
             (should= [#{0 1 2 3}                           ; row 1
                       #{4 5 6 7}                           ; row 2
                       #{8 9 10 11}                         ; row 3
                       #{12 13 14 15}                       ; row 4
                       #{0 4 8 12}                          ; col 1
                       #{1 5 9 13}                          ; col 2
                       #{2 6 10 14}                         ; col 3
                       #{3 7 11 15}                         ; col 4
                       #{0 5 10 15}                         ; dia 1
                       #{3 6 9 12}                          ; dia 2
                       ]))))
    )

  (context "Mark Placement"
    (it "manages book-keeping associated with placements"
      (let [grid    (new-grid 3)
            updated (place X 0 grid)]
        (should= {X #{0} O #{}} (:filled-by-mark updated))
        (should= {0 X} (:filled-by-cell updated))
        (should= #{1 2 3 4 5 6 7 8} (:empty-cells updated))))

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
    )

  (context "Win Detection"
    (for [grid no-winners]
      (it (str "identifies non-winning conditions " (render grid))
        (should-be-nil (winner (vector->grid grid)))))

    (for [grid winners-x]
      (it (str "identifies winning conditions for X " (render grid))
        (should= X (winner (vector->grid grid)))))

    (for [grid winners-o]
      (it (str "identifies winning conditions for O " (render grid))
        (should= O (winner (vector->grid grid)))))
    )
  )