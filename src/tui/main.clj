(ns tui.main
  (:require
    [ttt.ai :as ai]
    [ttt.grid :as grid]
    [tui.game :as game]
    [tui.human :as human]
    [tui.grid :as terminal]
    [tui.prompts :as prompts]))

(defn initialize-player [mark]
  (let [player (prompts/prompt-player mark)]
    (if (= player :human)
      (human/suggest prompts/prompt-player-move)
      (case (prompts/prompt-difficulty)
        :easy ai/easy
        :medium ai/medium
        :hard ai/hard))))

(defn initialize-grid []
  (let [grid-size (prompts/prompt-grid-size)]
    (grid/new-grid grid-size)))

(defn initialize-new-game-state []
  (let [grid (initialize-grid)]
    {:grid    grid
     :mark    :X
     :player1 (initialize-player :X)
     :player2 (initialize-player :O)}))

(defn -main []
  (game/play2 terminal/print-grid (initialize-new-game-state)))
