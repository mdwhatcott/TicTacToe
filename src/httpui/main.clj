(ns httpui.main
  (:require
    [hiccup.core :as hiccup]
    [httpui.start :as start])
  (:import
    (jhs Server Service HTTPResponse HTTPRequest)
    (java.util HashMap)))

(def hello
  (reify Service
    (serve [this request]
      (println request)
      (let [response (HTTPResponse.)]
        (set! (. response -StatusCode) 200)
        (doto response
          (.setBody "<html><head></head><body><h1>Hello, world!</h1></body></html>")
          (.setHeader "content-type" "text/html"))))))

(def start
  (reify Service
    (serve [this reader]
      (let [writer   (HTTPResponse.)
            request  {}                                     ;; TODO
            response (start/serve-start-page reader)]
        (doto writer
          (.setBody (str (:body response)))
          (doseq [[k v] (:headers response)]
                 (.setHeader k v)))))))

(defn setup-routes []
  (let [routes (HashMap.)]
    (doto routes
      (.put "/" hello)
      (.put "/ttt" start)
      (.put "/ttt/play" nil))))

(defn -main [& _args]
  (let [routes (setup-routes)
        server (Server. 8081 routes)]
    (.listenAndServe server)))