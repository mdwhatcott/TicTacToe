s" testing.fth" required
s" ttt.fth" required

: X-to-move X mark ! ;
: O-to-move O mark ! ;

: setup-grid ( 1 2 3 4 5 6 7 8 9 -- )
    clear-grid
    9 0 do
        mark !
        8 i - place-mark-at
    loop
;

: setup-drawn-grid ( -- )
    O X X
    X X O
    O O X
    setup-grid
;

: grid-line ( -- str-addr len )
    s" 123456789"        ( string buffer: <str-addr> 9 )
    swap
    9 0 do
        dup i +          ( seek to end of string buffer )
        1 i mark-at fill ( single char to buffer )
    loop
    swap
;

: assert-grid ( expected -- )
    grid-line should-equal-str
;

: assert-winner ( expected -- )
    winner should-equal-char
;


cr ." # Board Placement" cr cr

." - A newly created grid is empty" cr
    clear-grid
    s" ---------" assert-grid


." - The first turn places an X" cr
    clear-grid
    0 take-turn
    s" X--------" assert-grid


." - The second turn places an O" cr
    clear-grid
    0 take-turn
    1 take-turn
    s" XO-------" assert-grid


." - The third turn places another X" cr
    clear-grid
    0 take-turn
    1 take-turn
    2 take-turn
    s" XOX------" assert-grid

." - Marks cannot be overwritten" cr
    clear-grid
    0 take-turn
    0 take-turn \ no-op
    1 take-turn
    s" XO-------" assert-grid


." - A drawn game is immutable" cr
    setup-drawn-grid
    
    s" OXXXXOOOX" assert-grid
    
    1 take-turn ( should have no effect )
    
    s" OXXXXOOOX" assert-grid


." - A game won by X is immutable" cr
    X X X
    _ O O
    _ O O
    setup-grid

    s" XXX-OO-OO" assert-grid

    3 take-turn ( should have no effect )

    s" XXX-OO-OO" assert-grid


." - A game won by O is immutable" cr
    O O O
    _ X X
    _ X X
    setup-grid

    s" OOO-XX-XX" assert-grid

    3 take-turn ( should have no effect )

    s" OOO-XX-XX" assert-grid


cr ." # Winning Conditions" cr cr

." - An empty grid has no winner" cr
    clear-grid
    _ assert-winner


." - A drawn grid has no winner" cr
    setup-drawn-grid
    C assert-winner


." - X wins on row 1" cr
    X X X
    O _ O
    O _ O
    setup-grid
    X assert-winner


." - X wins on row 2" cr
    O _ O
    X X X
    O _ O
    setup-grid
    X assert-winner


." - X wins on row 3" cr
    O _ O
    O _ O
    X X X
    setup-grid
    X assert-winner


." - X wins on col 1" cr
    X O O
    X _ _
    X O O
    setup-grid
    X assert-winner


." - X wins on col 2" cr
    O X O
    _ X _
    O X O
    setup-grid
    X assert-winner


." - X wins on col 3" cr
    O O X
    _ _ X
    O O X
    setup-grid
    X assert-winner


." - X wins on dia 1" cr
    X O _
    O X O
    _ O X
    setup-grid
    X assert-winner


." - X wins on dia 2" cr
    _ O X
    O X O
    X O _
    setup-grid
    X assert-winner


." - O wins on row 1" cr
    O O O
    X _ X
    X _ X
    setup-grid
    O assert-winner


." - O wins on row 2" cr
    X _ X
    O O O
    X _ X
    setup-grid
    O assert-winner


." - O wins on row 3" cr
    X _ X
    X _ X
    O O O
    setup-grid
    O assert-winner


." - O wins on col 1" cr
    O X X
    O _ _
    O X X
    setup-grid
    O assert-winner


." - O wins on col 2" cr
    X O X
    _ O _
    X O X
    setup-grid
    O assert-winner


." - O wins on col 3" cr
    X X O
    _ _ O
    X X O
    setup-grid
    O assert-winner


." - O wins on dia 1" cr
    O X _
    X O X
    _ X O
    setup-grid
    O assert-winner


." - O wins on dia 2" cr
    _ X O
    X O X
    O X _
    setup-grid
    O assert-winner


cr ." # Unbeatable AI" cr cr

." - Inventory of openings" cr
    X _ O \   1
    X _ O \   4
    _ _ _ \ 6 7 8
    setup-grid
    push-blanks
    5 should-equal-char \ count
    8 should-equal-char
    7 should-equal-char
    6 should-equal-char
    4 should-equal-char
    1 should-equal-char

." - Undo a placed mark" cr
    clear-grid
    0 take-turn
    0 undo-turn
    s" ---------" assert-grid
    0 take-turn
    s" X--------" assert-grid


\ ." - As X, makes the winning move" cr
\     X X _ ( <-- )
\     O O _
\     _ _ _
\     setup-grid X-to-move
\     ai-choice 2 over should-equal-char


\ ." - As O, makes the winning move" cr
\     X X _
\     O O _ ( <-- )
\     _ _ _
\     setup-grid O-to-move
\     ai-choice 5 over should-equal-char


." - As X, blocks O from winning (TODO)" cr
    O _ O ( <-- )
    _ X _
    _ _ X
    setup-grid X-to-move
    ai-choice drop \ 1 should-equal-char



bye
