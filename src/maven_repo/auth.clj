(ns maven-repo.auth)

(defn contains-value? [xs x]
    (boolean (some #(= x %) xs)))

(defn authenticated? [{users :users} user pass]
    (if (empty? users)
        true
        (contains-value? users {user pass})))
