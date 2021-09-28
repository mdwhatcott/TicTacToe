(defproject ttt "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main gui.main
  :datomic {:schemas ["resources/datomic" ["schema.edn"]]}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.datomic/datomic-free "0.9.5697"]
                 [quil "3.1.0"]
                 [ttt-grid "0.1.0-SNAPSHOT"]]
  :profiles {:dev {:dependencies [[speclj "3.3.2"]]
                   :datomic {:config "resources/datomic/free-transactor-template.properties"
                             :db-uri "datomic:free://localhost:4334/ttt-db"}}}
  :plugins [[speclj "3.3.2"]]
  :test-paths ["spec"])
