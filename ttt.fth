: EMPTY [char] _ ;
: X     [char] X ;

variable grid 8 cells allot

: grid-init ( -- )
	9 0 do
		EMPTY grid i cells + !
	loop
;

: grid-line ( -- )
	9 0 do
		grid i cells + @ emit
	loop
	cr
;

: grid-place ( n -- )
	X grid rot cells + !
;
