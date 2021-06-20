package ttt

const (
	N = ' '
	X = 'X'
	O = 'O'
)

type Board [9]rune

func (this Board) Winner() rune {
	if this.isWinner(O) {
		return O
	}
	if this.isWinner(X) {
		return X
	}
	return N
}

func (this Board) Place(player rune, squares ...int) Board {
	result := this
	for _, square := range squares {
		result[square] = player
	}
	return result
}

func (this Board) isWinner(player rune) bool {
	for _, combo := range winners {
		if this.checkWinner(player, combo) {
			return true
		}
	}
	return false
}

func (this Board) checkWinner(player rune, combo []int) bool {
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
