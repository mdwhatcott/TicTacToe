package ttt

import (
	"bufio"
	"fmt"
	"io"
	"strconv"
)

type HumanAgent struct {
	player string
	screen io.Writer
	input  *bufio.Scanner
}

func NewHumanAgent(player string, screen io.Writer, input io.Reader) Agent {
	return &HumanAgent{
		player: player,
		screen: screen,
		input:  bufio.NewScanner(input),
	}
}

func (this HumanAgent) Move(board Board) int {
	available := board.ScanAvailable()
	for attempt := 0; ; attempt++ {
		if attempt > 0 {
			_, _ = fmt.Fprintln(this.screen, "invalid choice, try again!")
		}
		this.prompt(available)

		choice, err := strconv.Atoi(this.readline())
		if err != nil {
			continue
		}
		if !in(choice, available) {
			continue
		}
		return choice
	}
}

func (this HumanAgent) prompt(available []int) {
	_, _ = fmt.Fprintf(
		this.screen,
		"Player %s: Where would you like to place an '%s'? %v > ",
		this.player,
		this.player,
		available,
	)
}

func (this HumanAgent) readline() string {
	this.input.Scan()
	return this.input.Text()
}

func in(needle int, haystack []int) bool {
	for _, straw := range haystack {
		if straw == needle {
			return true
		}
	}
	return false
}
