(ns maven-repo.core
    (:require [ring.adapter.jetty :as jetty]
              [compojure.core :refer [routes]]
              [maven-repo.handler :as handler]
              [clojure.tools.cli :refer [cli]]
              [clojure.edn :as edn]
              [clojure.java.io :refer [as-file]]
              [ring.middleware.basic-authentication :refer [wrap-basic-authentication]])
    (:gen-class))

(def options
    {:repo-location "test-repo"
     :endpoint "/maven2"
     :proxies ["https://repo.maven.apache.org/maven2"]})

(defn authenticated? [name pass]
    (and (= name "boogie")
         (= pass "hello")))

(def app
    (-> (routes (handler/repo-routes options)
                (handler/deployment-routes options))
        (wrap-basic-authentication authenticated?)))

(defn -main [& args])
