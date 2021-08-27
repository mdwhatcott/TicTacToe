(ns gui.core
  (:require
    [quil.core :as q]
    [quil.middleware :as m]
    [gui.render :as r]
    [gui.common :as c]
    [gui.choose-grid :as choose-grid]
    [gui.configure-players :as configure-players]
    [gui.arena :as arena]))

(def transitions
  {:choose-grid       :configure-players
   :configure-players :arena
   :arena             :choose-grid})

(defn setup-anchors []
  {:choose-grid
   (choose-grid/calculate-anchors r/screen-width)

   :configure-players
   (configure-players/calculate-anchors r/screen-width)

   :arena
   (arena/calculate-anchors r/screen-width)})

(defn setup-root []
  {:transition       false
   :screen           :choose-grid
   :screens          (setup-anchors)

   :game-grid        nil
   :mark             :X
   :player1          nil
   :player2          nil

   :mouse            {:ready-for-click? true
                      :clicked          false
                      :x                nil
                      :y                nil}
   :ready-for-click? true
   :clicked?         false})

(def updates
  {:choose-grid       choose-grid/update_
   :configure-players configure-players/update_
   :arena             arena/update_})

(defn update-screen [state]
  (let [current-screen (:screen state)
        next-screen    (transitions current-screen)
        updater        (updates current-screen)
        updated        (updater state)]
    (if (true? (:transition updated))
      (-> updated (assoc :screen next-screen
                         :transition false))
      updated)))

(defn update-root [state]
  (update-screen (c/process-mouse state)))

(def drawings
  {:choose-grid       choose-grid/draw
   :configure-players configure-players/draw
   :arena             arena/draw})

(defn draw-root [state]
  (q/frame-rate 30)
  (q/background r/background-color)
  (let [screen (:screen state)
        draw   (drawings screen)]
    (draw state)))

(declare tic-tac-toe)

(defn -main [& _args]
  (q/defsketch
    tic-tac-toe
    :title "Tic-Tac-Toe"
    :size [r/screen-width r/screen-width]
    :setup #'setup-root
    :update #'update-root
    :draw #'draw-root
    :features [:keep-on-top]
    :middleware [m/fun-mode]))
