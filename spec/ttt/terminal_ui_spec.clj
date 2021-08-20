(ns ttt.terminal-ui-spec
  (:require [speclj.core :refer :all]
            [ttt.terminal-ui :refer :all]
            [ttt.grid :refer :all]
            [ttt.grid-spec :refer :all]))

(describe "Console Rendering"
  (it "renders the grid"
    (->> (vector->grid [O _ X
                        _ O _
                        _ X _])
         render-grid
         (should= (str "O| |X\n"
                       "-+-+-\n"
                       " |O| \n"
                       "-+-+-\n"
                       " |X| \n"))))

  (it "decorates the grid"
    (->> (make-grid)
         render-grid
         decorate-grid
         (should= (str " | |  1|2|3\n"
                       "-+-+- -+-+-\n"
                       " | |  4|5|6\n"
                       "-+-+- -+-+-\n"
                       " | |  7|8|9\n"))))
  )
