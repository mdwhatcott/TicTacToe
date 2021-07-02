package main

import (
	"flag"
	"fmt"
	"os"

	"github.com/mdwhatcott/tictactoe/ttt"
)

func main() {
	var xHuman bool
	var oHuman bool

	flag.BoolVar(&xHuman, "x", false, "when set, x is played by a human")
	flag.BoolVar(&oHuman, "o", false, "when set, o is played by a human")
	flag.Parse()

	game := ttt.NewGame(
		ttt.Board{},
		agent(xHuman, ttt.X),
		agent(oHuman, ttt.O),
	)
	fmt.Println("The winner is:", game.Play())
}

func agent(isHuman bool, player string) ttt.Agent {
	if isHuman {
		return ttt.NewTerminalUIAgent(player, os.Stdout, os.Stdin)
	}
	return ttt.NewCPUAgent(player)
}
