package ttt

type Agent interface {
	Move(Board) int
}

type Game struct {
	board  Board
	agents map[string]Agent
	player string
}

func NewGame(board Board, x, o Agent) *Game {
	return &Game{
		board:  board,
		agents: map[string]Agent{X: x, O: o},
		player: O,
	}
}

func (this *Game) Play() (winner string) {
	for x := 0; x < len(this.board); x++ {
		this.player = opposite[this.player]
		nextMove := this.agents[this.player].Move(this.board)
		this.board = this.board.Place(this.player, nextMove)
		if this.board.Winner() == this.player {
			return this.player
		}
	}

	return Tie
}

var opposite = map[string]string{
	X: O,
	O: X,
}
