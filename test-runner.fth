s" grid.fth" required

: setup-grid ( 1 2 3 4 5 6 7 8 9 -- )
    clear-grid
    9 0 do
        mark !
        8 i - place-mark-at
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
        1 i mark-at fill ( single char to buffer )
    loop
    swap
;

: assert-no-stack
    depth 0 > if
        ." >>> LEFT OVER STACK: " .s cr
        clearstack
    then
;

s" should-test.fth" required
s" ui-test.fth" required
s" grid-test.fth" required
s" ai-test.fth" required

bye
