package ttt

import (
	"testing"

	"github.com/mdwhatcott/testing/should"
	"github.com/mdwhatcott/testing/suite"
)

func TestCPUAgentSuite(t *testing.T) {
	suite.Run(&CPUAgentSuite{T: suite.New(t)}, suite.Options.UnitTests())
}

type CPUAgentSuite struct {
	*suite.T
	agent *CPUAgent
}

func (this *CPUAgentSuite) Setup() {
	this.agent = NewCPUAgent(X)
}

func (this *CPUAgentSuite) TestOnlyOneSpotRemaining() {
	spot := this.agent.Move(Board{
		X, X, O, // 0, 1, 2
		O, X, O, // 3, 4, 5
		X, O, N, // 6, 7, 8
	})

	this.So(spot, should.Equal, 8)
}

func (this *CPUAgentSuite) TestOneOfTheRemainingSpotsWins() {
	spot := this.agent.Move(Board{
		X, X, O, // 0, 1, 2
		O, X, O, // 3, 4, 5
		N, O, N, // 6, 7, 8  (8 wins for X, so it's better than 6)
	})

	this.So(spot, should.Equal, 8)
}

func (this *CPUAgentSuite) TestWinAtEarliestOpportunity() {
	this.agent.player = X

	spot := this.agent.Move(Board{
		X, O, O, // 0, 1, 2
		X, N, N, // 3, 4, 5
		N, N, N, // 6, 7, 8  (6 wins immediately)
	})

	this.So(spot, should.Equal, 6)
}

func (this *CPUAgentSuite) TestTwoMovesOut() {
	spot := this.agent.Move(Board{
		X, X, O, // 0, 1, 2
		O, N, N, // 3, 4, 5  (4, being a 'fork', is the best move)
		N, N, N, // 6, 7, 8
	})

	this.So(spot, should.Equal, 4)
}

func (this *CPUAgentSuite) TestBestOpening() {
	spot := this.agent.Move(Board{})
	this.So(spot, should.Equal, 0) // best move == first corner
}

func (this *CPUAgentSuite) TestBestCounter() {
	this.agent.player = O
	spot := this.agent.Move(Board{X})
	this.So(spot, should.Equal, 4) // best counter == center
}

func (this *CPUAgentSuite) TestVsEndsInTie_TryAllStartingMoves() {
	board := Board{}
	for startingX := 0; startingX < len(board); startingX++ {
		game := NewGame(
			board.Place(X, startingX),
			NewCPUAgent(X),
			NewCPUAgent(O),
		)
		game.player = opposite[game.player] // since X has already taken the first move

		winner := game.Play().Winner()

		this.So(winner, should.Equal, Tie)
	}
}
