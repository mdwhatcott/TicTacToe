(ns gui.core-spec
  (:require [speclj.core :refer :all]
            [gui.core :refer :all]))

(describe "Root Update Function"

  ; TODO: Mock quil mouse functions, which the subordinate update functions rely on.
  #_(context "Screen Transitions"

    (it "maintains current screen when no transition was requested"
      (let [state   (setup-root)
            updated (update-root state)]
        (should= state updated)))

    (it "transitions to the next screen when a transition is requested"
      (let [state   (merge (setup-root) {:transition :value-not-important})
            updated (update-root state)]
        (should= (transitions (:screen state)) (:screen updated)))))

  (it "initializes brand new state for next game"
    (let [game-over-state {}
          new-game-state  (update-root game-over-state)]
      (should= (setup-root) new-game-state)))
  )
