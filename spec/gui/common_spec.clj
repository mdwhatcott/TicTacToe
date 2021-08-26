(ns gui.common_spec
  (:require
    [speclj.core :refer :all]
    [gui.common :refer :all]))

(describe "Bounding Boxes"
  (it "calculates a bounding box from a center point and width"
    (should= [[0 0] [10 10]] (bounding-box [5 5] 10)))

  (it "identifies when a containing coordinate"
    (should= true (bounded? [1 1] (bounding-box [5 5] 10)))
    (should= true (bounded? [9 9] (bounding-box [5 5] 10)))
    (should= false (bounded? [10 10] (bounding-box [5 5] 10)))
    (should= false (bounded? [1 11] (bounding-box [5 5] 10)))
    )
  )
