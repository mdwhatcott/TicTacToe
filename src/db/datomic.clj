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
  '[:find ?game-name ?grid-dimensions ?x-player ?o-player
    :where
    [?g :game/name ?game-name]
    [?g :game/grid-dimensions ?grid-dimensions]
    [?g :game/x-player ?x-player]
    [?g :game/o-player ?o-player]
    [?g :game/over false]])

(defn get-unfinished-game [conn]
  (first
    (for [game (d/q unfinished-games-query (d/db conn))]
      {:name     (nth game 0)
       :grid     (nth game 1)
       :x-player (keyword (subs (nth game 2) 1))
       :o-player (keyword (subs (nth game 3) 1))})))

(defn establish-new-game [conn game-name grid-dimensions x-player o-player]
  (let [attributes {:db/id                "new-game"
                    :game/name            game-name
                    :game/grid-dimensions grid-dimensions
                    :game/x-player        (str x-player)
                    :game/o-player        (str o-player)
                    :game/over            false}]
    @(d/transact conn [attributes])))

(def get-moves-query
  '[:find ?move-sequence ?move-location
    :in $ ?game-name
    :where
    [?g :game/name ?game-name]
    [?g :game/moves ?m]
    [?m :move/sequence ?move-sequence]
    [?m :move/location ?move-location]])

(defn get-moves [conn game-name]
  (let [result (d/q get-moves-query (d/db conn) game-name)]
    (->> result                                             ; #{[1 1] [0 2]}
         (sort-by first)                                    ; ([0 2] [1 1])
         (map second))))                                    ; [2 1]


(defn associate-move [conn game-name sequence spot]
  (let [move-id      "next-move"]
    @(d/transact
       conn [{:db/id         move-id
              :move/sequence sequence}
             {:db/id         move-id
              :move/location spot}
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
