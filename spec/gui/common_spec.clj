(ns gui.common_spec
  (:require
    [speclj.core :refer :all]
    [gui.common :refer :all]
    [gui.screen :as screen]))

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

(describe "Mouse Input"

  (context "in the beginning"
    (it "is ready to receive mouse clicks in the very beginning"
      (let [state (screen/setup-state 0)
            {:keys [ready-to-click? clicked?]} (:mouse state)]
        (should= true ready-to-click?)
        (should= false clicked?)))

    (it "doesn't know where the mouse is in the very beginning"
      (let [state (screen/setup-state 0)
            {:keys [x y]} (:mouse state)]
        (should= nil x)
        (should= nil y)))
    )

  (context "after updates"
    (it "always knows where the mouse is"
      (let [state   (screen/setup-state 0)
            updated (update-mouse state false 42 43)
            {:keys [x y]} (:mouse updated)]
        (should= 42 x)
        (should= 43 y)))

    (it "recognizes an initial click"
      (let [state   (screen/setup-state 0)
            updated (update-mouse state true 0 0)]
        (println (:mouse state))
        (println (:mouse updated))
        (should= true (get-in updated [:mouse :clicked?]))))

    (it "only recognizes a click for a single frame/update"
      (let [state  (screen/setup-state 0)
            frame1 (update-mouse state true 0 0)
            frame2 (update-mouse frame1 true 0 0)]
        (should= false (get-in frame2 [:mouse :clicked?]))))

    (it "recognizes a subsequent click only after the button has been released"
      (let [state           (screen/setup-state 0)
            initial-click   (update-mouse state true 0 0)
            still-holding   (update-mouse initial-click true 0 0)
            button-released (update-mouse still-holding false 0 0)
            next-click      (update-mouse button-released true 0 0)]
        (should= true (get-in next-click [:mouse :clicked?]))))
    )
  )

(describe "2-Dimensional Grid Layout"
  (it "lays out a 2-dimensional square grid (2x2) for convenient rendering"
    (->>
      (assemble-grid-cells 2 [0 0] [10 10])
      (should= [{:width  5 :x 0 :y 0
                 :center [5/2 5/2] :box [[0 0] [5 5]]
                 :mark   nil :hovering? false :loser? false :winner? false}

                {:width  5 :x 5 :y 0
                 :center [15/2 5/2] :box [[5 0] [10 5]]
                 :mark   nil :hovering? false :loser? false :winner? false}

                {:width  5 :x 0 :y 5
                 :center [5/2 15/2] :box [[0 5] [5 10]]
                 :mark   nil :hovering? false :loser? false :winner? false}

                {:width  5 :x 5 :y 5
                 :center [15/2 15/2] :box [[5 5] [10 10]]
                 :mark   nil :hovering? false :loser? false :winner? false}])))
  )