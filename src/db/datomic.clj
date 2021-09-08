(ns db.datomic
  (:require
    [datomic.api :as d]
    [clojure.string :as string])
  (:import (java.util Date)))

(defn now []
  (.toString (Date.)))

(defn find-game-id [conn game-start]
  (ffirst (d/q '[:find ?eid
                 :in $ ?game-start
                 :where [?eid :game/name ?game-start]]
               (d/db conn)
               game-start)))

(def unfinished-games-query
  '[:find ?game-name ?grid-dimensions ?x-human? ?o-human?
    :where
    [?g :game/name ?game-name]
    [?g :game/grid-dimensions ?grid-dimensions]
    [?g :game/x-human? ?x-human?]
    [?g :game/o-human? ?o-human?]
    [?g :game/over false]])

(defn get-unfinished-game [conn]
  (first
    (for [game (d/q unfinished-games-query (d/db conn))]
      {:name     (nth game 0)
       :grid     (nth game 1)
       :x-human? (nth game 2)
       :o-human? (nth game 3)})))

(defn establish-new-game [conn game-start grid-dimensions x-human? o-human?]
  (let [attributes {:db/id                "new-game"
                    :game/name            game-start
                    :game/grid-dimensions grid-dimensions
                    :game/x-human?        x-human?
                    :game/o-human?        o-human?
                    :game/over            false}]
    @(d/transact conn [attributes])))

(def get-moves-query
  '[:find ?move-at
    :in $ ?game-name
    :where
    [?g :game/name ?game-name]
    [?g :game/moves ?m]
    [?m :move/details ?move-at]])

(defn get-moves [conn game-name]
  (let [result (d/q get-moves-query (d/db conn) game-name)]
    (->> result                                             ; #{["1-1"] ["0-2"]}
         (map vec)                                          ; [["1-1"] ["0-2"]]
         flatten                                            ; ["1-1" "0-2"]
         (map #(string/split % #"-"))                       ; [["1" "1"] ["0" "2"]]
         (sort-by first)                                    ; [["0" "2"] ["1" "1"]]
         (map second)                                       ; ["2" "1"]
         (map #(Integer/parseInt %)))))                     ; [2 1]

(defn associate-move [conn game-name sequence spot]
  (let [move-id      "next-move"
        move-details (str sequence "-" spot)]
    @(d/transact
       conn [{:db/id        move-id
              :move/details move-details}
             {:db/id      (find-game-id conn game-name)
              :game/moves move-id}])))

(defn conclude-game [conn game-name]
  @(d/transact
     conn [{:db/id     (find-game-id conn game-name)
            :game/over true}]))

(defn reset-db [uri schema-path]
  (d/delete-database uri)
  (d/create-database uri)
  (let [conn   (d/connect uri)
        schema (load-file schema-path)]
    (d/transact conn schema)
    conn))
