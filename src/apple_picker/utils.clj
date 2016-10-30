(ns apple-picker.utils)

(defn read-data
  [path]
  (read-string (slurp path)))
