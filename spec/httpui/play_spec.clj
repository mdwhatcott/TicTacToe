(ns httpui.play-spec
  (:require
    [speclj.core :refer :all]
    [httpui.play :refer :all]
    [hiccup.core :as hiccup]))

(describe "HTTP UI: Turn-by-turn play"
  (it "interprets the form as game state (appending the incremented move to the list)"
    (let [form  {"moves"      ["0 1 2"]
                 "move"       ["5"]
                 "grid-width" ["4"]}
          state (form->game-state form)]
      (should= {:x-player   :human
                :o-player   :human
                :moves      [0 1 2 6]
                :grid-width 4} state)))

  (it "plays the game and returns updated state (including the winner!)"
    (let [before {;;           X O X O X O X (winner: X)
                  :moves      [0 1 2 3 4 5 6]
                  :grid-width 3}
          after  (play before)]
      (should= after {:moves         [0 1 2 3 4 5 6]
                      :grid-width    3
                      :mark          nil
                      :rendered-grid (str "X|O|X   | | \n"
                                          "-+-+-  -+-+-\n"
                                          "O|X|O   | | \n"
                                          "-+-+-  -+-+-\n"
                                          "X| |    |8|9\n"
                                          "\n"
                                          "Winner: X\n"
                                          "")})))

  (context "HTML page rendering"
    (context "game not yet over"
      (it "renders the grid and the next-move form"
        (let [state {:moves         [0 1 2 3 4 5]
                     :grid-width    4
                     :mark          :O
                     :rendered-grid "<fake-rendered-grid>"}]
          (should= [:html
                    [:head]
                    [:body
                     [:h2 "TicTacToe"]
                     [:pre [:code "<fake-rendered-grid>"]]
                     [:form {:method "post" :action "/ttt/play"}
                      [:input {:type "hidden" :name "moves" :value "0 1 2 3 4 5"}]
                      [:input {:type "hidden" :name "grid-width" :value "4"}]
                      [:label {:for "guess"} "Player O: where would you like to move:"]
                      [:input {:type "number" :name "move" :required "true"}]
                      [:input {:type "submit" :value "Submit"}]]]]
                   (render-page state)))))

    (context "game over"
      (it "renders the grid"
        (let [state      {:moves         [0 1 2 3 4 5 6]
                          :grid-width    3
                          :mark          nil
                          :rendered-grid "<fake-rendered-grid>"}
              renderable (render-page state)]
          (should= [:html
                    [:head]
                    [:body
                     [:h2 "TicTacToe"]
                     [:pre [:code "<fake-rendered-grid>"]]
                     [:a {:href "/ttt"} "Play again?"]]]
                   renderable)
          (should= "" (hiccup/html renderable)))))

    )

  )
