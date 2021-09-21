(ns httpui.main
  (:require
    [httpui.start :as start])
  (:import
    (jhs Server Service HTTPResponse)
    (java.util HashMap)))

(defn parse-request [java-request]
  {:form (into {} (. java-request -Form))})

;; TODO: test? (how do you invoke a reified thing in clojure?)
(defn handle [f]
  (reify Service
    (serve [_this java-request]
      (let [java-response (HTTPResponse.)
            request       (parse-request java-request)
            response      (f request)]
        (set! (. java-response -StatusCode) 200)
        (doto java-response
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
