: _ [char] - ;
: X [char] x ;
: O [char] o ;
: C [char] C ;

variable grid 9 cells allot
variable mark

variable forks 9 cells allot
forks 9 cells erase

variable attacks 9 cells allot
attacks 9 cells erase

: .cells  ( addr n -- )
   0 ?do  dup ?  cell+  loop  drop
;

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

: print-hint-row ( row -- )
    { row }
    row 3 * { offset }

    winner _ = invert if exit then
    
    2 spaces
    3 0 do
        i offset + mark-at { mark }
        mark _ = if
            i offset + 48 + 1 + emit
        else
            space
        then
    i 3 mod 2 = invert if ." |" then
    loop
;

: print-row ( row -- )
    { row }
    row 3 * { offset }
    3 0 do
        i offset + mark-at { mark }
        mark _ = if
            space
        else
            mark emit
        then
        i 3 mod 2 = invert if ." |" then
    loop
    row print-hint-row
;

: print-grid ( -- )
    cr
    3 0 do
        i print-row cr
        i 3 mod 2 = invert if
            ." -----"
            winner _ = if ."   -----" then
        then
        cr
    loop
;

: place-win ( -- n )
    depth { original }
    push-blanks
    depth { with-blanks }
    with-blanks original - { blank-count }
    blank-count 0 do
        dup dup
        take-turn
        winner
        swap
        undo-turn
        mark @
        = if
            depth original 1 + > if
                swap drop
            then
        else
            drop
        then
    loop
    depth original = if _ then
;

: block-enemy-win ( n -- n )
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
    push-blanks 0 depth 1 - original ?do
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

: clear-attack-results ( -- )
    9 0 do
        0 attacks i cells + !
    loop
;

: store-fork-result ( n -- )
    { slot }
    slot peek-win-count { wins }
    wins forks slot cells + !
;

: place-fork ( n -- n )
    \ count-blanks 5 > if exit then \ TODO: remove
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
            wins 2 >= if i leave then
        loop

        depth original = if _ then
    then
;

: store-attacks ( -- )
    clear-attack-results

    depth { original }
    push-blanks
    depth { with-blanks }
    with-blanks original - { blank-count }
    blank-count 0 do
        dup take-turn
        _ block-enemy-win ( see if opponent has to block )
        swap dup undo-turn swap
        _ = if
            drop
        else
            { attack }
            1 attacks attack cells + !
        then
    loop
;

: attack-enemy-forks ( n -- n )
    count-blanks 6 > if exit then
    dup _ = if
        switch-mark  ( to opponent )
        _ place-fork ( for the opponent )
        _ = if
            exit
        then
        clearstack
        switch-mark  ( back to us )
        store-attacks
        9 0 do
            attacks i cells + @
            0 = invert if
                i dup
                take-turn         ( try our attack )
                _ block-enemy-win ( for the opponent )
                _ place-fork      ( for the opponent )
                rot dup undo-turn rot rot
                = invert if
                    clearstack i
                    ( if the block and the fork differ, use that attack! )
                    leave
                then
            then
        loop
    then
;

: take-center ( n -- n )
    dup _ = if
        drop
        4 mark-at _ = if 4 else _ then
    then
;

: can-take-opposite-corner ( n n -- b )
    { a b }
    a mark-at opponent = b mark-at _ = and
;

: take-opposite-corner ( n -- n )
    dup _ = if
        drop
        0 8 can-take-opposite-corner if 8 exit then
        8 0 can-take-opposite-corner if 0 exit then
        6 2 can-take-opposite-corner if 2 exit then
        2 6 can-take-opposite-corner if 6 exit then
        _
    then
;

: take-corner ( n -- n )
    dup _ = if
        drop
        0 mark-at _ = if 0 exit then
        2 mark-at _ = if 2 exit then
        6 mark-at _ = if 6 exit then
        8 mark-at _ = if 8 exit then
        _
    then
;

: take-side ( n -- n )
    dup _ = if
        1 mark-at _ = if drop 1 exit then
        3 mark-at _ = if drop 3 exit then
        5 mark-at _ = if drop 5 exit then
        7 mark-at _ = if drop 7 exit then
        _
    then
;

: ai-choice ( -- n )
        place-win
        block-enemy-win
        place-fork
        attack-enemy-forks
        take-center
        take-opposite-corner
        take-corner
        take-side
;

: human-turn ( -- n )
    ( TODO: input validation )
    ." Enter your choice: " key 48 - 1 - cr
;

: play ( -- )
    clear-grid

    9 0 do
        print-grid
        mark @ X = if
            human-turn
        else
            ai-choice
        then

        take-turn
        
        winner dup _ = invert if
            print-grid leave
        else
            drop
        then
    loop
;
