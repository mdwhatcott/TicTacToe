(ns gui.choose-grid
  (:require [quil.core :as q]))

(defn update [state]
  state)

(defn render-grid [row-count total-width-px start-point]
  (let [[start-x start-y] start-point
        cell-width-px (int (/ total-width-px row-count))]
    (doseq [y (range start-y (+ start-y total-width-px) cell-width-px)
            x (range start-x (+ start-x total-width-px) cell-width-px)]
      (q/rect x y cell-width-px cell-width-px))))

(defn calculate-anchors [screen-width]
  (let [center (/ screen-width 2)
        width  screen-width
        height screen-width]
    {:text-size            (/ height 24)
     :welcome-text         {:x (/ width 4) :y (/ height 10)}
     :what-size-text       {:x (/ width 3) :y (/ height 3)}
     :welcome-divider-line {:p1 [0,,,,, center] :p2 [width center]}
     :grid-divider-line    {:p1 [center center] :p2 [center height]}
     :grid3x3              {:width (/ width 2) :p1 [0 center]}
     :grid4x4              {:width (/ width 2) :p1 [center center]}}))

(defn draw [state]
  (let [{:keys [text-size welcome-text what-size-text
                welcome-divider-line grid-divider-line
                grid3x3 grid4x4]}
        (get-in state [:screen-anchors :choose-grid])]

    (q/stroke-weight 10)
    (q/stroke 50)
    (q/fill 0)
    (q/text-size text-size)

    (q/text "Welcome to Tic-Tac-Toe!" (:x welcome-text) (:y welcome-text))
    (q/line (:p1 welcome-divider-line) (:p2 welcome-divider-line))

    (q/text "What size grid?" (:x what-size-text) (:y what-size-text))
    (q/line (:p1 grid-divider-line) (:p2 grid-divider-line))

    (q/stroke-weight 2)
    (q/fill 255)
    (render-grid 3 (:width grid3x3) (:p1 grid3x3))
    (render-grid 4 (:width grid4x4) (:p1 grid4x4))))
