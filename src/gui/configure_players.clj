(ns gui.configure-players
  (:require
    [gui.common :as c]
    [gui.render :as r]
    [ttt.ai :as ai]))

;; TODO: test suite
(defn update_ [state]
  (let [mx        (get-in state [:mouse :x])
        my        (get-in state [:mouse :y])
        clicked?  (get-in state [:mouse :clicked?])
        boxes     (get-in state [:screens :configure-players])
        hovering? (map #(c/bounded? [mx my] (:box %)) boxes)
        indexed   (map-indexed vector hovering?)
        element   (ffirst (drop-while #(not (second %)) indexed))]
    (let [player (if (nil? (:player1 state)) :player1 :player2)]
      (if (and clicked? (some? element) (> element 0))
        (assoc state player (:value (nth boxes element))
                     :transition? (= player :player2))
        (assoc state :hovering element)))))

(defn calculate-anchors [screen-width]
  (let [sections 5
        height   (/ screen-width sections)
        boxes    (for [s (range sections)]
                   [[0 (* height s)]
                    [screen-width (+ height (* height s))]])
        anchors  (for [s (range sections)]
                   {:x (/ screen-width 10)
                    :y (+ (/ height 2) (* height s))})
        texts    ["Configure Player %s:"
                  "     Human"
                  "     Computer (easy)"
                  "     Computer (medium)"
                  "     Computer (hard)"]
        values   [nil :human ai/easy ai/medium ai/hard]]

    (for [i (range (count boxes))]
      {:text   (nth texts i)
       :anchor (nth anchors i)
       :box    (nth boxes i)
       :value  (nth values i)})))

(defn draw [state]
  (let [boxes (get-in state [:screens :configure-players])]
    (doseq [s (range (count boxes))]
      (let [box     (nth boxes s)
            player1 (:player1 state)
            x       (:x (:anchor box))
            y       (:y (:anchor box))
            text    (format (:text box) (if (nil? player1) "X" "O"))]
        (when (and (> s 0) (= s (:hovering state)))
          (r/render-rectangle r/hovering-color (:box box)))
        (r/render-text x y text)))))
