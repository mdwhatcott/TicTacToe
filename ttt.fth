: EMPTY [char] _ ;
: X     [char] X ;

variable board 8 cells allot

: board-init ( -- )
	9 0 do
		EMPTY board i cells + !
	loop
;

: board-line ( -- )
	9 0 do
		board i cells + @ emit
	loop
	cr
;

: board-place ( n -- )
	X board rot cells + !
;
