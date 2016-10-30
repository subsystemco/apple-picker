(ns apple-picker.utils)

(defn read-data
  [path]
  (-> (nodejs/require "fs")
      (.readFileSync path "UTF-8")
      read-string))
