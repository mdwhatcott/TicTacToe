s" grid.fth" required

variable forks ALL_SLOTS cells allot
forks ALL_SLOTS cells erase

variable attacks ALL_SLOTS cells allot
attacks ALL_SLOTS cells erase

: .cells  ( addr n -- )
   0 ?do  dup ?  cell+  loop  drop
;

: print-hint-row ( row -- )
    { row }
    row WIDTH * { offset }

    winner _ = invert if exit then
    
    2 spaces
    WIDTH 0 do
        i offset + mark-at { mark }
        mark _ = if
            i offset + 48 + 1 + emit
        else
            space
        then
    i WIDTH mod 2 = invert if ." |" then
    loop
;

: print-row ( row -- )
    { row }
    row WIDTH * { offset }
    WIDTH 0 do
        i offset + mark-at { mark }
        mark _ = if
            space
        else
            mark emit
        then
        i WIDTH mod 2 = invert if ." |" then
    loop
    row print-hint-row
;

: print-grid ( -- )
    cr
    WIDTH 0 do
        i print-row cr
        i WIDTH mod 2 = invert if
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
    ALL_SLOTS 0 do
        0 forks i cells + !
    loop
;

: clear-attack-results ( -- )
    ALL_SLOTS 0 do
        0 attacks i cells + !
    loop
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

        ALL_SLOTS 0 do
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
        ALL_SLOTS 0 do
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

48 constant ASCII_DIGIT_OFFSET
: human-turn ( -- n )
    ." Enter your choice: "
    key { choice }
    choice emit cr
    choice ASCII_DIGIT_OFFSET -
    1 - { slot } ( make 0-based offset )
    
    push-blanks
    depth 0 do
        slot = if
            clearstack slot leave
        then
    loop

    depth 1 = if exit then

    ." Invalid choice! Try again..." cr
    clearstack
    recurse
;

: play ( -- )
    clear-grid

    ALL_SLOTS 0 do
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
