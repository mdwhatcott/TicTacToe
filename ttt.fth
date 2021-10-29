: _ [char] - ;
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
    then ;

: grid-place ( n -- )
    mark @ grid rot cells + ! ;

: grid-init ( -- )
    _ mark !
    9 0 do i grid-place loop
    X mark ! ;

: grid-at ( n -- c )
    grid swap cells + @ ;

: count-blanks ( -- n )
    0 ( counter on bottom of stack )
    9 0 do
        i grid-at _ =
        if
            1 + ( increment counter )
        then
    loop ;

: all? ( c c c c -- b )
    { w x y z }
    w x =
    x y = and
    y z = and ;

: get-3 ( n n n -- c c c )
    { n1 n2 n3 }
    n1 grid-at
    n2 grid-at
    n3 grid-at ;

: is-row-winner? ( c -- b )
    { mark }
    0 1 2 get-3 mark all?
    3 4 5 get-3 mark all? or
    6 7 8 get-3 mark all? or ;

: is-col-winner? ( c -- b )
    { mark }
    0 3 6 get-3 mark all?
    1 4 7 get-3 mark all? or
    2 5 8 get-3 mark all? or ;

: is-dia-winner? ( c -- b )
    { mark }
    0 4 8 get-3 mark all?
    2 4 6 get-3 mark all? or ;

: is-winner? ( c -- b )
    { mark }
    mark is-row-winner?
    mark is-col-winner? or
    mark is-dia-winner? or ;

: grid-winner ( -- c )
    X is-winner?    if X else
    O is-winner?    if O else
    count-blanks 0= if C else _
    then then then ;

: grid-turn ( n -- )
    grid-winner C =
    if
        drop
    else
        grid-place
        switch!
    then ;
