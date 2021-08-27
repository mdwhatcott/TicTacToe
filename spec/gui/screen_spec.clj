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
