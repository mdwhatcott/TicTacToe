(ns httpui.play
    (:require
      [hiccup.core :as hiccup]))

(defn serve-play-page [_request]
      (println _request)
  {:status  200
   :headers {:content-type "text/html"}
   :body    (hiccup/html [:html])})