: grid-line ( -- str-addr len )
    s" 123456789"        ( string buffer: <str-addr> 9 )
    swap
	9 0 do
        dup i +          ( seek to end of string buffer )
	    1 i grid-at fill ( single char to buffer )
	loop
	swap
;

: should-equal-str ( str-addr len str-addr len -- n )
        ( 1 2  3 4           )
  2swap ( 3 4  1 2           )
  2dup  ( 3 4  1 2  1 2      )
  2rot  ( 1 2  1 2  3 4      )
  2dup  ( 1 2  1 2  3 4  3 4 )
  2rot  ( 1 2  3 4  3 4  1 2 )
  2swap ( 1 2  3 4  1 2  3 4 )
  compare if
  	2swap
  	." << fail >>" cr
	." - expect: [" type ." ]" cr
  	." - actual: [" type ." ]" cr
  else
  	2drop 2drop
  then
;

: assert-grid ( expected -- )
	grid-line should-equal-str
;

s" abc" s" abc" should-equal-str

." - grid-init!: creates a new grid" cr
grid-init!
s" _________" assert-grid

." - grid-place: places X first" cr
grid-init!
0 grid-place
s" X________" assert-grid

." - grid-place: places O after X" cr
grid-init!
0 grid-place
1 grid-place
s" XO_______" assert-grid

." - grid-place: places X after O" cr
grid-init!
0 grid-place
1 grid-place
2 grid-place
s" XOX______" assert-grid

bye