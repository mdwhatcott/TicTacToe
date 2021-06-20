package ttt

import (
	"io"
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
	board := Board{
		X, X, O, // 0, 1, 2
		O, O, X, // 3, 4, 5
		//          6, 7, 8
	}
	inputs := strings.Join([]string{
		"not-a-number",
		"-1", // too low
		"9",  // too high
		"0",  // unavailable
		"1",  // unavailable
		"2",  // unavailable
		"3",  // unavailable
		"4",  // unavailable
		"5",  // unavailable
		"6",  // available!
		"7",  // available!
		"8",  // available!
	}, "\n")

	agent := NewHumanAgent(X, io.Discard, strings.NewReader(inputs))

	this.So(agent.Move(board), should.Equal, 6)
	this.So(agent.Move(board), should.Equal, 7)
	this.So(agent.Move(board), should.Equal, 8)
}
