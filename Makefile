#!/usr/bin/make -f

test:
	@echo '---------------------' \
		&& echo && \
		gforth ttt-test.fth \
		&& echo && date '+%H:%M:%S' && echo

# Requires 'entr' utility (http://eradman.com/entrproject/)
auto-test:
	while sleep 1 ; do find . -name '*.fth' | entr -d make ; done