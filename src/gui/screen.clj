(ns gui.screen
  (:require
    [gui.arena :as arena]
    [gui.conclude-game :as conclude-game]
    [gui.choose-grid :as choose-grid]
    [gui.configure-players :as configure-players]
    [gui.establish-game :as establish-game]
    [gui.store-turn :as store-turn]))

(def updates-by-screen
  {:choose-grid       choose-grid/update_
   :configure-players configure-players/update_
   :establish-game    establish-game/update_
   :arena             arena/update_
   :store-turn        store-turn/update_
   :conclude-game     conclude-game/update_})

(def drawings-by-screen
  {:choose-grid       choose-grid/draw
   :configure-players configure-players/draw
   :establish-game    configure-players/draw
   :arena             arena/draw
   :store-turn        arena/draw
   :conclude-game     arena/draw})

(defn anchors-by-screen [screen-width]
  {:choose-grid       (choose-grid/calculate-anchors screen-width)
   :configure-players (configure-players/calculate-anchors screen-width)
   :arena             (arena/calculate-anchors screen-width)})

(defn setup-state [screen-width]
  {:transition? false
   :screen      :choose-grid
   :screens     (anchors-by-screen screen-width)

   :game-grid   nil
   :mark        :X
   :player1     nil
   :player2     nil

   :gui-grid    nil
   :mouse       {:ready-to-click? true
                 :clicked?        false
                 :x               nil
                 :y               nil}})

(defn update_ [state by-screen]
  (let [updater (get by-screen (:screen state))]
    (updater state)))

(defn draw [state by-screen]
  (let [drawer (get by-screen (:screen state))]
    (drawer state)))
