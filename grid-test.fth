s" grid.fth" required

: assert-grid ( expected -- )
    grid-line should-equal-str
    assert-no-stack
;

: assert-winner ( expected -- )
    winner should-equal-n
    assert-no-stack
;

cr ." # Board Placement" cr cr

." - A newly created grid is empty" cr
    clear-grid
    s" ---------" assert-grid

." - The first turn places an X" cr
    clear-grid
    0 take-turn
    s" x--------" assert-grid

." - The second turn places an O" cr
    clear-grid
    0 take-turn
    1 take-turn
    s" xo-------" assert-grid

." - The third turn places another X" cr
    clear-grid
    0 take-turn
    1 take-turn
    2 take-turn
    s" xox------" assert-grid

." - Marks cannot be overwritten" cr
    clear-grid
    0 take-turn
    0 take-turn \ no-op
    1 take-turn
    s" xo-------" assert-grid

." - A drawn game is immutable" cr
    setup-drawn-grid
    
    s" oxxxxooox" assert-grid
    
    1 take-turn ( should have no effect )
    
    s" oxxxxooox" assert-grid

." - A game won by X is immutable" cr
    X X X
    _ O O
    _ O O
    setup-grid
    3 take-turn ( should have no effect )
    s" xxx-oo-oo" assert-grid

." - A game won by O is immutable" cr
    O O O
    _ X X
    _ X X
    setup-grid
    3 take-turn ( should have no effect )
    s" ooo-xx-xx" assert-grid


cr ." # Winning Conditions" cr cr

." - An empty grid has no winner" cr
    clear-grid
    _ assert-winner

." - A drawn grid has no winner" cr
    setup-drawn-grid
    C assert-winner

." - X wins on row 1" cr
    X X X
    O _ O
    O _ O
    setup-grid
    X assert-winner

." - X wins on row 2" cr
    O _ O
    X X X
    O _ O
    setup-grid
    X assert-winner

." - X wins on row 3" cr
    O _ O
    O _ O
    X X X
    setup-grid
    X assert-winner

." - X wins on col 1" cr
    X O O
    X _ _
    X O O
    setup-grid
    X assert-winner

." - X wins on col 2" cr
    O X O
    _ X _
    O X O
    setup-grid
    X assert-winner

." - X wins on col 3" cr
    O O X
    _ _ X
    O O X
    setup-grid
    X assert-winner

." - X wins on dia 1" cr
    X O _
    O X O
    _ O X
    setup-grid
    X assert-winner

." - X wins on dia 2" cr
    _ O X
    O X O
    X O _
    setup-grid
    X assert-winner

." - O wins on row 1" cr
    O O O
    X _ X
    X _ X
    setup-grid
    O assert-winner

." - O wins on row 2" cr
    X _ X
    O O O
    X _ X
    setup-grid
    O assert-winner

." - O wins on row 3" cr
    X _ X
    X _ X
    O O O
    setup-grid
    O assert-winner

." - O wins on col 1" cr
    O X X
    O _ _
    O X X
    setup-grid
    O assert-winner

." - O wins on col 2" cr
    X O X
    _ O _
    X O X
    setup-grid
    O assert-winner

." - O wins on col 3" cr
    X X O
    _ _ O
    X X O
    setup-grid
    O assert-winner

." - O wins on dia 1" cr
    O X _
    X O X
    _ X O
    setup-grid
    O assert-winner

." - O wins on dia 2" cr
    _ X O
    X O X
    O X _
    setup-grid
    O assert-winner
