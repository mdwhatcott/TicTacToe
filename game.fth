s" grid.fth" required
s" ui.fth" required
s" ai.fth" required

: play ( -- )
    clear-grid

    ALL_9_SLOTS 0 do
        print-grid
        mark @ X = if
            human-choice
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
