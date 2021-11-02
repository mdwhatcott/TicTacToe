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

: assert-no-stack
    depth 0 > if
        ." >>> LEFT OVER STACK: " .s cr
        clearstack
    then
;

: assert-grid ( expected -- )
    grid-line should-equal-str
    assert-no-stack
;

: assert-winner ( expected -- )
    winner should-equal-n
    assert-no-stack
;

: assert-ai-choice
    { expected }
    ai-choice { actual }
    expected actual should-equal-n
    assert-no-stack
;


cr ." # Terminal UI" cr cr

." - Print empty grid" cr
    clear-grid
    print-grid cr cr

." - Print more filled-in grid" cr cr
    X O X
    _ X _
    O X O
    setup-grid
    print-grid cr

." - Print winning grid (no hints)" cr cr
    X X X
    _ _ _
    O _ O
    setup-grid
    print-grid cr


cr ." # Board Placement" cr cr

." - A newly created grid is empty" cr
    clear-grid
    s" ---------" assert-grid

." - The first turn places an X" cr
    clear-grid
    0 take-turn
    s" x--------" assert-grid

." - The second turn places an O" cr
    clear-grid
    0 take-turn
    1 take-turn
    s" xo-------" assert-grid

." - The third turn places another X" cr
    clear-grid
    0 take-turn
    1 take-turn
    2 take-turn
    s" xox------" assert-grid

." - Marks cannot be overwritten" cr
    clear-grid
    0 take-turn
    0 take-turn \ no-op
    1 take-turn
    s" xo-------" assert-grid

." - A drawn game is immutable" cr
    setup-drawn-grid
    
    s" oxxxxooox" assert-grid
    
    1 take-turn ( should have no effect )
    
    s" oxxxxooox" assert-grid

." - A game won by X is immutable" cr
    X X X
    _ O O
    _ O O
    setup-grid

    s" xxx-oo-oo" assert-grid

    3 take-turn ( should have no effect )

    s" xxx-oo-oo" assert-grid

." - A game won by O is immutable" cr
    O O O
    _ X X
    _ X X
    setup-grid

    s" ooo-xx-xx" assert-grid

    3 take-turn ( should have no effect )

    s" ooo-xx-xx" assert-grid


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
    8 should-equal-n
    7 should-equal-n
    6 should-equal-n
    4 should-equal-n
    1 should-equal-n

." - Undo a placed mark" cr
    clear-grid
    0 take-turn
    0 undo-turn
    s" ---------" assert-grid
    0 take-turn
    s" x--------" assert-grid

." - As X, take the center on the first move" cr
    clear-grid
    4 assert-ai-choice

." - As X, makes the winning move (slot: 0)" cr
    _ X X ( <-- )
    _ O O
    _ _ _
    setup-grid X-to-move
    0 assert-ai-choice

." - As O, makes the winning move (slot: middle)" cr
    X X _
    O O _ ( <-- )
    _ _ _
    setup-grid O-to-move
    5 assert-ai-choice

." - As O, makes the winning move (slot: last)" cr
    _ _ _
    X X _
    O O _ ( <-- )
    setup-grid O-to-move
    8 assert-ai-choice

." - As X, blocks O from winning" cr
    O _ O ( <-- )
    _ X _
    _ _ X
    setup-grid X-to-move
    1 assert-ai-choice

." - As O, blocks X from winning" cr
    X _ X ( <-- )
    _ O _
    _ _ O
    setup-grid O-to-move
    1 assert-ai-choice

." - Peek move result (X would win)" cr
    _ X X
    O O X
    X O O
    setup-grid X-to-move
    0 peek-move-result
    X swap should-equal-n
    s" -xxooxxoo" assert-grid

." - Peek count wins (2 for X)" cr
    O X O
    _ _ _
    _ _ X
    setup-grid x-to-move
    1 2 3 ( preexisting stack values )
    7 peek-win-count
    2 swap should-equal-n
    depth 3 should-equal-n
    drop drop drop

." - As X, places fork" cr
    O X O
    _ _ _
    _ _ X ( <-- )
    setup-grid X-to-move
    7 assert-ai-choice

." - As O, attack Xs multiple impending forks" cr
    ( X has possible forks on left-middle and bottom-middle )
    X _ _
    _ X _
    _ _ O
    setup-grid O-to-move
    2 assert-ai-choice

." - As O, attack to avoid Xs impending forks (part 1)" cr
    ( X has possible forks on upper-left and lower right )
    _ _ X
    _ O _
    X _ _
    setup-grid O-to-move
    1 assert-ai-choice

." - As O, attack to avoid Xs impending fork (part 2)" cr
    _ _ _
    _ O X
    _ X _
    setup-grid O-to-move
    2 assert-ai-choice

." - As O, attack in corner to avoid Xs impending fork (part 3)" cr
    O _ _
    _ X _
    _ _ X
    setup-grid O-to-move
    2 assert-ai-choice

." - As X, take center" cr
    O X O
    X _ _
    X O X
    setup-grid X-to-move
    4 assert-ai-choice

." - As X, take lower-right corner opposite O" cr
    O _ _
    _ X _
    _ _ _
    setup-grid X-to-move
    8 assert-ai-choice

." - As X, take upper-right corner opposite O" cr
    _ _ _
    _ X _
    _ _ O
    setup-grid X-to-move
    0 assert-ai-choice

." - As X, take upper-left corner opposite O" cr
    _ _ _
    _ X _
    O _ _
    setup-grid X-to-move
    2 assert-ai-choice

." - As X, take lower-left corner opposite O" cr
    _ _ O
    _ X _
    _ _ _
    setup-grid X-to-move
    6 assert-ai-choice

." - As X, take empty upper-left corner" cr
    _ O _
    _ X _
    _ _ _
    setup-grid X-to-move
    0 assert-ai-choice

." - As X, take empty upper-right corner" cr
    O _ _
    X X O
    _ O X
    setup-grid X-to-move
    2 assert-ai-choice

." - As X, take empty lower-left corner" cr
    O X O
    X X O
    _ O X
    setup-grid X-to-move
    6 assert-ai-choice

." - As X, take empty lower-right corner" cr
    O O X
    X X O
    O X _
    setup-grid X-to-move
    8 assert-ai-choice

." - As X, take empty top side" cr
    O _ X
    X X O
    O O X
    setup-grid X-to-move
    1 assert-ai-choice

." - As X, take empty right side" cr
    O X O
    O X _
    X O X
    setup-grid X-to-move
    5 assert-ai-choice

." - As X, take empty bottom side" cr
    X O O
    O X X
    X _ O
    setup-grid X-to-move
    7 assert-ai-choice

." - As X, take empty left side" cr
    X O X
    _ X O
    O X O
    setup-grid X-to-move
    3 assert-ai-choice


bye
