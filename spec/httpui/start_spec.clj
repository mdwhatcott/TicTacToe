(ns httpui.start-spec
  (:require
    [clojure.string :refer [trim replace starts-with?]]
    [speclj.core :refer :all]
    [httpui.start :refer :all]
    [hiccup.core :as hiccup])
  (:import (java.net URI)))

(describe "Opening Screen"
  (it "provides a representation of an HTTP response structure"
    (let [expectedTags [:html
                        [:head]
                        [:body
                         [:h1 "Welcome to Tic-Tac-Toe"]
                         [:form {:method "POST"
                                 :action (URI. "/ttt/play")}
                          [:label {:for "grid-width"} "Enter Grid Width:"]
                          [:input {:type  "number"
                                   :name  "grid-width"
                                   :id    "grid-width"
                                   :value 3}]
                          [:input {:type  "submit"
                                   :value "Play"}]]]]
          renderedTags (hiccup/html expectedTags)
          response     (serve-start-page nil)]
      (should= {:status 200
                :headers {:content-type "text/html"}
                :body renderedTags} response)

      (let [lines
            ["<html>                                                                     "
             "  <head>                                                                   "
             "  </head>                                                                  "
             "  <body>                                                                   "
             "    <h1>                                                                   "
             "      Welcome to Tic-Tac-Toe                                               "
             "    </h1>                                                                  "
             "    <form action='/ttt/play' method='POST'>                                "
             "      <label for='grid-width'>Enter Grid Width:</label>                    "
             "      <input id='grid-width' name='grid-width' type='number' value='3' />  "
             "      <input type='submit' value='Play' />                                 "
             "    </form>                                                                "
             "  </body>                                                                  "
             "</html>                                                                    "]]
        (as-> lines $
              (map trim $)
              (apply str $)
              (replace $ "'" "\"")
              (should= $ renderedTags)))))

  )
