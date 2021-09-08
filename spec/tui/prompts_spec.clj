(ns tui.prompts-spec
  (:require
    [speclj.core :refer :all]
    [tui.prompts :refer :all]))

(describe "Prompts"
  (with-stubs)

  (context "low-level prompt function"
    (it "prints and prompts"
      (let [printer (stub :printer)
            flusher (stub :flusher)
            reader  (stub :reader {:return "the-answer"})]

        (with-redefs [print     printer
                      flush     flusher
                      read-line reader]

          (let [response (prompt "the-question")]

            (should= response "the-answer")
            (should-have-invoked :printer {:with ["the-question"] :times 1})
            (should-have-invoked :flusher {:with [] :times 1})
            (should-have-invoked :reader {:with [] :times 1}))))))

  (context "prompt-options function"
    (it "repeats the prompt if the response is not a valid option"
      (let [prompter (stub :prompter {:return "the-wrong-answer"})
            options  {"the-right-answer" "hooray"}]
        (with-redefs [prompt prompter]
          (should-throw (prompt-options "the-question" options))
          (should-have-invoked
            :prompter {:with ["the-question (\"the-right-answer\"): "] :times 10}))))

    (it "returns the first right answer"
      (let [prompter (stub :prompter {:return "the-right-answer"})
            options  {"the-right-answer" "hooray"}]
        (with-redefs [prompt prompter]
          (should= "hooray" (prompt-options "the-question" options))
          (should-have-invoked
            :prompter {:with ["the-question (\"the-right-answer\"): "] :times 1}))))
    )

  (context "prompt-player function"
    (it "repeats the prompt if the response is not a valid option"
      (let [prompter (stub :prompter {:return "invalid"})]
        (with-redefs [prompt prompter]
          (should-throw Exception (prompt-player :X))
          (should-have-invoked
            :prompter
            {:with  ["Who will be playing 'X'? (\"human\" \"easy\" \"medium\" \"hard\"): "]
             :times 10}))))

    (it "accepts human as a valid option"
      (let [prompter (stub :prompter {:return "human"})]
        (with-redefs [prompt prompter]
          (let [response (prompt-player :X)]
            (should= :human response)))))

    (it "accepts easy as a valid option"
      (let [prompter (stub :prompter {:return "easy"})]
        (with-redefs [prompt prompter]
          (let [response (prompt-player :X)]
            (should= :easy response)))))

    (it "accepts medium as a valid option"
      (let [prompter (stub :prompter {:return "medium"})]
        (with-redefs [prompt prompter]
          (let [response (prompt-player :X)]
            (should= :medium response)))))

    (it "accepts hard as a valid option"
      (let [prompter (stub :prompter {:return "hard"})]
        (with-redefs [prompt prompter]
          (let [response (prompt-player :X)]
            (should= :hard response)))))

    )

  (context "prompt-grid-size function"
    (it "repeats the prompt if the response is not a valid option"
      (let [prompter (stub :prompter {:return "invalid"})]
        (with-redefs [prompt prompter]
          (should-throw Exception (prompt-grid-size))
          (should-have-invoked
            :prompter {:with  ["What size grid? (\"3x3\" \"4x4\"): "]
                       :times 10}))))

    (it "accepts 3x3 as a valid option"
      (let [prompter (stub :prompter {:return "3x3"})]
        (with-redefs [prompt prompter]
          (let [response (prompt-grid-size)]
            (should= 3 response)))))

    (it "accepts 4x4 as a valid option"
      (let [prompter (stub :prompter {:return "4x4"})]
        (with-redefs [prompt prompter]
          (let [response (prompt-grid-size)]
            (should= 4 response)))))

    )

  )
