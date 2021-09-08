(ns tui.human
  (:require
    [ttt.grid :refer :all]
    [clojure.edn :as edn]
    [tui.prompts :as prompts]))

(defn suggest [mark grid]
  (loop [selection -1
         available #{}
         attempts  11]
    (cond (zero? attempts) (throw (Exception. "too many invalid responses"))
          (some? (available selection)) selection
          :else (recur (dec (edn/read-string (prompts/prompt-player-move mark)))
                       (set (:empty-cells grid))
                       (dec attempts)))))
