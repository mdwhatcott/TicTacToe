(ns tui.main
  (:require
    [ttt.ai :as ai]
    [ttt.grid :as grid]
    [tui.game :as game]
    [tui.human :as human]
    [tui.terminal-ui :as terminal]))

(defn initialize-player [mark]
  (let [player (terminal/prompt-player mark)]
    (if (= player :human)
      (human/suggest terminal/prompt-player-move)
      (case (terminal/prompt-difficulty)
        :easy ai/easy
        :medium ai/medium
        :hard ai/hard))))

(defn initialize-grid []
  (let [grid-size (terminal/prompt-grid-size)] (grid/new-grid grid-size)))

(defn -main []
  (let [presenter terminal/print-grid
        player    :X
        grid      (initialize-grid)
        playerX   (initialize-player :X)
        playerO   (initialize-player :O)]
    (game/play presenter grid player playerX playerO)))
