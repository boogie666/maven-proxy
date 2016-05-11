(ns maven-repo.core
    (:require [maven-repo.handler :as handler]
              [maven-repo.auth :as auth]
              [ring.adapter.jetty :as jetty]
              [compojure.core :refer [routes]]
              [compojure.route :refer [not-found]]
              [clojure.tools.cli :refer [cli]]
              [clojure.edn :as edn]
              [clojure.java.io :refer [as-file]]
              [maven-repo.options :refer [create-default-config! load-options DEFAULTS]]
              [ring.middleware.basic-authentication :refer [wrap-basic-authentication]])
    (:gen-class))


(defn create-app [options]
    (-> (routes
            (handler/repo-routes options)
            (handler/deployment-routes options)
            (not-found "404 Not found!"))
        (wrap-basic-authentication (partial auth/authenticated? options))))


(def app (create-app DEFAULTS))

(defn print-help [args]
    (println "Simple Maven Proxy")
    (println "Usage:")
    (println "java -jar maven-proxy.jar            Starts maven proxy with default config file 'maven-proxy-config.edn' ")
    (println "      or")
    (println "java -jar maven-proxy.jar <option>   Starts maven proxy with given options")
    (println)
    (println "Options:")
    (println "--config-file <a file>      Starts maven proxy with a given config file (default file is 'maven-proxy-config.edn')")
    (println "--generate-config <a file>  Genertes the default config file at given location (default file is 'maven-proxy-config.edn')")
    (println "--help                      Shows this screen"))

(defn generate-config [args]
    (let [config-path (cond
                        (first args) (first args)
                        :else "maven-proxy-config.edn")]
        (println "Generating default config file at" config-path)
        (create-default-config! config-path)
        (println)
        (println "Run 'java -jar maven-proxy.jar --config-file" config-path "' to use config")))

(defn- print-options [{endpoint :endpoint port :port proxies :proxies users :users repo-location :repo-location}]
    (println "Starting maven-proxy on port" port "with:")
    (println)
    (println "endpoint       " endpoint)
    (println "users          " users)
    (println "proxies        " proxies)
    (println "repo-location  " repo-location))

(defn start-app [args]
    (let [options (load-options (first args))
          handler (create-app options)]
        (print-options options)
        (jetty/run-jetty handler {:port (:port options)})))

(defn handle-args [args]
    (cond
        (= (first args) "--help") (print-help (rest args))
        (= (first args) "--generate-config") (generate-config (rest args))
        (= (first args) "--config-file") (start-app (rest args))
        :else (start-app (list "maven-proxy-config.edn"))))

(defn -main [& args] (handle-args args))
