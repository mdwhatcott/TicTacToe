(ns httpui.play
  (:require
    [clojure.string :as string]
    [ttt.restore :as restore]
    [tui.grid :as tui]))

(defn- get-number [form key]
  (Integer/parseInt (or (first (form key)) "0")))

(defn- parse-moves [form]
  (let [move (inc (get-number form "move"))]
    (as-> (form "moves") $
          (first $)
          (string/split $ #"\s")
          (mapv #(Integer/parseInt %) $)
          (conj $ move))))

(defn form->game-state [form]
  (println "hi from form->game-state: " form (get-number form "grid-width") (parse-moves form))
  {:x-player   :human
   :o-player   :human
   :grid-width (get-number form "grid-width")
   :moves      (parse-moves form)})

(defn- derive-mark [grid]
  (if (:game-over? grid)
    nil
    (if (even? (count (:moves grid)))
      :X
      :O)))

(defn play [before]
  (let [restored  (restore/restore-game before)
        grid      (-> restored :game-grid)
        rendering (tui/render-grid grid)]
    (assoc before :mark (derive-mark grid)
                  :rendered-grid rendering)))

(defn- render-ux [state]
  (let [moves      (string/join " " (:moves state))
        mark       (:mark state)
        grid-width (str (:grid-width state))]
    (if (nil? mark)
      [:a {:href "/ttt"} "Play again?"]
      [:form {:method "post" :action "/ttt/play"}
       [:input {:type "hidden" :name "moves" :value moves}]
       [:input {:type "hidden" :name "grid-width" :value grid-width}]
       [:label {:for "guess"} (format "Player %s: where would you like to move:" (name mark))]
       [:input {:type "number" :name "move" :required "true"}]
       [:input {:type "submit" :value "Submit"}]])))

(defn render-page [state]
  (let [rendered-grid (:rendered-grid state)]
    [:html
     [:head]
     [:body
      [:h2 "TicTacToe"]
      [:pre [:code rendered-grid]]
      (render-ux state)]]))

(defn serve-play-page [request]
  (println "Hello, from serve-play-page")
  (let [r (:form request)
        ;r (form->game-state (:form request))
        ;r (play before)
        ;r (render-page after)
        ;r (hiccup/html hiccup-html)
        ]
    (println "goodbye from serve-play-page" r)
    {:status 200
     :body   "hi"}))
