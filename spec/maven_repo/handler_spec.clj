(ns maven-repo.handler-spec
  (:require [speclj.core :refer :all]
            [ring.mock.request :as mock]
            [clojure.java.io :refer [as-file] :as io]
            [maven-repo.handler :refer :all]
            [maven-repo.spec-utils :refer :all]))

(def options
    {:repo-location "spec/test-repo/"
     :endpoint "/maven2"
     :proxies [(path "spec" "proxy-repo"), (path "spec" "proxy-repo2")]})

(def routes (repo-routes options))


(describe (str (:endpoint options) " route handler")

    (before
        (create-files [(path (:repo-location options) "file")
                       (path "spec" "proxy-repo" "module" "module-file")
                       (path "spec" "proxy-repo2" "module2" "module-file2")]))
    (after
        (cleanup-files [(:repo-location options)
                        (path "spec" "proxy-repo")
                        (path "spec" "proxy-repo2")]))


    (it "finds a file in repo at given location"
        (let [request (mock/request :get "/maven2/file")]
            (should=
                (as-file (path "spec" "test-repo" "file"))
                (:body (routes request)))
            (should= 200 (:status (routes request)))))
    (it "goes to the first proxy-location and returns files if they are not in repo"
        (let [request (mock/request :get "/maven2/module/module-file")]
            (should=
                (as-file (path "spec" "test-repo" "module" "module-file"))
                (:body (routes request)))))
    (it "goes to any proxy-location and returns files if they are not in repo"
        (let [request (mock/request :get "/maven2/module2/module-file2")]
            (should=
                (as-file (path "spec" "test-repo" "module2" "module-file2"))
                (:body (routes request)))))
    (it "returns nil if file can't be found anywhere"
        (let [request (mock/request :get "/maven2/non-existent-module/non-existent-file")]
                (should= nil (routes request))))
    (it "does not create folders on 404"
        (let [request (mock/request :get "/maven2/a/b/c.txt")]
            (routes request))
            (should= false (.exists (io/file (path (:repo-location options) "a" "b"))))))

(def deploy (deployment-routes options))
(describe (str (:endpoint options) " route handler (deployment)")
    (after (cleanup-files [(path (:repo-location options))]))
    (it "writes a file to given location"
        (let [request (mock/request :put "/maven2/com/test/file" "file-contents")]
            (deploy request)
            (should=
                (slurp (as-file (path "spec" "test-repo" "com" "test" "file")))
                "file-contents")))
    (it "returns 201 for successful deployment"
        (let [request (mock/request :put "/maven2/com/test/file" "file-contents")]
            (should= 201 (:status (deploy request))))))
