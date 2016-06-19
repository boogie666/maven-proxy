(ns maven-repo.options
    (:require [clojure.set :as sets]))

(def DEFAULTS
    {:repo-location "maven2-repo"
     :endpoint "/maven2"
     :proxies ["https://repo.maven.apache.org/maven2"]
     :port 1337
     :users []})

(defn has-all-configs?
    [options]
    (let [keys-options (set (keys options))
          keys-default (set (keys DEFAULTS))]
        (= keys-options keys-default)))

(defn- valid-config-types?
    [{location :repo-location endpoint :endpoint proxies :proxies users :users}]
    (and (string? location)
         (string? endpoint)
         (vector? proxies)
         (vector? users)))

(defn load-options
    [file-path]
    (let [options (read-string (slurp file-path))]
      (when-not (has-all-configs? options)
          (throw (Exception. "Invalid config")))
      (when-not (valid-config-types? options)
          (throw (Exception. "Invalid config")))
      options))

(defn create-default-config! [path]
    (spit path DEFAULTS))
