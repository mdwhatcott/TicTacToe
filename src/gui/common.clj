(ns gui.common
  (:require [quil.core :as q]))

(def screen-width 500)
(def text-size (/ screen-width 24))
(def background-color 240)
(def highlight-color 200)

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

(defn render-grid [thickness row-count upper-left lower-right]
  (q/fill 255)
  (q/stroke-weight thickness)

  (let [[x1 y1] upper-left
        [x2 y2] lower-right
        total-width-px (- x2 x1)
        cell-width-px  (/ total-width-px row-count)]
    (q/stroke 50)
    (doseq [y (range y1 (+ y1 total-width-px) cell-width-px)
            x (range x1 (+ x1 total-width-px) cell-width-px)]
      (q/rect x y cell-width-px cell-width-px))

    (let [upper-right [x2 y1]
          lower-left  [x1 y2]]
      (q/stroke 240)
      (q/line upper-left upper-right)
      (q/line upper-right lower-right)
      (q/line lower-right lower-left)
      (q/line lower-left upper-left))))

