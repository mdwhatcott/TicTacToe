package ttt

import (
	"testing"

	"github.com/mdwhatcott/testing/should"
	"github.com/mdwhatcott/testing/suite"
)

func TestRenderSuite(t *testing.T) {
	suite.Run(&RenderSuite{T: suite.New(t)}, suite.Options.UnitTests())
}

type RenderSuite struct {
	*suite.T
}

func (this *RenderSuite) TestRender() {
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
