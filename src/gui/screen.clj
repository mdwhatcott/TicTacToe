(ns gui.screen
  (:require
    [gui.choose-grid :as choose-grid]
    [gui.configure-players :as configure-players]
    [gui.arena :as arena]))

(def updates-by-screen
  {:choose-grid       choose-grid/update_
   :configure-players configure-players/update_
   :arena             arena/update_})

(def drawings-by-screen
  {:choose-grid       choose-grid/draw
   :configure-players configure-players/draw
   :arena             arena/draw})

(defn anchors-by-screen [screen-width]
  {:choose-grid       (choose-grid/calculate-anchors screen-width)
   :configure-players (configure-players/calculate-anchors screen-width)
   :arena             (arena/calculate-anchors screen-width)})

(defn setup-state [screen-width]
  {:transition false
   :screen     :choose-grid
   :screens    (anchors-by-screen screen-width)

   :game-grid  nil
   :mark       :X
   :player1    nil
   :player2    nil

   :gui-grid   nil
   :mouse      {:ready-to-click? true
                :clicked?        false
                :x               nil
                :y               nil}})

(def screen-transitions
  {:choose-grid       :configure-players
   :configure-players :arena
   :arena             :choose-grid})


;; TODO: tests
(defn update_ [state updates-by-screen]
  (let [current-screen (:screen state)
        next-screen    (screen-transitions current-screen)
        updater        (updates-by-screen current-screen)
        updated        (updater state)]
    (if (true? (:transition updated))
      (-> updated (assoc :screen next-screen
                         :transition false))
      updated)))

;; TODO: test
(defn draw [state drawings-by-screen]
  (let [screen (:screen state)
        drawer (drawings-by-screen screen)]
    (drawer state)))
