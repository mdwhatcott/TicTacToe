(ns ttt.ai
  (:require [ttt.grid :refer :all]))

(defn minimax [grid depth mark is-max?]
  (let [remaining  (available-spots grid)
        game-tied? (empty? remaining)
        game-won?  (not (nil? (winner grid)))]
    (cond
      (zero? depth) 0
      game-tied? 0
      game-won? (let [score (count remaining)
                      final (if is-max? score (- score))] final)
      :else (let [mark       (other mark)
                  is-max?    (not is-max?)
                  depth      (dec depth)
                  evolutions (map #(place mark % grid) remaining)
                  scores     (map #(minimax % depth mark is-max?) evolutions)
                  pairs      (interleave scores remaining)
                  mapped     (apply assoc {} pairs)
                  ranked     (into (sorted-map) mapped)
                  best       (last (first ranked))] best))))

(defn suggest [mark grid]
  (minimax grid 4 (other mark) false))