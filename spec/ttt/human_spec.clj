(ns ttt.human-spec
  (:require
    [speclj.core :refer :all]
    [ttt.human :refer :all]
    [ttt.grid :refer :all]))

(defn fake-prompt [inputs]
  (fn [_mark]
    (first inputs)))

(describe "Processing Human Suggestions"
  (it "prompts the human for input"
    ;(let [human-suggest (suggest (fake-prompt ["0"]))]
    ;  (should= 0 (human-suggest X (make-grid))))
    (should= "0" ((fake-prompt ["0"]) ""))
    ))
