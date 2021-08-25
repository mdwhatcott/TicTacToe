(ns gui.configure-player
  (:require [quil.core :as q]
            [gui.common :as c]))

(defn update [player]
  (fn [state]
    state))

(defn calculate-anchors [screen-width]
  (let []
    {}))

(defn draw [player]
  (fn [state]
    (let [{:keys []} (get-in state [:screen-anchors :player])]

      )))
