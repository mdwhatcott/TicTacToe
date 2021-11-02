s" grid.fth" required

48 constant ASCII_DIGIT_OFFSET
: human-choice ( -- n )
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
