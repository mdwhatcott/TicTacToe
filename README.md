# github.com/mdwhatcott/tictactoe

The classic game of [Tic Tac Toe], implemented in a few different languages:

- [go](https://github.com/mdwhatcott/tictactoe/tree/go)
- [clojure](https://github.com/mdwhatcott/tictactoe/tree/clojure)
- [forth](https://github.com/mdwhatcott/tictactoe/tree/forth)
- [racket](https://github.com/mdwhatcott/tictactoe/tree/racket)

## Forth

### Prerequisites

- Install [gforth](https://gforth.org/) 
	- `brew install gforth`
- Install [entr](http://eradman.com/entrproject/) (optional, only needed for `make auto-test` below)
	- `brew install entr`


### Execution

- Tests: `make test`
- Test runner: `make auto-test`
- Play the game: `make play`
