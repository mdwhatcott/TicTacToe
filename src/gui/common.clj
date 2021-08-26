(ns gui.common
  (:require [quil.core :as q]))

(def screen-width 500)
(def text-size (/ screen-width 24))
(def background-color 240)
(def hovering-color 200)
(def cell-color 255)
(def mark-color 50)
(def losing-color [200 0 0])
(def winning-color [0 200 0])

(defn render-text [x y size text]
  (q/fill 0)
  (q/text-size size)
  (q/text text x y))

(defn render-rectangle [color [[x1 y1] [x2 y2]]]
  (q/fill color)
  (q/rect x1 y1 (- x2 x1) (- y2 y1)))

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
      {:x         x
       :y         y
       :width     cell-width-px
       :box       [[x y] [(+ x cell-width-px) (+ y cell-width-px)]]
       :center    [(+ x (/ cell-width-px 2)) (+ y (/ cell-width-px 2))]
       :mark      nil
       :hovering? false
       :winner?   false
       :loser?    false})))

(defn draw-x [{:keys [center width]}]
  (let [x (first center)
        y (second center)
        x1 (- x (* 0.3 width))
        y1 (- y (* 0.3 width))
        x2 (+ x (* 0.3 width))
        y2 (+ y (* 0.3 width))]
    (q/stroke-weight 10)
    (q/line x1 y1 x2 y2)
    (q/line x2 y1 x1 y2)))

(defn draw-o [{:keys [center width]}]
  (q/stroke-weight 10)
  (q/fill 255)
  (let [x (first center)
        y (second center)
        width (* 0.8 width)]
    (q/ellipse x y width width)))

(defn render-grid-cells [thickness cells]
  (q/fill cell-color)
  (q/stroke-weight thickness)

  (doseq [c cells]
    (q/stroke mark-color)
    (q/rect (:x c) (:y c) (:width c) (:width c))
    (cond (:hovering? c) (q/stroke hovering-color)
          (:winner? c) (apply q/stroke winning-color)
          (:loser? c) (apply q/stroke losing-color)
          (:tied? c) (apply q/stroke losing-color))
    (cond (= :X (:mark c)) (draw-x c)
          (= :O (:mark c)) (draw-o c)))

  ; draw over the outer rectangle
  (let [upper-left-cell  (first cells)
        lower-right-cell (last cells)
        width            (:width upper-left-cell)
        upper-left       [(:x upper-left-cell) (:y upper-left-cell)]
        lower-right      [(+ width (:x lower-right-cell)) (+ width (:y lower-right-cell))]
        [x1 y1] upper-left
        [x2 y2] lower-right
        upper-right      [x2 y1]
        lower-left       [x1 y2]]
    (q/stroke background-color)
    (q/line upper-left upper-right)
    (q/line upper-right lower-right)
    (q/line lower-right lower-left)
    (q/line lower-left upper-left))
  )

(defn render-grid [thickness row-count upper-left lower-right]
  (let [cells (assemble-grid-cells row-count upper-left lower-right)]
    (render-grid-cells thickness cells)))

(defn process-mouse [state]
  (let [ready?   (get-in state [:mouse :ready-to-click])
        pressed? (q/mouse-pressed?)]
    (-> state
        (assoc-in [:mouse :x] (q/mouse-x))
        (assoc-in [:mouse :y] (q/mouse-y))
        (assoc-in [:mouse :clicked?] (and ready? pressed?))
        (assoc-in [:mouse :ready-to-click] (not pressed?)))))
