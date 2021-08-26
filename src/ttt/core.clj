(ns ttt.core
  (:require
    [ttt.grid :as grid]
    [ttt.human :as human]
    [ttt.ai :as ai]
    [ttt.terminal-ui :as terminal]
    [ttt.game :as game]))

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
