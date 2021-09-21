(ns httpui.play
  (:require
    [clojure.string :as string]))

(defn parse-numeric-list [input]
  (->> (string/split input #"%20")
       (map #(Integer/parseInt %))))

(defn parse-form [input]
  (cond (nil? input) nil
        (empty? input) nil
        :else
        (let [fields     (string/split input #"&")
              key-values (map #(string/split % #"=") fields)
              keywords   (map #(keyword (first %)) key-values)
              values     (map second key-values)
              bag        (zipmap keywords values)]
          (-> bag
              (update :grid-width #(Integer/parseInt %))
              (update :move #(Integer/parseInt %))
              (update :moves parse-numeric-list)))))
