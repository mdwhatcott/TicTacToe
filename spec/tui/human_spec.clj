(ns tui.human-spec
  (:require
    [speclj.core :refer :all]
    [tui.human :refer :all]))

(describe "Human Suggestions"
  (with-stubs)

  (it "repeats the prompt when an invalid response is provided"
    (let [prompter  (stub :prompter {:return "invalid"})
          suggester (suggest prompter)]
      (should-throw Exception (suggester :X {:empty-cells [0 1 2]}))))

  (it "accepts a valid answer"
    (let [prompter  (stub :prompter {:return "2"})
          suggester (suggest prompter)
          response  (suggester :X {:empty-cells [1]})]
      (should= 1 response)))
  )
