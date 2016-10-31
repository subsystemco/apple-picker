(ns apple-picker.core-test
  (:require [cljs.test :refer-macros [deftest is async testing]]
            [cljs.core.async :refer [<!]]
            [apple-picker.core :refer [verify-receipt]]
            [apple-picker.fixture :as fixture])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(deftest receipt-verification-works
  (testing "a receipt is validated with sandbox if code returned"
    (async done
     (go
       (let [receipt (:sandbox-expired fixture/receipts)
             response (<! (verify-receipt (:receipt-data receipt) (:password receipt)))]
         (is (= (get-in response [:body :receipt :receipt_type]) "ProductionSandbox")))
       (done)))))
