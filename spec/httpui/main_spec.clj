(ns httpui.main-spec
  (:require
    [speclj.core :refer :all]
    [httpui.main :refer :all])
  (:import (jhs HTTPRequest)))

(defn build-java-request []
  (let [java-request (HTTPRequest.)]
    (doto java-request
      (.addForm "repeated" "a")
      (.addForm "repeated" "b")
      (.addForm "grid-width" "4")
      (.addForm "x-player" "human")
      (.addForm "o-player" "human")
      (.addForm "moves" "0 1 2")
      (.addForm "move" "3"))))

(describe "Request Form Conversion"
  (it "converts the java structure into a clojure map"
    (let [java-request (build-java-request)
          request      (parse-request java-request)]
      (should= {"repeated"   ["a" "b"]
                "x-player"   ["human"]
                "o-player"   ["human"]
                "moves"      ["0 1 2"]
                "move"       ["3"]
                "grid-width" ["4"]} (:form request)))))
