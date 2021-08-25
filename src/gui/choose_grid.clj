(ns gui.choose-grid
  (:require [quil.core :as q]
            [gui.common :as c]))

(defn update [state]
  state)

(defn calculate-anchors [screen-width]
  (let [center         (/ screen-width 2)
        half           (/ center 2)
        width          screen-width
        height         screen-width
        grid3x3-center [half (+ center half)]
        grid3x3-bounds (c/bounding-box grid3x3-center (/ width 3))
        grid4x4-center [(+ center half) (+ center half)]
        grid4x4-bounds (c/bounding-box grid4x4-center (/ width 3))]
    {:text-size            (/ height 24)
     :welcome-text         {:x (/ width 4) :y (/ height 10)}
     :what-size-text       {:x (/ width 3) :y (/ height 3)}
     :grid3x3              {:p1 (first grid3x3-bounds) :p2 (second grid3x3-bounds)}
     :grid4x4              {:p1 (first grid4x4-bounds) :p2 (second grid4x4-bounds)}}))

(def line-thickness 2)
(def row-count-3x3 3)
(def row-count-4x4 4)

(defn draw [state]
  (let [{:keys [text-size welcome-text what-size-text grid3x3 grid4x4]}
        (get-in state [:screen-anchors :choose-grid])]

    (c/render-text (:x welcome-text) (:y welcome-text) text-size "Welcome to Tic-Tac-Toe!")
    (c/render-text (:x what-size-text) (:y what-size-text) text-size "What size grid?")

    (c/render-grid line-thickness row-count-3x3 (:p1 grid3x3) (:p2 grid3x3))
    (c/render-grid line-thickness row-count-4x4 (:p1 grid4x4) (:p2 grid4x4))))
