(ns maven-repo.handler
  (:require [compojure.core :refer :all]
            [clojure.string :as str]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.util.response :as response]))


(defn- repo-route [endpoint repo-path proxy]
    (GET (str endpoint "*") {{file-path :*} :route-params}
        (let [read-path (str proxy file-path)
              write-path (str repo-path file-path)]
            (io/make-parents (io/as-file write-path))
            (try
                (with-open [in (io/input-stream read-path)
                            out (io/output-stream write-path)]
                    (io/copy in out))
                    (response/file-response write-path)
                (catch java.lang.Exception e
                    nil)))))

(defn repo-routes
    "Defines the routes for the maven repository.
     If file is not available in local repo it is fetched
     from one of the defined proxies.

     Repositories are tried in order of definition,
     the file is downloaded from the first avaiable location.

     If requested file is not found anywhere it returns nil."
    [{repo-location :repo-location proxies :proxies endpoint :endpoint}]
    (let [local-repo (route/files endpoint {:root repo-location})
          proxied-repos (map #(repo-route endpoint repo-location %) proxies)
          all-repos (conj proxied-repos local-repo)]
        (apply routes
            all-repos)))

(defn deployment-routes
    "Defines the routes for deploying artifacts to the maven repository.
    This only deployes to this repository, proxies are not touched by this."
    [{repo-location :repo-location endpoint :endpoint}]
    (routes
        (PUT (str endpoint "*") {{file-path :*} :route-params body :body}
            (let [write-path (str repo-location file-path)]
                (io/make-parents write-path)
                (io/copy body (io/as-file write-path))
                (response/created write-path)))))
