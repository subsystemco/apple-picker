(ns apple-picker.core-test
  (:require [apple-picker.core :refer [verify-receipt]]
            [apple-picker.http :as http]
            [apple-picker.fixture :as fixture]
            [apple-receipt.record :as record]
            [apple-receipt.status-code :as status-code]
            [two-headed-boy.core :as utils]
            #?@(:clj  [[clojure.test :refer :all]
                       [clojure.core.async :refer [go <!! <! timeout alts!]]]
                :cljs [[cljs.test :refer-macros [deftest async is testing]]
                       [cljs.core.async :refer [<! timeout alts! take!]]]))
  #?(:cljs (:require-macros [cljs.core.async.macros :refer [go]])))

;; via http://stackoverflow.com/a/30781278

(defn test-within
  "Asserts that ch does not close or produce a value within ms. Returns a
  channel from which the value can be taken."
  [ms ch]
  (go (let [t (timeout ms)
            [v ch] (alts! [ch t])]
        (is (not= ch t)
            (str "Test should have finished within " ms "ms."))
        v)))

(def default-timeout 2000)

(deftest receipt-verification-works
  (testing "a valid production receipt is correctly returned"
    (utils/test-async
     (test-within default-timeout
      (go (let [receipt (:production-empty fixture/receipts)
                response (<! (verify-receipt (:receipt-data receipt) (:password receipt)))]
            (is (record? response))
            (is (= "Production" (get-in response [:receipt :receipt_type]))))))))

  (testing "a receipt is validated with sandbox if a 21007 status code is returned"
    (utils/test-async
     (test-within default-timeout
      (go (let [receipt (:sandbox-expired fixture/receipts)
                response (<! (verify-receipt (:receipt-data receipt) (:password receipt)))]
            (is (record? response))
            (is (= "ProductionSandbox" (get-in response [:receipt :receipt_type]))))))))

  (testing "an invalid receipt is correctly returned"
    (utils/test-async
     (test-within default-timeout
      (go (let [response (<! (verify-receipt "foo" "bar"))]
            (is (record? response))
            (is (= status-code/data-malformed (:status response)))))))))
