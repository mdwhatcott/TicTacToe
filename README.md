# github.com/mdwhatcott/tictactoe

To begin, please download [Go 1.16+](https://golang.org/dl/).

To run the tests:

```
$ make test
go fmt ./...
go mod tidy
go test -cover ./...
?   	github.com/mdwhatcott/tictactoe	[no test files]
ok  	github.com/mdwhatcott/tictactoe/ttt	0.803s	coverage: 100.0% of statements
```

What you're seeing here is that:

1. all the code has been well-formatted (`go fmt ./...`), 
2. the module files (`go.mod` and `go.sum`) are well-formatted and all dependencies are resolved (`go mod tidy`), 
3. all tests pass (`go test -cover ./...`), and 
4. the code in the `ttt` package is 100% covered by its tests. Yes, that's correct, 100% code coverage (except for the wireup code at the project root (main.go), which is not covered by unit tests). 

Without the `-v` flag the test runner doesn't emit output except in the case of failed tests. To see verbose test output simply add the `-v` flag to the `go test` command:

```
go test -cover -v ./...
$ go test -cover -v ./...
?   	github.com/mdwhatcott/tictactoe	[no test files]
=== RUN   TestBoardSuite
=== PAUSE TestBoardSuite
=== RUN   TestCPUAgentSuite
=== PAUSE TestCPUAgentSuite
=== RUN   TestGameSuite
=== PAUSE TestGameSuite
=== RUN   TestHumanAgentSuite
...
... some output omitted
...
--- PASS: TestCPUAgentSuite (0.00s)
    --- PASS: TestCPUAgentSuite/TestOnlyOneSpotRemaining (0.00s)
    --- PASS: TestCPUAgentSuite/TestOneOfTheRemainingSpotsWins (0.00s)
    --- PASS: TestCPUAgentSuite/TestTwoMovesOut (0.00s)
    --- PASS: TestCPUAgentSuite/TestBestCounter (0.08s)
    --- PASS: TestCPUAgentSuite/TestBestOpening (0.48s)
    --- PASS: TestCPUAgentSuite/TestVsEndsInTie (0.50s)
PASS
coverage: 100.0% of statements
ok  	github.com/mdwhatcott/tictactoe/ttt	0.751s	coverage: 100.0% of statements
```

To see a coverage report (this command will open a web browser to render the html report):

```
$ go test '-coverprofile=/tmp/coverage.out' ./... && go tool cover '-html=/tmp/coverage.out'
?   	github.com/mdwhatcott/tictactoe	[no test files]
ok  	github.com/mdwhatcott/tictactoe/ttt	0.816s	coverage: 100.0% of statements
```

To build the executable and print CLI usage:

```
$ make build
go build && ./tictactoe -help
Usage of ./tictactoe:
  -o	when set, o is played by a human
  -x	when set, x is played by a human
```

To run an automated game (cpu vs. cpu):

```
$ ./tictactoe 
The winner is: Tie

X|X|O   | | 
-+-+-  -+-+-
O|O|X   | | 
-+-+-  -+-+-
X|O|X   | | 
```

The CPU agent is implemented using a variation of the [minimax algorithm](https://en.wikipedia.org/wiki/Minimax) and will never lose. The minimax algorithm is a good choice for situations involving combinatorics. It brute forces the situation with the maximum gain and minimum loss. It's also the first thing that comes up when you google `tic tac toe ai`...

To run the game w/ `x` played by computer and `o` by a human:

```
$ ./tictactoe -o
...
```
