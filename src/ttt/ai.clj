(ns ttt.ai
  (:require [ttt.grid :refer :all]
            [ttt.terminal-ui :as ui]))


(defn minimax [grid depth is-max? mark]
  (let [open-cells (available-cells grid)
        game-tied? (empty? open-cells)
        game-won?  (not (nil? (winner grid)))]
    (cond
      (zero? depth) 0
      game-tied? 0
      game-won? (if is-max? depth (- depth))
      :else
      (let [is-max?    (not is-max?)
            mark       (other mark)
            children   (map #(place mark % grid) open-cells)
            scores     (map #(minimax % (dec depth) is-max? mark) children)
            mixed      (interleave open-cells scores)
            pairs      (partition 2 mixed)
            ranked     (sort-by last pairs)
            max-ranked (if is-max? (reverse ranked) ranked)
            best-score (second (first max-ranked))]
        ;(println max-ranked)
        ;(println (ui/render-grid grid))
        best-score))))

(defn suggest [mark grid]
  (let [
        open-cells (available-cells grid)
        depth      (count open-cells)
        children   (map #(place mark % grid) open-cells)
        scores     (map #(minimax % depth true mark) children)
        mixed      (interleave open-cells scores)
        pairs      (partition 2 mixed)
        ranked     (reverse (sort-by last pairs))
        best-move  (ffirst ranked)
        ]
    ;(println ranked)
    best-move))
