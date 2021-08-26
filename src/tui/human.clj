(ns tui.human
  (:require
    [ttt.grid :refer :all]
    [clojure.edn :as edn]))

(defn suggest [prompt]
  (fn [mark grid]
    (loop [selection -1
           available #{}]
      (if (not (nil? (available selection)))
        selection
        (recur (dec (edn/read-string (prompt mark)))
               (set (:empty-cells grid)))))))
