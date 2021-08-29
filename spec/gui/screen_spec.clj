(ns gui.screen-spec
  (:require
    [speclj.core :refer :all]
    [gui.screen :refer :all]))

(defn update-without-transition [state] (assoc state :transition? false))
(defn update-with-transition [state] (assoc state :transition? true))

(def test-transitions {:1 :2})

(describe "Screen Transitions"
  (it "does not transition if not called for"
    (let [state (->> {:screen :1}
                     update-without-transition
                     (transition test-transitions))]
      (should= :1 (:screen state))))

  (it "transitions when called for"
    (let [state (->> {:screen :1}
                     update-with-transition
                     (transition test-transitions))]
      (should= :2 (:screen state))))
  )

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