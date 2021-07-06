#!/usr/bin/env racket

#lang racket/base

(require racket/list
         racket/string
         racket/set
         racket/stream)

(define X #\x)
(define O #\o)
(define N #\n)
(define TIE #\t)

(define ROW1 (set 0 1 2))
(define ROW2 (set 3 4 5))
(define ROW3 (set 6 7 8))

(define COL1 (set 0 3 6))
(define COL2 (set 1 4 7))
(define COL3 (set 2 5 8))

(define DIA1 (set 0 4 8))
(define DIA2 (set 2 4 6))

(define WINS (set
  ROW1 ROW2 ROW3
  COL1 COL2 COL3
  DIA1 DIA2))

(define (new-board) #hash())

(define (place board player slot)
  (if (equal? (hash-ref board slot N) N)
    (hash-set board slot player) board))

(define (winner board)
  (define (is-winner player board)
    (define (extract player board)
      (for/set ([(key value) (in-hash board)]
                #:when (equal? value player)) key))
    (set-member? WINS (extract player board)))
  (cond [(is-winner X board) X]
        [(is-winner O board) O]
        [(= 9 (hash-count board)) TIE]
        [else N]))

(define (scan-available board)
  (for/list ([i (in-range 9)]
             #:when (not (hash-has-key? board i))) i))

(define (smart-move player board)
  
  (define (minimax depth board player is-max-player?)
    (let ([w (winner board)])
      (cond [(equal? w TIE) 0]
            [(and (not (equal? w N)) is-max-player?) depth]
            [(not (equal? w N)) (- depth)]
            [else 0]))) ; TODO: scan available and recurse to find best spot/score
  8) ; TODO: scan available and minimax each to find the best move

;;;;

(require rackunit)

(test-equal? "place X" X (hash-ref (place (new-board) X 4) 4))
(test-equal? "place O" O (hash-ref (place (new-board) O 4) 4))

(test-equal? "place O over X: nope" X
  (hash-ref (place (place (new-board) X 4) O 4) 4))

(test-equal? "place X over O: nope" O
  (hash-ref (place (place (new-board) O 4) X 4) 4))

(test-equal? "new board nobody is winner yet" N
  (winner (new-board)))

(test-equal? "incomplete board nobody is winner yet" N
  (winner #hash(
    (0 . #\x) (2 . #\o))))

(test-equal? "X is winner" X
  (winner #hash(
    (0 . #\n) (1 . #\n) (2 . #\x)
    (3 . #\n) (4 . #\o) (5 . #\x)
    (6 . #\o) (7 . #\n) (8 . #\x))))

(test-equal? "O is winner" O
  (winner #hash(
    (0 . #\n) (1 . #\n) (2 . #\o)
    (3 . #\n) (4 . #\x) (5 . #\o)
    (6 . #\x) (7 . #\n) (8 . #\o))))

(test-equal? "cat is winner" TIE
  (winner #hash(
    (0 . #\x) (1 . #\o) (2 . #\o)
    (3 . #\o) (4 . #\x) (5 . #\x)
    (6 . #\x) (7 . #\o) (8 . #\o))))

(test-equal? "scan-available - empty board - full list"
  (stream->list (in-range 9))
  (scan-available (new-board)))

(test-equal? "scan-available - full board - null list"
  (list)
  (scan-available #hash(
    (0 . #\x) (1 . #\o) (2 . #\o)
    (3 . #\o) (4 . #\x) (5 . #\x)
    (6 . #\x) (7 . #\o) (8 . #\o))))

(test-equal? "scan-available - partial board - null list"
  (list 2 5 8)
  (scan-available #hash(
    (0 . #\x) (1 . #\o) ; (2 . #\o)
    (3 . #\o) (4 . #\x) ; (5 . #\x)
    (6 . #\x) (7 . #\o) ; (8 . #\o)
    )))


(test-equal? "mate in 1, x to play" 8
  (smart-move X #hash(
    (0 . #\x) (1 . #\o) (2 . #\o)
    (3 . #\o) (4 . #\x) (5 . #\x)
    (6 . #\x) (7 . #\o))))