: N [char] - ;
: X [char] X ;
: O [char] O ;
: C [char] C ;

variable grid 8 cells allot
variable mark

: switch! ( -- )
    mark @ X =
    if
        O mark !
    else
        X mark !
    then
;

: grid-place ( n -- )
    mark @ grid rot cells + !
;

: grid-init ( -- )
    N mark !
    9 0 do i grid-place loop
    X mark !
;

: grid-at ( n -- c )
    grid swap cells + @
;

: count-blanks ( -- n )
    0 ( counter on bottom of stack )
    9 0 do
        i grid-at N =
        if
            1 + ( increment counter )
        then
    loop
;

: 3-in-a-row? { g1 g2 g3 c -- b }
    g1 c = invert if
        false
    else
        g1 g2 =
        g1 g3 =
        and
    then
;

: get-3 { n1 n2 n3 }
    n1 grid-at
    n2 grid-at
    n3 grid-at
;

: grid-winner ( -- c )
    0 1 2 get-3 X 3-in-a-row? if
        X
    else
        count-blanks 0=
        if
            C
        else
            N
        then
    then
;

: grid-turn ( n -- )
    grid-winner C =
    if
        drop
    else
        grid-place
        switch!
    then
;
