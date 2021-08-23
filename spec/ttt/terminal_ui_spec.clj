(ns ttt.terminal-ui-spec
  (:require [speclj.core :refer :all]
            [ttt.terminal-ui :refer :all]
            [ttt.grid :refer :all]
            [ttt.grid-spec :refer :all]))

(describe "Console Rendering"
  (context "3x3 Grids"
    (it "renders the grid"
      (->> (vector->grid [O _ X
                          _ O _
                          _ X _])
           render-grid
           (should= (str "O| |X  |2| \n"
                         "-+-+- -+-+-\n"
                         " |O|  4| |6\n"
                         "-+-+- -+-+-\n"
                         " |X|  7| |9\n"))))


    (it "renders the winner with the grid"
      (->> (vector->grid [O _ X
                          _ O _
                          _ X O])
           render-grid
           (should= (str "O| |X  |2| \n"
                         "-+-+- -+-+-\n"
                         " |O|  4| |6\n"
                         "-+-+- -+-+-\n"
                         " |X|O 7| | \n"
                         "\n"
                         "Winner: O\n")))))

  )
