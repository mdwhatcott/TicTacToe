(ns gui.arena-spec
  (:require
    [speclj.core :refer :all]
    [gui.arena :refer :all]
    [gui.common :as common]
    [ttt.grid :as grid]
    [ttt.grid-spec :as grid-spec]
    [ttt.ai :as ai]))

(def fake-players
  {:easy   (fn [_mark _grid] 1)
   :medium (fn [_mark _grid] 2)
   :hard   (fn [_mark _grid] 3)})

(describe "Screen: The Gameplay Arena"

  (with-stubs)

  (it "waits for a click when already in game-over state"
    (let [input  {:mouse     {:clicked? false}
                  :game-grid {:game-over? true}}
          output (update_ input)]
      (should= input output)))

  (context "Human Players Taking Turns"

    (it "highlights cell under cursor"
      (let [input  {:mouse     {:clicked? false :x 1 :y 1}
                    :mark      :X
                    :player1   :human
                    :game-grid (grid/new-grid 2)
                    :gui-grid  (common/assemble-grid-cells 2 [0 0] [4 4])}
            output (update_ input)
            cell   (first (:gui-grid output))]
        (should= true (:hovering? cell))
        (should= :X (:mark cell))))

    (it "highlights no cells when cursor is off-grid"
      (let [input  {:mouse     {:clicked? false :x -1 :y 10}
                    :mark      :X
                    :player1   :human
                    :game-grid (grid/new-grid 2)
                    :gui-grid  (common/assemble-grid-cells 2 [0 0] [4 4])}
            output (update_ input)]
        (should= input output)))

    (it "places mark on game grid and gui corresponding with clicked cell"
      (let [input          {:mouse     {:clicked? true :x 1 :y 1}
                            :mark      :X
                            :player1   :human
                            :game-grid (grid/new-grid 2)
                            :gui-grid  (common/assemble-grid-cells 2 [0 0] [4 4])}
            output         (update_ input)
            gui-marks      (map :mark (:gui-grid output))
            gui-highlights (map :hovering? (:gui-grid output))
            grid-marks     (:filled-by-cell (:game-grid output))]
        (should= (:last-move output) 0)
        (should= [:X nil nil nil] gui-marks)
        (should= [false false false false] gui-highlights)
        (should= {0 :X} grid-marks)))

    (it "clears all highlights after placing a mark"
      (let [gui-grid   (vec (common/assemble-grid-cells 2 [0 0] [4 4]))
            gui-grid   (assoc-in gui-grid [0 :hovering?] true)
            input      {:mouse     {:clicked? true :x 1 :y 1}
                        :mark      :X
                        :player1   :human
                        :game-grid (grid/new-grid 2)
                        :gui-grid  gui-grid}
            output     (update_ input)
            highlights (map :hovering? (:gui-grid output))]
        (should= [false false false false] highlights)))

    (it "doesn't highlight previously marked cells, even if covered by the cursor"
      (let [game-grid      (->> (grid/new-grid 2)
                                (grid/place :X 0)
                                (grid/place :O 1))
            input          {:mouse     {:clicked? false :x 1 :y 1}
                            :mark      :X
                            :player1   :human
                            :game-grid game-grid
                            :gui-grid  (common/assemble-grid-cells 2 [0 0] [4 4])}
            output         (update_ input)
            gui-highlights (map :hovering? (:gui-grid output))]
        (should= [false false false false] gui-highlights)))
    )

  (context "AI Players Taking Turns"

    (it "places :X on game grid and gui corresponding with ai suggestion"
      (with-redefs [ai/players fake-players]
        (let [input      {:mouse     {:clicked? false :x 1 :y 1}
                          :mark      :X
                          :player1   :easy
                          :game-grid (grid/new-grid 2)
                          :gui-grid  (common/assemble-grid-cells 2 [0 0] [4 4])}
              output     (update_ input)
              gui-marks  (map :mark (:gui-grid output))
              grid-marks (:filled-by-cell (:game-grid output))]
          (should= (:last-move output) 1)
          (should= [nil :X nil nil] gui-marks)
          (should= {1 :X} grid-marks))))

    (it "places :O on game grid and gui corresponding with ai suggestion"
      (with-redefs [ai/players fake-players]
        (let [game-grid  (->> (grid/new-grid 2)
                              (grid/place :X 3))
              input      {:mouse     {:clicked? false :x 1 :y 1}
                          :mark      :O
                          :player2   :easy
                          :game-grid game-grid
                          :gui-grid  (common/assemble-grid-cells 2 [0 0] [4 4])}
              output     (update_ input)
              gui-marks  (map :mark (:gui-grid output))
              grid-marks (:filled-by-cell (:game-grid output))]
          (should= [nil :O nil :X] gui-marks)
          (should= {1 :O, 3 :X} grid-marks))))

    )

  (context "Taking Turns (in general)"

    (it "flags winning and losing marks when a player achieves tic-tac-toe"
      (with-redefs [ai/players fake-players]
        (let [game-grid (->> (grid/new-grid 2)
                             (grid/place :X 0)
                             (grid/place :O 3))
              input     {:mouse     {:clicked? false :x 1 :y 1}
                         :mark      :X
                         :player1   :easy
                         :game-grid game-grid
                         :gui-grid  (common/assemble-grid-cells 2 [0 0] [4 4])}
              output    (update_ input)
              gui-marks (map :mark (:gui-grid output))
              winners   (map :winner? (:gui-grid output))
              losers    (map :loser? (:gui-grid output))
              tied      (map :tied? (:gui-grid output))]
          (should= [:X,,, :X,,, nil,, :O,,,] gui-marks)
          (should= [true, true, false false] winners)
          (should= [false false true, true,] losers)
          (should= [false false false false] tied))))

    (it "flags all placed marks as losing when a the game is drawn"
      (with-redefs [ai/players fake-players]
        (let [game-grid (grid-spec/vector->grid [:O :O nil
                                                 :X :X :O
                                                 :O :X :X])
              input     {:mouse     {:clicked? false :x 1 :y 1}
                         :mark      :X
                         :player1   :medium
                         :game-grid game-grid
                         :gui-grid  (common/assemble-grid-cells 3 [0 0] [4 4])}
              output    (update_ input)
              gui-marks (map :mark (:gui-grid output))
              winners   (map :winner? (:gui-grid output))
              losers    (map :loser? (:gui-grid output))
              tied      (map :tied? (:gui-grid output))]
          (should= [:O :O :X
                    :X :X :O
                    :O :X :X] gui-marks)
          (should= (repeat 9 false) winners)
          (should= (repeat 9 false) losers)
          (should= (repeat 9 true) tied))))

    )
  )
