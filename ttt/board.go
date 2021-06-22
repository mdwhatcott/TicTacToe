package ttt

import (
	"fmt"
	"strconv"
	"strings"
)

const (
	Tie = "Tie"
	X   = "X"
	O   = "O"
	N   = ""
)

type Board [9]string

func (this Board) Winner() string {
	if this.isWinner(O) {
		return O
	}
	if this.isWinner(X) {
		return X
	}
	if len(this.ScanAvailable()) == 0 {
		return Tie
	}
	return N
}

func (this Board) ScanAvailable() (result []int) {
	for x, spot := range this {
		if spot == N {
			result = append(result, x)
		}
	}
	return result
}

func (this Board) Place(player string, squares ...int) Board {
	result := this
	for _, square := range squares {
		result[square] = player
	}
	return result
}

func (this Board) isWinner(player string) bool {
	for _, combo := range winners {
		if this.checkWinner(player, combo) {
			return true
		}
	}
	return false
}

func (this Board) checkWinner(player string, combo []int) bool {
	for _, square := range combo {
		if this[square] != player {
			return false
		}
	}
	return true
}

var (
	winners = [][]int{
		row1, row2, row3,
		col1, col2, col3,
		diag1, diag2,
	}

	row1 = []int{0, 1, 2}
	row2 = []int{3, 4, 5}
	row3 = []int{6, 7, 8}

	col1 = []int{0, 3, 6}
	col2 = []int{1, 4, 7}
	col3 = []int{2, 5, 8}

	diag1 = []int{0, 4, 8}
	diag2 = []int{2, 4, 6}
)

func render(board Board) string {
	b := new(strings.Builder)
	b.WriteString("\n")
	_, _ = fmt.Fprintf(b, "%s|%s|%s  %s|%s|%s\n",
		orSpace(board[0]), orSpace(board[1]), orSpace(board[2]),
		spot(board[0], 0), spot(board[1], 1), spot(board[2], 2))
	_, _ = fmt.Fprintf(b, "-+-+-  -+-+-\n")
	_, _ = fmt.Fprintf(b, "%s|%s|%s  %s|%s|%s\n",
		orSpace(board[3]), orSpace(board[4]), orSpace(board[5]),
		spot(board[3], 3), spot(board[4], 4), spot(board[5], 5))
	_, _ = fmt.Fprintf(b, "-+-+-  -+-+-\n")
	_, _ = fmt.Fprintf(b, "%s|%s|%s  %s|%s|%s\n",
		orSpace(board[6]), orSpace(board[7]), orSpace(board[8]),
		spot(board[6], 6), spot(board[7], 7), spot(board[8], 8))
	return b.String()
}

func spot(r string, s int) string {
	if r == "" {
		return strconv.Itoa(s)
	}
	return " "
}
func orSpace(r string) string {
	if r == "" {
		return " "
	}
	return r
}
