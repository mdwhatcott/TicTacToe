(ns gui.screen-spec
  (:require
    [speclj.core :refer :all]
    [gui.screen :refer :all]))

(describe "Update Function"
  (it "invokes subordinate update function indicated on state"
    (let [input             {:screen :1}
          updates-by-screen {:1 (fn [state] (assoc state :1 true))}
          output            (update_ input updates-by-screen)]
      (should= true (:1 output)))))

(describe "Draw Function"
  (it "invokes subordinate draw function indicated on state"
    (let [input {:screen :1}
          drawers-by-screen {:1 (fn [state] 42)}
          result (draw input drawers-by-screen)]
      (should= 42 result))))