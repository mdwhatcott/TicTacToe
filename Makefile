#!/usr/bin/make

tcr:
	tcr -command 'make test' -working .

test:
	raco test ttt.rkt

.PHONY: tcr, test
