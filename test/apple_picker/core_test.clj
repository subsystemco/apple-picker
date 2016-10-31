(ns apple-picker.core-test
  (:require [clojure.test :refer :all]
            [apple-picker.core :refer :all]
            [apple-picker.http :as http]
            [apple-picker.fixture :as fixture]
            [clojure.core.async :refer [<!!]]))

(deftest receipt-verification-works
  (testing "a valid receipt is correctly returned"
    (let [receipt (:sandbox-expired fixture/receipts)
          response (<!! (verify-receipt (:receipt-data receipt) (:password receipt)))]
      (is (= (get-in response [:body :receipt :receipt_type]) "ProductionSandbox"))))

  (testing "a receipt is validated with sandbox if code returned"
    (let [receipt (:sandbox-expired fixture/receipts)
          response (<!! (verify-receipt (:receipt-data receipt) (:password receipt)))]
      (is (= (get-in response [:body :receipt :receipt_type]) "ProductionSandbox"))))

  (testing "an invalid receipt is correctly returned"))

(defn scratch []
  (let [receipt (:sandbox-expired fixture/receipts)]
    (<!! (verify-receipt (:receipt-data receipt) (:password receipt))))

(let [receipt (:sandbox-expired fixture/receipts)]
  (<!! (http/post "https://buy.itunes.apple.com/verifyReceipt" receipt)))
  )
