(ns gui.render
  (:require
    [quil.core :as q]
    [gui.common :as c]))

(def screen-width 1000)
(def text-size (/ screen-width 24))
(def background-color 240)
(def hovering-color 200)
(def cell-color 255)
(def mark-color 50)
(def losing-color [200 0 0])
(def winning-color [0 200 0])
(def grid-thickness-multiplier 0.05)

(defn render-text [x y size text]
  (q/fill 0)
  (q/text-size size)
  (q/text text x y))

(defn render-rectangle [color [[x1 y1] [x2 y2]]]
  (q/fill color)
  (q/rect x1 y1 (- x2 x1) (- y2 y1)))

(defn draw-x [{:keys [center width]}]
  (let [x  (first center)
        y  (second center)
        x1 (- x (* 0.3 width))
        y1 (- y (* 0.3 width))
        x2 (+ x (* 0.3 width))
        y2 (+ y (* 0.3 width))]
    (q/stroke-weight (* grid-thickness-multiplier width))
    (q/line x1 y1 x2 y2)
    (q/line x2 y1 x1 y2)))

(defn draw-o [{:keys [center width]}]
  (q/stroke-weight (* grid-thickness-multiplier width))
  (q/fill 255)
  (let [x     (first center)
        y     (second center)
        width (* 0.8 width)]
    (q/ellipse x y width width)))

(defn render-grid-cells [cells]
  (q/fill cell-color)

  (q/stroke-weight (* grid-thickness-multiplier (:width (first cells))))

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

(defn render-grid [row-count upper-left lower-right]
  (let [cells (c/assemble-grid-cells row-count upper-left lower-right)]
    (render-grid-cells cells)))

