(ns gui.configure-players
  (:require [quil.core :as q]
            [gui.common :as c]))

(defn update [state]
  (let [mx        (q/mouse-x)
        my        (q/mouse-y)
        clicked?  (q/mouse-pressed?)
        boxes     (get-in state [:screens :configure-players :x])
        hovering? (map #(c/bounded? [mx my] (:box %)) boxes)
        indexed   (map-indexed vector hovering?)
        element   (ffirst (drop-while #(not (second %)) indexed))]
    (let [player (if (nil? (:player1 state)) :player1 :player2)]
     (if (and clicked? (some? element) (> element 0))
       (assoc state player (:key (nth boxes element))
                    :transition (= player :player2))
       (assoc state :hovering element)))))

(defn calculate-anchors [screen-width]
  (let [sections     5
        height       (/ screen-width sections)
        boxes        (for [s (range sections)]
                       [[0 (* height s)]
                        [screen-width (+ height (* height s))]])
        text-anchors (for [s (range sections)]
                       {:x (/ screen-width 10)
                        :y (+ (/ height 2) (* height s))})
        keys         [nil
                      :human
                      :ai-easy
                      :ai-medium
                      :ai-hard]
        options      ["      Human"
                      "      Computer (easy)"
                      "      Computer (medium)"
                      "      Computer (hard)"]
        playerX      (cons "Configure Player X:" options)
        playerO      (cons "Configure Player O:" options)

        x            (for [s (range sections)]
                       {:text   (nth playerX s)
                        :anchor (nth text-anchors s)
                        :box    (nth boxes s)
                        :key    (nth keys s)})
        o            (for [s (range sections)]
                       {:text   (nth playerO s)
                        :anchor (nth text-anchors s)
                        :box    (nth boxes s)
                        :key    (nth keys s)})]
    {:x x
     :o o}))

(defn draw [state]
  (let [this  (get-in state [:screens :configure-players])
        boxes (if (nil? (get state :player1)) (:x this) (:o this))]
    (doseq [s (range (count boxes))]
      (let [box (nth boxes s)]
        (when (and (> s 0) (= s (:hovering state)))
          (c/render-rectangle c/highlight-color (:box box)))
        (c/render-text (:x (:anchor box)) (:y (:anchor box)) c/text-size (:text box))))))
