(ns apple-picker.core-test
  (:require [clojure.test :refer :all]
            [apple-picker.core :refer :all]
            [apple-picker.http :as http]
            [apple-picker.fixture :as fixture]
            [apple-receipt.record :as record]
            [apple-receipt.status-code :as status-code]
            [clojure.core.async :refer [<!!]]))

(deftest receipt-verification-works
  (testing "a valid production receipt is correctly returned"
    (let [receipt (:production-empty fixture/receipts)
          response (<!! (verify-receipt (:receipt-data receipt) (:password receipt)))]
      (is (record? response))
      (is (= "Production" (get-in response [:receipt :receipt_type])))))

  (testing "a receipt is validated with sandbox if code returned"
    (let [receipt (:sandbox-expired fixture/receipts)
          response (<!! (verify-receipt (:receipt-data receipt) (:password receipt)))]
      (is (record? response))
      (is (= "ProductionSandbox" (get-in response [:receipt :receipt_type])))))

  (testing "an invalid receipt is correctly returned"
    (let [response (<!! (verify-receipt "foo" "bar"))]
      (is (record? response))
      (is (= status-code/data-malformed (:status response))))))
