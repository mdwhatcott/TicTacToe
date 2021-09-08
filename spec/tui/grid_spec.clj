(ns tui.grid-spec
  (:require
    [speclj.core :refer :all]
    [ttt.grid :refer :all]
    [ttt.grid-spec :refer :all]
    [tui.grid :refer :all]))

(describe "Console Rendering"
  (context "3x3 Grids"
    (it "renders the grid"
      (->> (vector->grid [O _ X
                          _ O _
                          _ X _])
           render-grid
           (should= (str "O| |X   |2| \n"
                         "-+-+-  -+-+-\n"
                         " |O|   4| |6\n"
                         "-+-+-  -+-+-\n"
                         " |X|   7| |9\n"))))


    (it "renders the winner with the grid"
      (->> (vector->grid [O _ X
                          _ O _
                          _ X O])
           render-grid
           (should= (str "O| |X   |2| \n"
                         "-+-+-  -+-+-\n"
                         " |O|   4| |6\n"
                         "-+-+-  -+-+-\n"
                         " |X|O  7| | \n"
                         "\n"
                         "Winner: O\n"))))

    (it "renders no winner for drawn game"
      (->> (vector->grid [O X X
                          X X O
                          O O X])
           render-grid
           (should= (str "O|X|X   | | \n"
                         "-+-+-  -+-+-\n"
                         "X|X|O   | | \n"
                         "-+-+-  -+-+-\n"
                         "O|O|X   | | \n"
                         "\n"
                         "Winner: none\n"))))
    )

  (context "4x4 Grids"
    (it "renders the grid"
      (->> (vector->grid [X _ X _
                          _ O _ O
                          _ X X _
                          _ O O _])
           render-grid
           (should= (str "X| |X|    |2| |4\n"
                         "-+-+-+-  -+-+-+-\n"
                         " |O| |O  5| |7| \n"
                         "-+-+-+-  -+-+-+-\n"
                         " |X|X|   9| | |c\n"
                         "-+-+-+-  -+-+-+-\n"
                         " |O|O|   d| | |g\n"))))


    (it "renders the winner with the grid"
      (->> (vector->grid [_ O _ O
                          _ _ _ _
                          _ O _ O
                          X X X X])
           render-grid
           (should= (str " |O| |O  1| |3| \n"
                         "-+-+-+-  -+-+-+-\n"
                         " | | |   5|6|7|8\n"
                         "-+-+-+-  -+-+-+-\n"
                         " |O| |O  9| |b| \n"
                         "-+-+-+-  -+-+-+-\n"
                         "X|X|X|X   | | | \n"
                         "\n"
                         "Winner: X\n"))))
    )
  )
