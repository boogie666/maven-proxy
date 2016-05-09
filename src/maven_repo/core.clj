(ns maven-repo.core
    (:require [maven-repo.handler :as handler]
              [maven-repo.auth :as auth]
              [ring.adapter.jetty :as jetty]
              [compojure.core :refer [routes]]
              [compojure.route :refer [not-found]]
              [clojure.tools.cli :refer [cli]]
              [clojure.edn :as edn]
              [clojure.java.io :refer [as-file]]
              [ring.middleware.basic-authentication :refer [wrap-basic-authentication]])
    (:gen-class))

(def options
    {:repo-location "test-repo"
     :endpoint "/maven2"
     :users [{"boogie" "hello"}]
     :proxies ["https://repo.maven.apache.org/maven2"]})


(def app
    (-> (routes (handler/repo-routes options)
                (handler/deployment-routes options)
                (not-found "404 Not found!"))
        (wrap-basic-authentication (partial auth/authenticated? options))))

(defn -main [& args])
