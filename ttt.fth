: N [char] _ ;
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

: grid-winner ( -- c )
    count-blanks 0=
    if
        C
    else
        N
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
