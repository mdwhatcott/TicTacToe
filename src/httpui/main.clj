(ns httpui.main
  (:require
    [hiccup.core :as hiccup]
    [httpui.start :as start])
  (:import
    (jhs Server Service HTTPResponse HTTPRequest)
    (java.util HashMap)))

(defn handle [f]
  (reify Service
    (serve [this javaRequest]
      (let [javaResponse (HTTPResponse.)
            request      {}                                     ;; TODO
            response     (f request)]
        (set! (. javaResponse -StatusCode) 200)
        (doto javaResponse
          (.setBody (str (:body response)))
          (.setHeader "content-type" "text/html"))))))

(defn setup-routes []
  (let [routes (HashMap.)]
    (doto routes
      (.put "/ttt" (handle start/serve-start-page))
      (.put "/ttt/play" (handle nil)))))

(defn -main [& _args]
  (let [routes (setup-routes)
        server (Server. 8081 routes)]
    (.listenAndServe server)))