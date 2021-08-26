(ns ttt.ai
  (:require
    [ttt.grid :refer :all]))

(defn minimax [grid fmax-depth depth is-max? mark]
  (let [max-depth  (fmax-depth grid)
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
            scores     (map #(minimax % fmax-depth (inc depth) is-max? mark) children)
            mixed      (interleave open-cells scores)
            pairs      (partition 2 mixed)
            ranked     (sort-by last (if is-max? > <) pairs)
            best-score (second (first ranked))] best-score))))

(defn- do-minimax [mark grid fmax-depth]
  (let [open-cells (:empty-cells grid)
        children   (map #(place mark % grid) open-cells)
        scores     (map #(minimax % fmax-depth 0 true mark) children)
        mixed      (interleave open-cells scores)
        pairs      (partition 2 mixed)
        ranked     (sort-by last > pairs)
        best-move  (ffirst ranked)] best-move))

(defn- max-depth [grid]
  (let [total-turns     (:capacity grid)
        remaining-turns (count (:empty-cells grid))
        turns-taken     (- total-turns remaining-turns)]
    (let [empty (count (:empty-cells grid))
          depth (if (> empty 9) 4 6)]
      (min depth turns-taken))))

(defn- handicap-max-depth [handicap]
  (fn [grid]
    (- (max-depth grid) handicap)))

(defn- random-empty-cell [grid]
  (let [empty-cells (vec (sort (:empty-cells grid)))]
    (get empty-cells (rand-int (count empty-cells)))))

(defn easy [_mark grid]
  (random-empty-cell grid))

;; Concept: start w/ random moves, progress to handicapped minimax, end w/ random moves (to allow possible win)
(defn medium [mark grid]
  (cond (> (count (:empty-cells grid)) 11) (random-empty-cell grid)
        (< (count (:empty-cells grid)) (dec (/ (:capacity grid) 2))) (random-empty-cell grid)
        :else (do-minimax mark grid (handicap-max-depth 1))))

(defn hard [mark grid]
  (if (> (count (:empty-cells grid)) 11)
    (random-empty-cell grid)
    (do-minimax mark grid max-depth)))
