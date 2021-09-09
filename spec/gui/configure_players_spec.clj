(ns gui.configure-players-spec
  (:require
    [speclj.core :refer :all]
    [gui.configure-players :refer :all]))

(defn check-no-selection []
  (let [anchors     (calculate-anchors 20)
        option      (first anchors)
        box         (:box option)
        upper-left  (first box)
        lower-right (second box)]
    (doseq [x (range (inc (first upper-left)) (first lower-right))
            y (range (inc (second upper-left)) (second lower-right))]
      (let [input  {:mouse   {:clicked? true :x x :y y}
                    :screens {:configure-players anchors}}
            output (update_ input)]
        (should= nil (:transition? output))
        (should= nil (:hovering output))
        (should= nil (:player1 output))
        (should= nil (:player2 output))))))

(defn check-no-hover-range []
  (let [anchors     (calculate-anchors 20)
        option      (first anchors)
        box         (:box option)
        upper-left  (first box)
        lower-right (second box)]
    (doseq [x (range (inc (first upper-left)) (first lower-right))
            y (range (inc (second upper-left)) (second lower-right))]
      (let [input  {:mouse   {:clicked? false :x x :y y}
                    :screens {:configure-players anchors}}
            output (update_ input)]
        (should= nil (:transition? output))
        (should= nil (:hovering output))))))

(defn check-valid-hovering-range [option-index]
  (let [anchors     (calculate-anchors 20)
        option      (nth anchors option-index)
        box         (:box option)
        upper-left  (first box)
        lower-right (second box)]
    (doseq [x (range (inc (first upper-left)) (first lower-right))
            y (range (inc (second upper-left)) (second lower-right))]
      (let [input  {:mouse   {:clicked? false :x x :y y}
                    :screens {:configure-players anchors}}
            output (update_ input)]
        (should= nil (:transition? output))
        (should= option-index (:hovering output))))))

(defn check-selection-option
  [option-index configured-player-key expected-value should-transition?]
  (let [anchors     (calculate-anchors 20)
        option      (nth anchors option-index)
        box         (:box option)
        upper-left  (first box)
        lower-right (second box)]
    (doseq [x (range (inc (first upper-left)) (first lower-right))
            y (range (inc (second upper-left)) (second lower-right))]
      (let [input  {:mouse   {:clicked? true :x x :y y}
                    :screens {:configure-players anchors}
                    :player1 (when (= configured-player-key :player2)
                               :player1:selected-previously)}
            output (update_ input)]
        (should= should-transition? (:transition? output))
        (should= expected-value (configured-player-key output))))))

(describe "Screen: Configure Player"

  (context "Highlighting on-hover"
    (it "highlights nothing when hovering over dead space"
      (check-no-hover-range))

    (it "highlights the 'human' option when the cursor is above it"
      (check-valid-hovering-range 1))

    (it "highlights the 'easy computer' option when the cursor is above it"
      (check-valid-hovering-range 2))

    (it "highlights the 'medium computer' option when the cursor is above it"
      (check-valid-hovering-range 3))

    (it "highlights the 'hard computer' option when the cursor is above it"
      (check-valid-hovering-range 4)))

  (context "Configuring player 1"
    (it "assigns nothing when clicking in dead space"
      (check-no-selection))

    (it "assigns player1 the 'human' option when clicked"
      (check-selection-option 1 :player1 :human false))

    (it "assigns player1 the 'easy computer' option when clicked"
      (check-selection-option 2 :player1 :easy false))

    (it "assigns player1 the 'medium computer' option when clicked"
      (check-selection-option 3 :player1 :medium false))

    (it "assigns player1 the 'hard computer' option when clicked"
      (check-selection-option 4 :player1 :hard false)))

  (context "Configuring player 2"
    (it "assigns player2 the 'human' option when clicked (and transitions!)"
      (check-selection-option 1 :player2 :human true))

    (it "assigns player2 the 'easy computer' option when clicked (and transitions!)"
      (check-selection-option 2 :player2 :easy true))

    (it "assigns player2 the 'medium computer' option when clicked (and transitions!)"
      (check-selection-option 3 :player2 :medium true))

    (it "assigns player2 the 'hard computer' option when clicked (and transitions!)"
      (check-selection-option 4 :player2 :hard true)))

  )
