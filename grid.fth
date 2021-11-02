: _ [char] - ;
: X [char] x ;
: O [char] o ;
: C [char] C ;

9 constant ALL_9_SLOTS
3 constant WIDTH

variable mark

variable grid ALL_9_SLOTS cells allot
grid ALL_9_SLOTS cells erase

: opponent ( -- n )
    mark @ X = if O else X then
;

: switch-mark ( -- )
    opponent mark !
;

: place-mark-at ( n -- )
    mark @ grid rot cells + ! ;

: clear-grid ( -- )
    _ mark !
    ALL_9_SLOTS 0 do i place-mark-at loop
    X mark ! ;

: mark-at ( n -- c )
    grid swap cells + @ ;

: push-blanks ( -- ... len )
    depth { original-depth }
    ALL_9_SLOTS 0 do i mark-at _ = if i then loop
;

: count-blanks ( -- n )
    0 ( counter on bottom of stack )
    ALL_9_SLOTS 0 do
        i mark-at _ =
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
    n1 mark-at
    n2 mark-at
    n3 mark-at ;

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

: winner ( -- c )
    X is-winner?    if X else
    O is-winner?    if O else
    count-blanks 0= if C else _
    then then then ;

: take-turn ( n -- )
    { on -- }
    winner _ = on mark-at _ = and
    if
        on place-mark-at
        switch-mark
    then ;

: undo-turn ( n -- )
    dup mark-at { on original }
    _ mark !
    on place-mark-at
    original mark !
;
