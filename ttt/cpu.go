package ttt

type CPUAgent struct {
	player string
}

func NewCPUAgent(player string) *CPUAgent {
	return &CPUAgent{player: player}
}

func (this CPUAgent) Move(board Board) int {
	var (
		bestScore, bestSpot = -1, -1
		availableSpots      = board.ScanAvailable()
		depth               = len(availableSpots)
	)
	for _, spot := range availableSpots {
		option := board.Place(this.player, spot)
		score := minimax(depth, option, this.player, true)
		if score > bestScore {
			bestScore = score
			bestSpot = spot
		}
	}
	return bestSpot
}

func minimax(depth int, board Board, player string, isMaxPlayer bool) int {
	winner := board.Winner()

	if winner == Tie {
		return 0
	}
	if winner != N && isMaxPlayer {
		return depth
	}
	if winner != N {
		return -depth
	}
	if depth == 0 {
		return 0
	}

	bestScore := -0xFFFFFFFF
	minOrMax := max
	if isMaxPlayer {
		bestScore *= -1
		minOrMax = min
	}

	for _, spot := range board.ScanAvailable() {
		otherPlayer := opposite[player]
		nextBoard := board.Place(otherPlayer, spot)
		score := minimax(depth-1, nextBoard, otherPlayer, !isMaxPlayer)
		bestScore = minOrMax(bestScore, score)
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
