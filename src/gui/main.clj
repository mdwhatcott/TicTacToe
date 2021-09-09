(ns gui.main
  (:require
    [quil.core :as quil]
    [quil.middleware :as m]
    [gui.render :as render]
    [gui.screen :as screen]
    [gui.common :as common]
    [db.datomic :as db]
    [datomic.api :as d]))

(def screen-width (- (min (quil/screen-width)
                          (quil/screen-height)) 100))

(defn setup-root []
  (screen/setup-state screen-width))

(defn update-root [state]
  (let [pressed? (quil/mouse-pressed?)
        mouse-x  (quil/mouse-x)
        mouse-y  (quil/mouse-y)
        state    (common/update-mouse state pressed? mouse-x mouse-y)]
    (screen/update_ state screen/updates-by-screen)))

(defn draw-root [state]
  (quil/background render/background-color)
  (screen/draw state screen/drawings-by-screen))

(defn on-close [_state]
  (d/shutdown true))

(declare tic-tac-toe)

(defn -main [& _args]
  (quil/defsketch
    tic-tac-toe
    :title "Tic-Tac-Toe"
    :size [screen-width screen-width]
    :setup #'setup-root
    :update #'update-root
    :draw #'draw-root
    :on-close #'on-close
    :features [:keep-on-top]
    :middleware [m/fun-mode]))
