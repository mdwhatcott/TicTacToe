(ns db.reset
  (:require [db.datomic :as db]
            [datomic.api :as d]))

(defn -main []
  (db/reset-db db/prod-uri db/schema)
  (d/shutdown true))