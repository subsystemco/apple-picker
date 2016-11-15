(ns apple-picker.fixture
  (:require [two-headed-boy.core :as utils]))

(def receipts (utils/read-file "test/edn/receipts.edn"))
(def responses (utils/read-file "test/edn/responses.edn"))
