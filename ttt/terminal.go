package ttt

import (
	"bufio"
	"fmt"
	"io"
	"strconv"
)

type TerminalUIAgent struct {
	player string
	screen io.Writer
	input  *bufio.Scanner
}

func NewTerminalUIAgent(player string, screen io.Writer, input io.Reader) Agent {
	return &TerminalUIAgent{
		player: player,
		screen: screen,
		input:  bufio.NewScanner(input),
	}
}

func (this TerminalUIAgent) Move(board Board) int {
	available := board.ScanAvailable()
	for attempt := 0; ; attempt++ {
		choice, err := this.prompt(attempt, board)
		if err != nil {
			continue
		}
		if in(choice, available) {
			return choice
		}
	}
}

func (this TerminalUIAgent) prompt(attempt int, board Board) (choice int, err error) {
	if attempt > 0 {
		_, _ = fmt.Fprintln(this.screen, "invalid choice, try again!")
	}
	_, _ = fmt.Fprintf(this.screen, ""+
		"\n"+
		"%s"+"\n"+
		"\n"+
		"Player %s: Where would you like to place your next '%s'? > ",

		render(board),
		this.player,
		this.player,
	)

	_ = this.input.Scan()
	return strconv.Atoi(this.input.Text())
}

func in(needle int, haystack []int) bool {
	for _, straw := range haystack {
		if straw == needle {
			return true
		}
	}
	return false
}
