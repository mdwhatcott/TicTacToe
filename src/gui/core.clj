(ns gui.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [gui.choose-grid :as choose-grid]))

(def transitions
  {:choose-grid :player1
   :player1     :player2
   :player2     :in-play
   :in-play     :game-over
   :game-over   :choose-grid})

(def updates
  {:choose-grid #'choose-grid/update
   :player1     nil
   :player2     nil
   :in-play     nil
   :game-over   nil})

(def drawings
  {:choose-grid #'choose-grid/draw
   :player1     nil
   :player2     nil
   :in-play     nil
   :game-over   nil})

(def screen-width 500)

(defn setup-root []
  {:current-screen :choose-grid
   :screen-anchors {:choose-grid (choose-grid/calculate-anchors screen-width)
                    :player1     nil
                    :player2     nil
                    :in-play     nil
                    :game-over   nil}})

(defn update-root [state]
  ((updates (:current-screen state)) state))

(defn draw-root [state]
  (q/frame-rate 30)
  (q/background 240)
  ((drawings (:current-screen state)) state))

(declare tic-tac-toe)


(defn -main [& args]
  (q/defsketch
    tic-tac-toe
    :title "Tic-Tac-Toe"
    :size [screen-width screen-width]
    :setup #'setup-root
    :update #'update-root
    :draw #'draw-root
    :features [:keep-on-top]
    :middleware [m/fun-mode]))