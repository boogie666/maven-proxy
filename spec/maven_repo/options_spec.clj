(ns maven-repo.options-spec
    (:require [speclj.core :refer :all]
              [maven-repo.options :refer :all]
              [maven-repo.spec-utils :refer :all]))


(def options-file (path "spec" "options.edn"))

(describe "options - defaults"
    (after (cleanup-files [options-file]))
    (it "creates a file with default options"
        (let [defaults {:port 1337 :repo-location "maven2-repo" :endpoint "/maven2" :users [] :proxies ["https://repo.maven.apache.org/maven2"]}]
            (create-default-config! (str options-file))
            (should= defaults (read-string (slurp options-file))))))


(describe "options"
    (after (cleanup-files [options-file]))
    (it "load options from a file"
        (let [expected-options DEFAULTS]
            (spit options-file DEFAULTS)
            (should= expected-options (load-options options-file))))
    (it "throws of configs are missing"
        (spit options-file {:repo-location "some-location" :users [{"admin" "admin"}]})
        (should-throw Exception (load-options options-file)))
    (it "throws of configs are invalid"
        (spit options-file {:repo-location "some-location" :users [{"admin" "admin"}] :endpoint "" :proxies ""})
        (should-throw Exception (load-options options-file))))
