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

." - grid-init!: creates a new grid" cr
    grid-init!
    s" _________"
    assert-grid

." - grid-turn!: places X first" cr
    grid-init!
    0 grid-turn!
    s" X________"
    assert-grid

." - grid-turn!: places O after X" cr
    grid-init!
    0 grid-turn!
    1 grid-turn!
    s" XO_______"
    assert-grid

." - grid-turn!: places X after O" cr
    grid-init!
    0 grid-turn!
    1 grid-turn!
    2 grid-turn!
    s" XOX______" 
    assert-grid

." - grid-win?: empty board" cr
    grid-init!
    grid-winner
    EMPTY should-equal-char

bye