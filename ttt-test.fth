: setup-grid ( 1 2 3 4 5 6 7 8 9 -- )
    grid-init
    9 0 do
        mark !
        8 i - grid-place
    loop
;

: setup-drawn-grid ( -- )
    O X X
    X X O
    O O X
    setup-grid
;

: grid-line ( -- str-addr len )
    s" 123456789"        ( string buffer: <str-addr> 9 )
    swap
    9 0 do
        dup i +          ( seek to end of string buffer )
        1 i grid-at fill ( single char to buffer )
    loop
    swap
;

: should-equal-char ( expected actual -- )
    2dup = invert if
        swap
        ." << fail >>" cr
        ." -expect: " emit cr
        ." -actual: " emit cr
    else
        2drop
    then
;

: should-equal-str ( str-addr len str-addr len -- )
          ( um, there's got to )
          ( be a better way... )
          ( 1 2  3 4           )
    2swap ( 3 4  1 2           )
    2dup  ( 3 4  1 2  1 2      )
    2rot  ( 1 2  1 2  3 4      )
    2dup  ( 1 2  1 2  3 4  3 4 )
    2rot  ( 1 2  3 4  3 4  1 2 )
    2swap ( 1 2  3 4  1 2  3 4 )
    compare if
        2swap
        ." << fail >>" cr
        ." - expect: [" type ." ]" cr
        ." - actual: [" type ." ]" cr
    else
        2drop 2drop
    then
;

: assert-grid ( expected -- )
    grid-line should-equal-str
;

: assert-winner ( expected -- )
    grid-winner should-equal-char
;

." - A newly created grid is empty" cr
    grid-init
    s" ---------" assert-grid

." - The first turn places an X" cr
    grid-init
    0 grid-turn
    s" X--------" assert-grid

." - The second turn places an O" cr
    grid-init
    0 grid-turn
    1 grid-turn
    s" XO-------" assert-grid

." - The third turn places another X" cr
    grid-init
    0 grid-turn
    1 grid-turn
    2 grid-turn
    s" XOX------" assert-grid

." - A drawn game is immutable" cr
    setup-drawn-grid
    
    s" OXXXXOOOX" assert-grid
    
    1 grid-turn ( should have no effect )
    
    s" OXXXXOOOX" assert-grid

." - An empty grid has no winner" cr
    grid-init
    N assert-winner


." - A drawn grid has no winner" cr
    setup-drawn-grid
    C assert-winner

." - X wins on row 1" cr
    X X X
    O N O
    O N O
    setup-grid
    X assert-winner

." - X wins on row 2" cr
    O N O
    X X X
    O N O
    setup-grid
    X assert-winner

." - X wins on row 3" cr
    O N O
    O N O
    X X X
    setup-grid
    X assert-winner

." - X wins on col 1" cr
    X O O
    X N N
    X O O
    setup-grid
    X assert-winner

." - X wins on col 2" cr
    O X O
    N X N
    O X O
    setup-grid
    X assert-winner

." - X wins on col 3" cr
    O O X
    N N X
    O O X
    setup-grid
    X assert-winner

." - X wins on dia 1" cr
    X O N
    O X O
    N O X
    setup-grid
    X assert-winner

." - X wins on dia 2" cr
    N O X
    O X O
    X O N
    setup-grid
    X assert-winner

." - O wins on row 1" cr
    O O O
    X N X
    X N X
    setup-grid
    O assert-winner

." - O wins on row 2" cr
    X N X
    O O O
    X N X
    setup-grid
    O assert-winner

." - O wins on row 3" cr
    X N X
    X N X
    O O O
    setup-grid
    O assert-winner

." - O wins on col 1" cr
    O X X
    O N N
    O X X
    setup-grid
    O assert-winner

." - O wins on col 2" cr
    X O X
    N O N
    X O X
    setup-grid
    O assert-winner

." - O wins on col 3" cr
    X X O
    N N O
    X X O
    setup-grid
    O assert-winner

." - O wins on dia 1" cr
    O X N
    X O X
    N X O
    setup-grid
    O assert-winner

." - O wins on dia 2" cr
    N X O
    X O X
    O X N
    setup-grid
    O assert-winner

bye