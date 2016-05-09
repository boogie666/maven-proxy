(ns maven-repo.auth-spec
    (:require [speclj.core :refer :all]
              [maven-repo.auth :refer :all]))

(def options {:users [{"User" "Pass"}]})

(describe "authentification"
    (it "allows authenticated users in"
        (should= true (authenticated? options "User" "Pass")))
    (it "does not allow wrong users in"
        (should-not= true (authenticated? options "NotUser" "NotPass")))
    (it "allows all request if no users are configured"
        (let [no-user-options {:users []}]
            (should= true (authenticated? no-user-options "Whatever" "Whatever")))))
