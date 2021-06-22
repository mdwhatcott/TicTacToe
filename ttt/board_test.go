package ttt

import (
	"testing"

	"github.com/mdwhatcott/testing/should"
	"github.com/mdwhatcott/testing/suite"
)

func TestBoardSuite(t *testing.T) {
	suite.Run(&BoardSuite{T: suite.New(t)}, suite.Options.UnitTests())
}

type BoardSuite struct {
	*suite.T
	board Board
}

func (this *BoardSuite) TestInitialBoard_NoWinner() {
	this.So(Board{}.Winner(), should.Equal, N)
}
func (this *BoardSuite) TestCatNoWinner() {
	this.So(Board{
		X, O, O,
		O, X, X,
		O, X, O,
	}.Winner(), should.Equal, Tie)
}

func (this *BoardSuite) TestWinnerIs0() {
	this.So(this.board.Place(O, row1...).Winner(), should.Equal, O)
	this.So(this.board.Place(O, row2...).Winner(), should.Equal, O)
	this.So(this.board.Place(O, row3...).Winner(), should.Equal, O)

	this.So(this.board.Place(O, col1...).Winner(), should.Equal, O)
	this.So(this.board.Place(O, col2...).Winner(), should.Equal, O)
	this.So(this.board.Place(O, col3...).Winner(), should.Equal, O)

	this.So(this.board.Place(O, diag1...).Winner(), should.Equal, O)
	this.So(this.board.Place(O, diag2...).Winner(), should.Equal, O)
}
func (this *BoardSuite) TestWinnerIsX() {
	this.So(this.board.Place(X, row1...).Winner(), should.Equal, X)
	this.So(this.board.Place(X, row2...).Winner(), should.Equal, X)
	this.So(this.board.Place(X, row3...).Winner(), should.Equal, X)

	this.So(this.board.Place(X, col1...).Winner(), should.Equal, X)
	this.So(this.board.Place(X, col2...).Winner(), should.Equal, X)
	this.So(this.board.Place(X, col3...).Winner(), should.Equal, X)

	this.So(this.board.Place(X, diag1...).Winner(), should.Equal, X)
	this.So(this.board.Place(X, diag2...).Winner(), should.Equal, X)
}

func (this *BoardSuite) TestRender() {
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
