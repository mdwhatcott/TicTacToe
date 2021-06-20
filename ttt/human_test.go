package ttt

import (
	"bytes"
	"strings"
	"testing"

	"github.com/mdwhatcott/testing/should"
	"github.com/mdwhatcott/testing/suite"
)

func TestHumanAgentSuite(t *testing.T) {
	suite.Run(&HumanAgentSuite{T: suite.New(t)}, suite.Options.UnitTests())
}

type HumanAgentSuite struct {
	*suite.T
}

func (this *HumanAgentSuite) TestMoveSelection() {
	screen := new(bytes.Buffer)
	input := strings.NewReader(
		strings.Join([]string{
			"not-a-number",
			"-1", // too low
			"9",  // too high
			"0",  // not available
			"1",  // not available
			"2",  // not available
			"3",  // not available
			"4",  // not available
			"5",  // not available
			"6",  // available!
			"7",  // available!
			"8",  // available!
		}, "\n"),
	)
	board := Board{
		X, X, O,
		O, O, X,
	}
	agent := NewHumanAgent(X, screen, input)

	this.So(agent.Move(board), should.Equal, 6)
	this.So(agent.Move(board), should.Equal, 7)
	this.So(agent.Move(board), should.Equal, 8)
}
