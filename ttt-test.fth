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

: should-equal-char { expected actual -- }
    expected actual = invert
    if
        ." << fail >>" cr
        ." -expect: " expected emit cr
        ." -actual: " actual emit cr
    then
;

: should-equal-str { expected expected-length
                       actual   actual-length -- }

    expected expected-length actual actual-length compare
    if
        actual actual-length expected expected-length
        ." << fail >>" cr
        ." - expect: [" type ." ]" cr
        ." - actual: [" type ." ]" cr
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


." - A game won by X is immutable" cr
    X X X
    _ O O
    _ O O
    setup-grid

    s" XXX-OO-OO" assert-grid

    3 grid-turn ( should have no effect )

    s" XXX-OO-OO" assert-grid


." - A game won by O is immutable" cr
    O O O
    _ X X
    _ X X
    setup-grid

    s" OOO-XX-XX" assert-grid

    3 grid-turn ( should have no effect )

    s" OOO-XX-XX" assert-grid


." - An empty grid has no winner" cr
    grid-init
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

bye