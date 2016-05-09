(ns maven-repo.spec-utils
    (:require [clojure.java.io :as io]))



(defn path [& args]
    (clojure.string/join java.io.File/separator args))


(defn create-files [files]
    (doseq [f files]
        (io/make-parents f)
        (spit f f)))

(defn delete-directory [path]
    (let [file (io/as-file path)]
        (if (.isDirectory file)
            (do
                (doseq [f (.listFiles file)]
                    (delete-directory (.getPath f)))
                (.delete file))
            (.delete file))))

(defn cleanup-files [files]
    (doseq [f files]
        (delete-directory f)))
