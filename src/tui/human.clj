(ns tui.human
  (:require
    [ttt.grid :refer :all]
    [clojure.edn :as edn]))

(defn suggest [prompt]
  (fn [mark grid]
    (loop [selection          -1
           available          #{}
           attempts-remaining 11]
      (cond
        (zero? attempts-remaining) (throw (Exception. "too many invalid responses"))
        (some? (available selection)) selection
        :else (recur (dec (edn/read-string (prompt mark)))
                     (set (:empty-cells grid))
                     (dec attempts-remaining))))))
