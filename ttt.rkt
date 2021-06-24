#!/usr/bin/env racket

#lang racket/base

(require racket/list
         racket/string
         racket/set)

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

(define (winner? board)
  (set-member? WINS board))

;;;;

(require rackunit)

(for ([win WINS])
  (test-true "winners" (winner? win)))