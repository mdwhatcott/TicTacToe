package ttt

import (
	"io"
	"testing"

	"github.com/mdwhatcott/testing/should"
	"github.com/mdwhatcott/testing/suite"
)

func TestGameSuite(t *testing.T) {
	suite.Run(&GameSuite{T: suite.New(t)}, suite.Options.UnitTests())
}

type GameSuite struct {
	*suite.T
	agent *FakeAgent
	game  *Game
}

func (this *GameSuite) Setup() {
	this.agent = NewFakeAgent()
	this.game = NewGame(io.Discard, Board{}, this.agent, this.agent)
}

func (this *GameSuite) TestXWinsOnRow1() {
	this.agent.PrepareMoves(X, row1...)
	this.agent.PrepareMoves(O, row2...)
	winner := this.game.Play()
	this.So(winner, should.Equal, X)
}
func (this *GameSuite) TestOWinsOnRow2() {
	this.agent.PrepareMoves(X, 0, 2, 7)
	this.agent.PrepareMoves(O, row2...)
	winner := this.game.Play()
	this.So(winner, should.Equal, O)
}
func (this *GameSuite) TestCat() {
	this.agent.PrepareMoves(X, 0, 2, 4, 5, 7)
	this.agent.PrepareMoves(O, 1, 3, 6, 8)
	winner := this.game.Play()
	this.So(winner, should.Equal, Tie)
}

/***************************************************************************/

type FakeAgent struct {
	move  string
	moves map[string][]int
}

func NewFakeAgent() *FakeAgent {
	return &FakeAgent{
		move:  X,
		moves: make(map[string][]int),
	}
}

func (this *FakeAgent) PrepareMoves(player string, squares ...int) {
	this.moves[player] = append(this.moves[player], squares...)
}

func (this *FakeAgent) Move(_ Board) int {
	moves := this.moves[this.move]
	move := moves[0]
	this.moves[this.move] = moves[1:]
	this.move = opposite[this.move]
	return move
}
