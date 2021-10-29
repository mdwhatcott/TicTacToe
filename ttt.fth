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

: is-winner? { c -- b }
    0 1 2 get-3 c 3-in-a-row?
    3 4 5 get-3 c 3-in-a-row? or
    6 7 8 get-3 c 3-in-a-row? or
    
    0 3 6 get-3 c 3-in-a-row? or
    1 4 7 get-3 c 3-in-a-row? or
    2 5 8 get-3 c 3-in-a-row? or

    0 4 8 get-3 c 3-in-a-row? or
    2 4 6 get-3 c 3-in-a-row? or
;

: grid-winner ( -- c )
    X is-winner?    if X else
    O is-winner?    if O else
    count-blanks 0= if C else N
    then then then
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
