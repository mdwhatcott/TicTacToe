(ns ttt.ai
  (:require [ttt.grid :refer :all]))

(def max-depth 10)

(defn minimax [grid depth is-max? mark]
  (let [open-cells (:empty-cells grid)
        game-won?  (boolean (winner grid))
        game-tied? (empty? open-cells)]
    (cond
      (> depth max-depth) 0
      game-won? (if is-max? (- max-depth depth) (- depth max-depth))
      game-tied? 0
      :else
      (let [is-max?    (not is-max?)
            mark       (other mark)
            children   (map #(place mark % grid) open-cells)
            scores     (map #(minimax % (inc depth) is-max? mark) children)
            mixed      (interleave open-cells scores)
            pairs      (partition 2 mixed)
            ranked     (sort-by last (if is-max? > <) pairs)
            best-score (second (first ranked))] best-score))))

(defn suggest [mark grid]
  (let [open-cells (:empty-cells grid)
        children   (map #(place mark % grid) open-cells)
        scores     (map #(minimax % 0 true mark) children)
        mixed      (interleave open-cells scores)
        pairs      (partition 2 mixed)
        ranked     (sort-by last > pairs)
        best-move  (ffirst ranked)] best-move))
