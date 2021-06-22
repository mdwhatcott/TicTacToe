package ttt

import (
	"fmt"
	"io"
)

type Agent interface {
	Move(Board) int
}

type Game struct {
	screen io.Writer
	board  Board
	agents map[string]Agent
	player string
}

func NewGame(screen io.Writer, board Board, x, o Agent) *Game {
	return &Game{
		screen: screen,
		board:  board,
		agents: map[string]Agent{X: x, O: o},
		player: O,
	}
}

func (this *Game) Play() (winner string) {
	defer this.render()
	for x := 0; x < len(this.board); x++ {
		this.render()
		this.player = opposite[this.player]
		nextMove := this.agents[this.player].Move(this.board)
		this.board = this.board.Place(this.player, nextMove)
		if this.board.Winner() == this.player {
			return this.player
		}
	}

	return Tie
}

func (this *Game) render() {
	_, _ = fmt.Fprintln(this.screen, render(this.board))
}

var opposite = map[string]string{
	X: O,
	O: X,
}
