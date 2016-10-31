(ns apple-picker.utils
  (:require [cljs.nodejs :as nodejs]
            [cljs.reader :refer [read-string]]))

(defn read-data
  [path]
  (-> (nodejs/require "fs")
      (.readFileSync path "UTF-8")
      read-string))
