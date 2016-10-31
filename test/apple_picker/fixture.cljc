(ns apple-picker.fixture
  (:require [apple-picker.utils :as utils]))

(def receipts (utils/read-data "test/edn/receipts.edn"))
(def responses (utils/read-data "test/edn/responses.edn"))
