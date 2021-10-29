: grid-line ( -- )
	9 0 do
		grid i cells + @ emit
	loop
	cr
;

cr ." grid-init: creates a new grid"
cr ." - expect: _________"
cr ." - actual: " grid-init
                  grid-line
cr
cr ." grid-place: places X first"
cr ." - expect: ____X____"
cr ." - actual: " grid-init
                  4 grid-place
                  grid-line
cr
cr ." grid-place: places O after X"
cr ." - expect: XO_______"
cr ." - actual: " grid-init
                  0 grid-place
                  1 grid-place
                  grid-line
cr
cr ." grid-place: places X after O"
cr ." - expect: XOX______"
cr ." - actual: " grid-init
                  0 grid-place
                  1 grid-place
                  2 grid-place
                  grid-line
cr



bye