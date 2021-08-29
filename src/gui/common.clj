(ns gui.common)

(defn bounded? [[x y] [[x1 y1] [x2 y2]]]
  (and (> x x1) (< x x2)
       (> y y1) (< y y2)))

(defn bounding-box [[x y] width]
  (let [half (/ width 2)
        x1   (- x half)
        y1   (- y half)
        x2   (+ x half)
        y2   (+ y half)]
    [[x1 y1] [x2 y2]]))

(defn assemble-grid-cells [row-count upper-left lower-right]
  (let [[x1 y1] upper-left
        [x2 __] lower-right
        total-width-px (- x2 x1)
        cell-width-px  (/ total-width-px row-count)]

    (for [y (range y1 (+ y1 total-width-px) cell-width-px)
          x (range x1 (+ x1 total-width-px) cell-width-px)]
      ; TODO: assign numeric id to allow easier identification of cells later.
      {:x         x
       :y         y
       :width     cell-width-px
       :box       [[x y] [(+ x cell-width-px) (+ y cell-width-px)]]
       :center    [(+ x (/ cell-width-px 2)) (+ y (/ cell-width-px 2))]
       :mark      nil
       :hovering? false
       :winner?   false
       :loser?    false})))

(defn update-mouse [state pressed? mouse-x mouse-y]
  (let [ready? (get-in state [:mouse :ready-to-click?])]
    (-> state
        (assoc-in [:mouse :x] mouse-x)
        (assoc-in [:mouse :y] mouse-y)
        (assoc-in [:mouse :clicked?] (boolean (and ready? pressed?)))
        (assoc-in [:mouse :ready-to-click?] (not pressed?)))))
