(ns gui.choose-grid
  (:require
    [gui.common :as c]
    [gui.render :as r]
    [ttt.grid :as grid]))

(def rows3x3 3)
(def rows4x4 4)

(defn initialize-grid [state rows]
  (let [screen-width (get-in state [:screens :choose-grid :screen-width])]
    (assoc state :game-grid (grid/new-grid rows)
                 :gui-grid (c/assemble-grid-cells rows [0 0] [screen-width screen-width])
                 :transition true)))

(defn update_ [state]
  (let [anchors      (get-in state [:screens :choose-grid])
        box3x3       (get anchors :box3x3)
        box4x4       (get anchors :box4x4)
        mx           (get-in state [:mouse :x])
        my           (get-in state [:mouse :y])
        clicked?     (get-in state [:mouse :clicked?])
        hovering3x3? (c/bounded? [mx my] box3x3)
        hovering4x4? (c/bounded? [mx my] box4x4)]
    (cond
      (and hovering3x3? clicked?) (initialize-grid state rows3x3)
      (and hovering4x4? clicked?) (initialize-grid state rows4x4)
      hovering3x3? (assoc state :hovering :3x3)
      hovering4x4? (assoc state :hovering :4x4)
      :else (dissoc state :hovering))))

(defn calculate-anchors [screen-width]
  (let [center         (/ screen-width 2)
        half           (/ center 2)
        width          screen-width
        height         screen-width
        grid3x3-center [half (+ center half)]
        grid3x3-bounds (c/bounding-box grid3x3-center (/ width 3))
        box3x3-bounds  (c/bounding-box grid3x3-center (/ width 2))
        grid4x4-center [(+ center half) (+ center half)]
        grid4x4-bounds (c/bounding-box grid4x4-center (/ width 3))
        box4x4-bounds  (c/bounding-box grid4x4-center (/ width 2))]
    {:screen-width   screen-width
     :welcome-text   {:x (/ width 4) :y (/ height 10)}
     :what-size-text {:x (/ width 3) :y (/ height 3)}
     :box3x3         box3x3-bounds
     :box4x4         box4x4-bounds
     :grid3x3        {:p1 (first grid3x3-bounds) :p2 (second grid3x3-bounds)}
     :grid4x4        {:p1 (first grid4x4-bounds) :p2 (second grid4x4-bounds)}}))

(def line-thickness 2)

(defn draw [state]
  (let [hovered (:hovering state)
        anchors (get-in state [:screens :choose-grid])
        {:keys [welcome-text what-size-text
                box3x3 box4x4
                grid3x3 grid4x4]} anchors]

    (r/render-text (:x welcome-text) (:y welcome-text) r/text-size "Welcome to Tic-Tac-Toe!")
    (r/render-text (:x what-size-text) (:y what-size-text) r/text-size "What size grid?")

    (cond (= hovered :3x3) (r/render-rectangle r/hovering-color box3x3)
          (= hovered :4x4) (r/render-rectangle r/hovering-color box4x4))

    (r/render-grid line-thickness rows3x3 (:p1 grid3x3) (:p2 grid3x3))
    (r/render-grid line-thickness rows4x4 (:p1 grid4x4) (:p2 grid4x4))))
