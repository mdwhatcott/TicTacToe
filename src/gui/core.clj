(ns gui.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [gui.common :as c]
            [gui.choose-grid :as choose-grid]
            [gui.configure-players :as configure-player]))

(def transitions
  {:choose-grid       :configure-players
   :configure-players :arena
   :arena             :choose-grid})

(defn setup-root []
  {:transition false
   :screen     :choose-grid
   :screens    {:choose-grid       (choose-grid/calculate-anchors c/screen-width)
                :configure-players (configure-player/calculate-anchors c/screen-width)}
   :game-grid  nil
   :player1    nil
   :player2    nil})

(def updates
  {:choose-grid       #'choose-grid/update
   :configure-players #'configure-player/update
   :arena             nil})

(defn update-root [state]
  (if (empty? state)
    (setup-root)
    (let [current-screen (:screen state)
          next-screen    (transitions current-screen)
          updater        (updates current-screen)
          updated        (updater state)]
      (if (true? (:transition updated))
        (-> updated (assoc :screen next-screen
                           :transition false))
        updated))))

(def drawings
  {:choose-grid       #'choose-grid/draw
   :configure-players #'configure-player/draw
   :arena             nil})

(defn draw-root [state]
  (q/frame-rate 30)
  (q/background c/background-color)
  ((drawings (:screen state)) state))

(declare tic-tac-toe)

(defn -main [& _args]
  (q/defsketch
    tic-tac-toe
    :title "Tic-Tac-Toe"
    :size [c/screen-width c/screen-width]
    :setup #'setup-root
    :update #'update-root
    :draw #'draw-root
    :features [:keep-on-top]
    :middleware [m/fun-mode]))