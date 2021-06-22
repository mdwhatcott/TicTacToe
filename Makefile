#!/usr/bin/make -f

test:
	go fmt ./...
	go mod tidy
	go test -cover ./...

build:
	go build && ./tictactoe -help