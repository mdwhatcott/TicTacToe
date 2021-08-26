(ns gui.core-spec
  (:require
    [speclj.core :refer :all]
    [gui.core :refer :all]))

(describe "Root Update Function"

  ; TODO: Mock quil mouse functions, which the subordinate update functions rely on.
  #_(context "Screen Transitions"

      (it "maintains current screen when no transition was requested"
        (let [state   (setup-root)
              updated (update-screen state)]
          (should= state updated)))

      (it "transitions to the next screen when a transition is requested"
        (let [state   (merge (setup-root) {:transition true})
              updated (update-screen state)]
          (should= (transitions (:screen state)) (:screen updated))))
      )
  )
