s" ai.fth" required

: X-to-move X mark ! ;
: O-to-move O mark ! ;

: assert-ai-choice ( expected -- )
    { expected }
    ai-choice { actual }
    expected actual should-equal-n
    assert-no-stack
;

cr ." # Unbeatable AI" cr cr

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
