(ns ttt.ai
  (:require [ttt.grid :refer :all]))

(defn minimax [depth grid mark is-max?]
  (if (zero? depth)
    0 (cond
        (nil? (winner grid))
        (let [
              available (available-spots grid)
              grids     (map #(place mark % grid) available)
              depth     (dec (count available))
              mark      (other mark)
              is-max?   (not is-max?)
              scores    (map #(minimax depth % mark is-max?) grids)
              pairs     (apply assoc {} (interleave scores available))
              ranked    (reverse (into (sorted-map) pairs))
              best      (last (first ranked))
              ]
          (println ranked)
          best)

        is-max? depth
        :else (- depth))))

(defn suggest [mark grid]
  (minimax (inc (count (available-spots grid))) grid mark true))