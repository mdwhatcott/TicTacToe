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
	totalMoves := len(this.board.ScanAvailable())

	for i := 0; i < totalMoves; i++ {
		this.switchPlayer()
		this.makeMove()
		if this.gameHasBeenWon() {
			return this.player
		}
	}

	return Tie
}

func (this *Game) switchPlayer() {
	this.player = opposite[this.player]
}
func (this *Game) makeMove() {
	nextMove := this.agents[this.player].Move(this.board)
	this.board = this.board.Place(this.player, nextMove)
}
func (this *Game) gameHasBeenWon() bool {
	return this.board.Winner() == this.player
}

var opposite = map[string]string{
	X: O,
	O: X,
}
