(ns ttt.ai
  (:require [ttt.grid :refer :all]))

(defn max-depth [grid]
  (min 5 (- (:capacity grid) (count (:empty-cells grid)))))

(defn minimax [grid depth is-max? mark]
  (let [max-depth  (max-depth grid)
        open-cells (:empty-cells grid)
        game-won?  (boolean (:winner grid))
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

(defn do-minimax [mark grid]
  (let [open-cells (:empty-cells grid)
        children   (map #(place mark % grid) open-cells)
        scores     (map #(minimax % 0 true mark) children)
        mixed      (interleave open-cells scores)
        pairs      (partition 2 mixed)
        ranked     (sort-by last > pairs)
        best-move  (ffirst ranked)] best-move))

(defn suggest [mark grid]
  (if (> (count (:empty-cells grid)) 12)
    (first (:empty-cells grid))
    (do-minimax mark grid)))
