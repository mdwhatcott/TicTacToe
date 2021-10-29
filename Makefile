#!/usr/bin/make -f

test:
	@echo '---------------------' \
		&& echo && \
		time gforth ttt.fth ttt-test.fth \
		&& echo && date && echo

# Requires 'entr' utility (http://eradman.com/entrproject/)
auto-test:
	while sleep 1 ; do find . -name '*.fth' | entr -d make ; done