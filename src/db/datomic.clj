(ns db.datomic
  (:require
    [datomic.api :as d]))

(def test-uri "datomic:mem://ttt-test-db")
(def prod-uri "datomic:free://localhost:4334/ttt-db")
(def schema "resources/datomic/schema.edn")
(def conn nil)


(def game-id-query
  '[:find ?eid
    :in $ ?game-start
    :where [?eid :game/name ?game-start]])

(defn find-game-id [game-start]
  (ffirst (d/q game-id-query (d/db conn) game-start)))


(def get-moves-query
  '[:find ?move-sequence ?move-location
    :in $ ?game-name
    :where
    [?g :game/name ?game-name]
    [?g :game/moves ?m]
    [?m :move/sequence ?move-sequence]
    [?m :move/location ?move-location]])

(defn get-moves [game-name]
  (->> (d/q get-moves-query (d/db conn) game-name)          ; #{[1 1] [0 2]}
       (sort-by first)                                      ; ([0 2] [1 1])
       (map second)))                                       ; [2 1]


(def unfinished-games-query
  '[:find ?game-name ?grid-dimensions ?x-player ?o-player
    :where
    [?g :game/name ?game-name]
    [?g :game/grid-dimensions ?grid-dimensions]
    [?g :game/x-player ?x-player]
    [?g :game/o-player ?o-player]
    [?g :game/over false]])

(defn get-unfinished-game []
  (first
    (for [game (d/q unfinished-games-query (d/db conn))]
      {:name       (nth game 0)
       :grid-width (nth game 1)
       :x-player   (keyword (subs (nth game 2) 1))
       :o-player   (keyword (subs (nth game 3) 1))
       :moves      (get-moves (nth game 0))})))


(defn establish-new-game [game-name grid-width x-player o-player]
  @(d/transact conn [{:db/id                "new-game"
                      :game/name            game-name
                      :game/grid-dimensions grid-width
                      :game/x-player        (str x-player)
                      :game/o-player        (str o-player)
                      :game/over            false}]))

(defn associate-move [game-name sequence spot]
  (let [move-id "next-move"]
    @(d/transact
       conn [{:db/id move-id :move/sequence sequence}
             {:db/id move-id :move/location spot}
             {:db/id (find-game-id game-name) :game/moves move-id}])))

(defn conclude-game [game-name]
  @(d/transact
     conn [{:db/id (find-game-id game-name) :game/over true}]))


(defn reset-db [uri schema-path]
  (d/delete-database uri)
  (d/create-database uri)
  (let [conn (d/connect uri)]
    (d/transact conn (load-file schema-path))
    conn))
