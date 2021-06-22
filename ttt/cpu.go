package ttt

type CPUAgent struct {
	player string
}

func NewCPUAgent(player string) *CPUAgent {
	return &CPUAgent{player: player}
}

func (this CPUAgent) Move(board Board) int {
	best := -1
	bestSpot := -1
	for _, spot := range board.ScanAvailable() {
		score := minimax(board.Place(this.player, spot), opposite[this.player], true)
		if score > best {
			best = score
			bestSpot = spot
		}
	}
	return bestSpot
}

func minimax(board Board, nextPlayer string, isMaxPlayer bool) int {
	winner := board.Winner()

	if winner == Tie {
		return 0
	} else if winner != N {
		if isMaxPlayer {
			return 10
		} else {
			return -10
		}
	}

	bestScore := -0xFFFFFFFF
	minOrMax := max
	if isMaxPlayer {
		bestScore *= -1
		minOrMax = min
	}

	for _, available := range board.ScanAvailable() {
		next := board.Place(nextPlayer, available)
		bestScore = minOrMax(bestScore, minimax(next, opposite[nextPlayer], !isMaxPlayer))
	}

	return bestScore

}

func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}
func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}
