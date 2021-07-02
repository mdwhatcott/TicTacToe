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

func (this *Game) Play() Board {
	for !this.gameOver() {
		this.switchPlayer()
		this.makeMove()
	}
	return this.board
}

func (this *Game) gameOver() bool {
	return this.board.Winner() != N
}
func (this *Game) switchPlayer() {
	this.player = opposite[this.player]
}
func (this *Game) makeMove() {
	nextMove := this.agents[this.player].Move(this.board)
	this.board = this.board.Place(this.player, nextMove)
}

func (this Game) Board() Board {
	return this.board
}

var opposite = map[string]string{
	X: O,
	O: X,
}
