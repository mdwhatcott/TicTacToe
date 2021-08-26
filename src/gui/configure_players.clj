(ns gui.configure-players
  (:require [quil.core :as q]
            [gui.common :as c]
            [ttt.ai :as ai]))

(defn update [state]
  (let [mx        (q/mouse-x)
        my        (q/mouse-y)
        clicked?  (get-in state [:mouse :clicked?])
        boxes     (get-in state [:screens :configure-players])
        hovering? (map #(c/bounded? [mx my] (:box %)) boxes)
        indexed   (map-indexed vector hovering?)
        element   (ffirst (drop-while #(not (second %)) indexed))]
    (let [player (if (nil? (:player1 state)) :player1 :player2)]
      (if (and clicked? (some? element) (> element 0))
        (assoc state player (:value (nth boxes element))
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
                        :y (+ (/ height 2) (* height s))})]
    [{:text   "Configure Player %s:"
      :anchor (nth text-anchors 0)
      :box    (nth boxes 0)
      :value  nil}

     {:text   "     Human"
      :anchor (nth text-anchors 1)
      :box    (nth boxes 1)
      :value  :human}

     {:text   "     Computer (easy)"
      :anchor (nth text-anchors 2)
      :box    (nth boxes 2)
      :value  ai/easy}

     {:text   "     Computer (medium)"
      :anchor (nth text-anchors 3)
      :box    (nth boxes 3)
      :value  ai/medium}

     {:text   "     Computer (hard)"
      :anchor (nth text-anchors 4)
      :box    (nth boxes 4)
      :value  ai/hard}]))

(defn draw [state]
  (let [boxes (get-in state [:screens :configure-players])]
    (doseq [s (range (count boxes))]
      (let [box (nth boxes s)]
        (when (and (> s 0) (= s (:hovering state)))
          (c/render-rectangle c/hovering-color (:box box)))
        (c/render-text (:x (:anchor box))
                       (:y (:anchor box))
                       c/text-size
                       (if (zero? s)
                         (format (:text box) (if (nil? (:player1 state)) "X" "O"))
                         (:text box)))))))
