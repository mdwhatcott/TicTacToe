package ttt

import (
	"fmt"
	"strconv"
	"strings"
)

func render(board Board) string {
	b := new(strings.Builder)
	b.WriteString("\n")
	_, _ = fmt.Fprintln(b, renderFullRow(board, 0))
	_, _ = fmt.Fprintln(b, "-+-+-  -+-+-")
	_, _ = fmt.Fprintln(b, renderFullRow(board, 3))
	_, _ = fmt.Fprintln(b, "-+-+-  -+-+-")
	_, _ = fmt.Fprintln(b, renderFullRow(board, 6))
	return b.String()
}
func renderFullRow(board Board, start int) string {
	return fmt.Sprintf("%s|%s|%s  %s|%s|%s",
		taken(board[start]),
		taken(board[start+1]),
		taken(board[start+2]),
		open(board[start], start),
		open(board[start+1], start+1),
		open(board[start+2], start+2),
	)
}
func open(r string, s int) string {
	if r == "" {
		return strconv.Itoa(s)
	}
	return " "
}
func taken(r string) string {
	if r == "" {
		return " "
	}
	return r
}
