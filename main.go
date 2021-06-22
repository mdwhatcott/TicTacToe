package main

import (
	"flag"
	"fmt"
	"io"
	"os"

	"github.com/mdwhatcott/tictactoe/ttt"
)

func main() {
	var xHuman bool
	var oHuman bool

	flag.BoolVar(&xHuman, "x", false, "when set, x is played by a human")
	flag.BoolVar(&oHuman, "o", false, "when set, o is played by a human")

	flag.Parse()

	screen := os.Stdout
	input := os.Stdin

	game := ttt.NewGame(
		screen,
		ttt.Board{},
		agent(xHuman, ttt.X, screen, input),
		agent(oHuman, ttt.O, screen, input),
	)

	winner := game.Play()

	fmt.Println("The winner is:", winner)
}

func agent(human bool, player string, screen io.Writer, input io.Reader) ttt.Agent {
	if human {
		return ttt.NewHumanAgent(player, screen, input)
	}
	return ttt.NewCPUAgent(player)
}