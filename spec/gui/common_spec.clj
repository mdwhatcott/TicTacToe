(ns gui.common_spec
  (:require [speclj.core :refer :all]
            [gui.common :refer :all]))

(describe "Bounding Boxes"
  (it "calculates a bounding box from a center point and width"
    (should= [[0 0] [10 10]] (bounding-box [5 5] 10))))
