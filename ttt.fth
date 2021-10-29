: N [char] _ ;
: X [char] X ;
: O [char] O ;

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

: grid-turn ( n -- )
    grid-place
    switch!
;

: grid-at ( n -- c )
    grid swap cells + @
;

: grid-winner ( -- c )
    N
;
