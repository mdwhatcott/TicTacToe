(ns ttt.core
  (:require [ttt.grid :as grid]
            [ttt.human :as human]
            [ttt.ai :as ai]
            [ttt.terminal-ui :as terminal]
            [ttt.game :as game]))

(defn -main []
  (let [presenter  terminal/print-grid
        grid       (grid/new-grid 3)
        player     :X
        player1-in (human/suggest terminal/prompt)
        player2-in ai/suggest]
    (println
      "winner: "
      (game/play presenter grid player player1-in player2-in))))
