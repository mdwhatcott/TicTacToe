: _ [char] - ;
: X [char] X ;
: O [char] O ;
: C [char] C ;

variable grid 9 cells allot
variable mark

variable forks 9 cells allot


: switch-mark ( -- )
    mark @ X =
    if
        O mark !
    else
        X mark !
    then ;

: place-mark-at ( n -- )
    mark @ grid rot cells + ! ;

: clear-grid ( -- )
    _ mark !
    9 0 do i place-mark-at loop
    X mark ! ;

: mark-at ( n -- c )
    grid swap cells + @ ;

: push-blanks ( -- ... len )
    depth { original-depth }
    9 0 do i mark-at _ = if i then loop
;

: count-blanks ( -- n )
    0 ( counter on bottom of stack )
    9 0 do
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

: place-win ( -- n )
    depth 1 + { threshold }
    push-blanks
    depth 0 do
        dup dup
        take-turn
        winner
        swap
        undo-turn
        mark @
        = if
            depth threshold > if
                swap drop
            then
        else
            drop
        then
    loop
    depth 0 = if _ then
;

: prevent-loss ( n -- n )
    dup _ = if
        drop
        switch-mark
        place-win
        switch-mark
    then
;

: peek-move-result ( n -- n )
    dup
    take-turn
    winner
    swap
    undo-turn
;

: peek-win-count ( n -- n )
    { choice }
    depth { original }
    choice take-turn
    switch-mark ( back to current player )
    mark @ { player }
    push-blanks 0 depth 1 - original do
        swap
        peek-move-result player = if
            1 +
        then
    loop
    choice undo-turn
;

: clear-fork-results ( -- )
    9 0 do
        0 forks i cells + !
    loop
;

: .cells  ( addr n -- )
   0 ?do  dup ?  cell+  loop  drop
;

: store-fork-result ( n -- )
    { slot }
    slot peek-win-count { wins }
    wins forks slot cells + !
;

: place-fork ( n -- n )
    dup _ = if
        drop
        clear-fork-results
        depth { original }
        push-blanks
        depth original do
            store-fork-result
        loop


        9 0 do
            forks i cells + @ { wins }
            wins 2 >= if i then
        loop
    then
;

: take-center ( n -- n )
    dup _ = if
        drop 4
    then
;

: ai-choice ( -- n )
    count-blanks 9 = if
        _ take-center
    else
        place-win
        prevent-loss
        place-fork
        \ TODO: block a fork
        \ TODO: force a block to prevent a fork
        \ take-center
        \ TODO: play a corner if available
        \ TODO: play a side
    then
;
