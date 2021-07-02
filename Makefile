#!/usr/bin/make -f

test:
	go fmt ./...
	go mod tidy
	go test -cover -count=1 ./...

build:
	go build && ./tictactoe -help
