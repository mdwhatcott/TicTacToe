: EMPTY [char] _ ;
: X     [char] X ;
: O     [char] O ;

variable grid 8 cells allot
variable mark

: switch! ( -- )
	mark @ X = if
		O mark !
	else
		X mark !
	then
;

: grid-init! ( -- )
	X mark !
	9 0 do
		EMPTY grid i cells + !
	loop
;

: grid-place ( n -- )
	mark @ grid rot cells + !
	switch!
;

: grid-at ( n -- c )
	grid swap cells + @
;