(ns gui.choose-grid-spec
  (:require
    [speclj.core :refer :all]
    [gui.choose-grid :refer :all]))

(defn check-valid-hovering-range [box-key expected-hover-element]
  (let [anchors     (calculate-anchors 10)
        box         (box-key anchors)
        upper-left  (first box)
        lower-right (second box)]
    (doseq [x (range (inc (first upper-left)) (first lower-right))
            y (range (inc (second upper-left)) (second lower-right))]
      (let [input  {:mouse   {:clicked? false :x x :y y}
                    :screens {:choose-grid anchors}}
            output (update_ input)]
        (should= nil (:transition? output))
        (should= nil (:game-grid output))
        (should= nil (:gui-grid output))
        (should= expected-hover-element (:hovering output))))))

(defn check-valid-selection-range [box-key expected-grid-cell-count]
  (let [anchors     (calculate-anchors 10)
        box         (box-key anchors)
        upper-left  (first box)
        lower-right (second box)]
    (doseq [x (range (inc (first upper-left)) (first lower-right))
            y (range (inc (second upper-left)) (second lower-right))]
      (let [input  {:mouse   {:clicked? true :x x :y y}
                    :screens {:choose-grid anchors}}
            output (update_ input)]
        (should= true (:transition? output))
        (should= expected-grid-cell-count (count (:gui-grid output)))
        (should= expected-grid-cell-count (count (:empty-cells (:game-grid output))))))))

(defn check-no-hover-range []
  (let [anchors              (calculate-anchors 10)
        top-half-upper-left  [0 0]
        top-half-lower-right [10 5]]
    (doseq [x (range (first top-half-upper-left) (first top-half-lower-right))
            y (range (second top-half-upper-left) (second top-half-lower-right))]
      (let [input  {:mouse   {:clicked? false :x x :y y}
                    :screens {:choose-grid anchors}}
            output (update_ input)]
        (should= nil (:transition? output))
        (should= nil (:game-grid output))
        (should= nil (:gui-grid output))
        (should= nil (:hovering output))))))

(describe "Screen: Choose Grid"

  (it "prepares for gameplay when the 3x3 board is clicked"
    (check-valid-selection-range :box3x3 9))

  (it "prepares for gameplay when the 4x4 board is clicked"
    (check-valid-selection-range :box4x4 16))

  (it "highlights the 3x3 grid when the cursor is above it"
    (check-valid-hovering-range :box3x3 :3x3))

  (it "highlights the 4x4 grid when the cursor is above it"
    (check-valid-hovering-range :box4x4 :4x4))

  (it "highlights no grid if cursor not above a grid"
    (check-no-hover-range))
  )
