(ns gui.core-spec
  (:require [speclj.core :refer :all]
            [gui.core :refer :all]))

(describe "Root Update Function"

  (context "Screen Transitions"

    (it "maintains current screen when no transition was requested"
      (let [state   {:current-screen :choose-grid}
            updated (update-root state)]
        (should= state updated)))

    (it "transitions to the next screen when a transition is requested"
      (let [state   {:current-screen :choose-grid
                     :transition     :value-not-important}
            updated (update-root state)]
        (should= (transitions (:current-screen state)) (:current-screen updated)))))

  )
