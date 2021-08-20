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
             (place2 (first v) on grid)))))

#_(describe "Grid Mechanics"

  (it "starts with 9 empty spots"
    (->> (make-grid) (should= [_ _ _
                               _ _ _
                               _ _ _])))

  (it "places Xs"
    (->> (make-grid)
         (place X 0)
         (should= [X _ _
                   _ _ _
                   _ _ _])))

  (it "places Os"
    (->> (make-grid)
         (place O 0)
         (should= [O _ _
                   _ _ _
                   _ _ _])))

  (it "preserves Xs"
    (->> (make-grid)
         (place X 0)
         (place O 0)
         (should= [X _ _
                   _ _ _
                   _ _ _])))

  (it "preserves Os"
    (->> (make-grid)
         (place O 0)
         (place X 0)
         (should= [O _ _
                   _ _ _
                   _ _ _])))

  (it "ignores out-of-bounds"
    (let [empty (make-grid)]
      (->> empty
           (place O (inc (count empty)))
           (should= empty)))))

(defn render [grid]
  (apply str (map #(if (nil? %) "-" %) grid)))

#_(describe "Winning Conditions"
  (for [grid no-winners]
    (it (str "identifies non-winning conditions " (render grid))
      (should-be-nil (winner grid))))

  (for [grid winners-x]
    (it (str "identifies winning conditions for X " (render grid))
      (should= X (winner grid))))

  (for [grid winners-o]
    (it (str "identifies winning conditions for O " (render grid))
      (should= O (winner grid)))))

#_(describe "Grid Scanning"
  (it "identifies all available spots on an empty grid"
    (->> (make-grid)
         available-cells
         (should= [0 1 2 3 4 5 6 7 8])))

  (it "identifies all available spots on a partially filled grid"
    (->> (make-grid)
         (place X 0)
         (place O 3)
         (place X 4)
         (place O 8)
         available-cells
         (should= [1 2 5 6 7])))

  (it "identifies no available spots on a filled-in grid"
    (->> [O X O
          X O O
          X O X]
         available-cells
         (should= []))))

(describe "Grid Data Structure"
  (context "Upon Construction"
    (it "contains metadata to facilitate book-keeping"
      (let [grid (new-grid 3)]
        (should= 3 (:row-count grid))
        (should= 3 (:col-count grid))
        (should= 9 (:empty-cell-count grid))
        (should= #{0 1 2 3 4 5 6 7 8} (:empty-cells grid))
        (should= {} (:filled-by-cell grid))
        (should= {X #{}
                  O #{}} (:filled-by-mark grid))))

    (it "precomputes all winning combinations"
      (let [grid (new-grid 3)]
        (->> (:wins grid)
             (should= [#{0 1 2} ; row 1
                       #{3 4 5} ; row 2
                       #{6 7 8} ; row 3
                       #{0 3 6} ; col 1
                       #{1 4 7} ; col 2
                       #{2 5 8} ; col 3
                       #{0 4 8} ; dia 1
                       #{2 4 6} ; dia 2
                       ]))))
    )

  (context "Mark Placement"
    (it "manages book-keeping associated with placements"
      (let [grid    (new-grid 3)
            updated (place2 X 0 grid)]
        (should= {X #{0} O #{}} (:filled-by-mark updated))
        (should= {0 X} (:filled-by-cell updated))
        (should= #{1 2 3 4 5 6 7 8} (:empty-cells updated))
        (should= 8 (:empty-cell-count updated))))

    (it "rejects out-of-bounds placements (too low)"
      (let [grid    (new-grid 3)
            updated (place2 X -1 grid)]
        (should= grid updated)))

    (it "rejects out-of-bounds placements (too high)"
      (let [grid    (new-grid 3)
            updated (place2 X (inc (:empty-cell-count grid)) grid)]
        (should= grid updated)))

    (it "rejects placements that would overwrite previous placements"
      (let [grid      (new-grid 3)
            updated-x (place2 X 1 grid)
            updated-o (place2 O 1 updated-x)]
        (should= {X #{1} O #{}} (:filled-by-mark updated-o))
        (should= {1 X} (:filled-by-cell updated-o))
        (should= 8 (:empty-cell-count updated-o))))
    )

  (context "Win Detection"
    (for [grid no-winners]
      (it (str "identifies non-winning conditions " (render grid))
        (should-be-nil (winner2 (vector->grid grid)))))

    (for [grid winners-x]
      (it (str "identifies winning conditions for X " (render grid))
        (should= X (winner2 (vector->grid grid)))))

    (for [grid winners-o]
      (it (str "identifies winning conditions for O " (render grid))
        (should= O (winner2 (vector->grid grid)))))
    )
  )