(ns db.reporting
  (:require
    [db.datomic :as db]
    [datomic.api :as d]))

(def all-games-query
  '{:find  [?game-name
            ?grid-width
            ?x-player
            ?o-player
            ?game-over?]
    :where [[?g :game/name ?game-name]
            [?g :game/grid-width ?grid-width]
            [?g :game/x-player ?x-player]
            [?g :game/o-player ?o-player]
            [?g :game/over ?game-over?]]})

(defn get-all-games []
  (for [game (d/q all-games-query (d/db db/conn))]
    (let [name (nth game 0)]
      {:name       name
       :grid-width (nth game 1)
       :x-player   (keyword (subs (nth game 2) 1))
       :o-player   (keyword (subs (nth game 3) 1))
       :moves      (db/get-moves name)
       :game-over? (nth game 4)})))

(defn -main []
  (with-redefs [db/conn (d/connect db/prod-uri)]
    (clojure.pprint/pprint (get-all-games))
    (d/shutdown true)))