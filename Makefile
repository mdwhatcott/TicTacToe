#!/usr/bin/make -f

test:
	@echo '\n---------------------\n' && \
		time gforth test-runner.fth && echo && \
		date '+%H:%M:%S' && echo

# Requires 'entr' utility (http://eradman.com/entrproject/)
auto-test:
	while sleep 1; do find . -name '*.fth' | entr -d make; done

play:
	gforth main.fth