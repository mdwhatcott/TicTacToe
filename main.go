package main

import (
	"fmt"
	"os"

	"github.com/mdwhatcott/tictactoe/ttt"
)

func main() {
	game := ttt.NewGame(
		ttt.NewHumanAgent(ttt.X, os.Stdout, os.Stdin),
		ttt.NewHumanAgent(ttt.O, os.Stdout, os.Stdin),
	)
	winner := game.Play()
	fmt.Println("The winner is:", winner)
}
