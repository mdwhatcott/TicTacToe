package ttt

const (
	N Player = iota // 0
	X               // 1
	O               // 2
)

type Player int

type Board [9]Player

func (this Board) Winner() Player {
	if this.isWinner(O) {
		return O
	}
	if this.isWinner(X) {
		return X
	}
	return N
}

func (this Board) Place(player Player, squares ...int) Board {
	result := this
	for _, square := range squares {
		result[square] = player
	}
	return result
}

func (this Board) isWinner(player Player) bool {
	for _, combo := range winners {
		if this.checkWinner(player, combo) {
			return true
		}
	}
	return false
}

func (this Board) checkWinner(player Player, combo []int) bool {
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
