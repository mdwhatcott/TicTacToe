(ns tui.human-spec
  (:require
    [speclj.core :refer :all]
    [tui.human :refer :all]
    [tui.prompts :as prompts]))

(describe "Human Suggestions"
  (with-stubs)

  (it "repeats the prompt when an invalid response is provided"
    (let [prompter (stub :prompter {:return "invalid"})]
      (with-redefs [prompts/prompt-player-move prompter]
        (should-throw Exception (suggest :X {:empty-cells [0 1 2]})))))

  (it "accepts a valid answer"
    (let [prompter (stub :prompter {:return "2"})]
      (with-redefs [prompts/prompt-player-move prompter]
        (should= 1 (suggest :X {:empty-cells [1]})))))
  )
