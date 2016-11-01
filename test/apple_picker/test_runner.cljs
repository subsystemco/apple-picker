(ns apple-picker.test-runner
 (:require [doo.runner :refer-macros [doo-tests]]
           [apple-picker.core-test]
           [cljs.nodejs :as nodejs]))

(try
  (.install (nodejs/require "source-map-support"))
  (catch :default _))

(doo-tests
 'apple-picker.core-test)
