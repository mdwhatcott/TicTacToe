s" ui.fth" required

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
