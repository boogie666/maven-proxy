(defproject maven-repo "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.5.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring-basic-authentication "1.0.5"]
                 [org.clojure/tools.cli "0.2.4"]
                 [org.clojure/tools.logging "0.3.1"]
                 [digest "1.4.4"]]

  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler maven-repo.core/app
         :main maven-repo.core}
  :profiles
  {:dev {
    :test-paths ["spec"]
    :plugins [[speclj "3.3.2"]]
    :dependencies [[javax.servlet/servlet-api "2.5"]
                   [ring/ring-mock "0.3.0"]
                   [speclj "3.3.2"]]}})
