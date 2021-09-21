(ns httpui.play-spec
  (:require
    [speclj.core :refer :all]
    [httpui.play :refer :all]))

(describe "HTTP UI: Turn-by-turn play"
  (context "parse form"

    (it "ignores nil input"
      (let [input  nil
            output (parse-form input)]
        (should= nil output)))

    (it "ignores empty input"
      (let [input  ""
            output (parse-form input)]
        (should= nil output)))

    (it "parses form fields"
      (let [input  (str
                     "grid-width=3&"
                     "move=4&"
                     "moves=0%201%202")                     ; 0 1 2
            output (parse-form input)]
        (should= {:grid-width 3
                  :move       4
                  :moves      [0 1 2]} output)))
    )

  (context "render html")
  )