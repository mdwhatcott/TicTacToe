: should-equal-n { expected actual -- }
    expected actual = invert
    if
        ." <<< FAIL >>>" cr
        ." -- expect: " expected . expected emit cr
        ." -- actual: " actual   . actual   emit cr
        cr
    then
;

: should-equal-str { expected expected-length
                       actual   actual-length -- }

    expected expected-length actual actual-length compare
    if
        actual actual-length expected expected-length
        ." <<< FAIL >>>" cr
        ." -- expect: [" type ." ]" cr
        ." -- actual: [" type ." ]" cr
        cr
    then
;
