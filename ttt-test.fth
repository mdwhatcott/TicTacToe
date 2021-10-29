: setup-grid ( 1 2 3 4 5 6 7 8 9 -- )
    grid-init
    9 0 do
        mark !
        i grid-place
    loop
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
    s" _________"
    assert-grid

." - The first turn places an X" cr
    grid-init
    0 grid-turn
    s" X________"
    assert-grid

." - The second turn places an O" cr
    grid-init
    0 grid-turn
    1 grid-turn
    s" XO_______"
    assert-grid

." - The third turn places another X" cr
    grid-init
    0 grid-turn
    1 grid-turn
    2 grid-turn
    s" XOX______"
    assert-grid

." - An empty grid has no winner" cr
    grid-init
    N assert-winner

." - A drawn grid has no winner" cr
    O X X
    X X O
    O O X
    setup-grid
    N assert-winner

." - X wins on row 1 (TODO)" cr
." - X wins on row 2 (TODO)" cr
." - X wins on row 3 (TODO)" cr
." - X wins on col 1 (TODO)" cr
." - X wins on col 2 (TODO)" cr
." - X wins on col 3 (TODO)" cr
." - X wins on dia 1 (TODO)" cr
." - X wins on dia 2 (TODO)" cr

." - O wins on row 1 (TODO)" cr
." - O wins on row 2 (TODO)" cr
." - O wins on row 3 (TODO)" cr
." - O wins on col 1 (TODO)" cr
." - O wins on col 2 (TODO)" cr
." - O wins on col 3 (TODO)" cr
." - O wins on dia 1 (TODO)" cr
." - O wins on dia 2 (TODO)" cr


bye