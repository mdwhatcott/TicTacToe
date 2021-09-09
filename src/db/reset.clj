(ns db.reset
  (:require [db.datomic :as db]))

(defn -main []
  (db/reset-db db/prod-uri db/schema))