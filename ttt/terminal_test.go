package ttt

import (
	"io/ioutil"
	"strings"
	"testing"

	"github.com/mdwhatcott/testing/should"
	"github.com/mdwhatcott/testing/suite"
)

func TestTerminalUIAgentSuite(t *testing.T) {
	suite.Run(&TerminalUIAgentSuite{T: suite.New(t)}, suite.Options.UnitTests())
}

type TerminalUIAgentSuite struct {
	*suite.T
}

func (this *TerminalUIAgentSuite) TestMoveSelection() {
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

	agent := NewTerminalUIAgent(X, ioutil.Discard, strings.NewReader(inputs))

	this.So(agent.Move(board), should.Equal, 6)
	this.So(agent.Move(board), should.Equal, 7)
	this.So(agent.Move(board), should.Equal, 8)
}

func (this *TerminalUIAgentSuite) TestRender() {
	this.So(render(Board{}), should.Equal, "\n"+
		" | |   0|1|2\n"+
		"-+-+-  -+-+-\n"+
		" | |   3|4|5\n"+
		"-+-+-  -+-+-\n"+
		" | |   6|7|8\n",
	)

	this.So(render(Board{
		X, X, O,
		O, O, X,
		X, O, X,
	}), should.Equal, "\n"+
		"X|X|O   | | \n"+
		"-+-+-  -+-+-\n"+
		"O|O|X   | | \n"+
		"-+-+-  -+-+-\n"+
		"X|O|X   | | \n",
	)
}
