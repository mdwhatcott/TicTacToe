(ns httpui.start
  (:require
    [hiccup.form :as form]
    [hiccup.core :as hiccup]))

(defn start-tags []
  [:html
   [:head]
   [:body
    [:h1 "Welcome to Tic-Tac-Toe"]
    (form/form-to
      [:post "/ttt/play"]
      (form/label "grid-width" "Enter Grid Width:")
      (form/text-field {:type "number"} "grid-width" 3)
      (form/submit-button "Play"))]])

(defn serve-start-page [_request]
  {:status  200
   :headers {:content-type "text/html"}
   :body    (hiccup/html (start-tags))})
