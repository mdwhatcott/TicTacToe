package main

import (
	"flag"
	"fmt"
	"os"

	"github.com/mdwhatcott/tictactoe/ttt"
)

func main() {
	config := parseCLIFlags(os.Args)

	game := ttt.NewGame(
		ttt.Board{},
		agent(config.xHuman, ttt.X),
		agent(config.oHuman, ttt.O),
	)

	final := game.Play()

	fmt.Printf("\n"+
		"The winner is: %s\n"+
		"%s\n",
		final.Winner(),
		ttt.Render(final),
	)
}

type Config struct {
	xHuman bool
	oHuman bool
}

func parseCLIFlags(args []string) (config Config) {
	flags := flag.NewFlagSet(args[0], flag.ExitOnError)
	flags.BoolVar(&config.xHuman, "x", false, "when set, x is played by a human")
	flags.BoolVar(&config.oHuman, "o", false, "when set, o is played by a human")
	_ = flags.Parse(args[1:])
	return config
}

func agent(isHuman bool, player string) ttt.Agent {
	if isHuman {
		return ttt.NewTerminalUIAgent(player, os.Stdout, os.Stdin)
	}
	return ttt.NewCPUAgent(player)
}
