(ns apple-picker.core-test
  (:require [cljs.test :refer-macros [deftest is async testing]]
            [cljs.core.async :refer [<!]]
            [apple-picker.core :refer [verify-receipt]]
            [apple-picker.fixture :as fixture]
            [apple-receipt.record :as record]
            [apple-receipt.status-code :as status-code])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(deftest receipt-verification-works
  (testing "a valid production receipt is correctly returned"
    (async done
     (go
       (let [receipt (:production-empty fixture/receipts)
             response (<! (verify-receipt (:receipt-data receipt) (:password receipt)))]
         (is (record? response))
         (is (= "Production" (get-in response [:receipt :receipt_type]))))
       (done))))

  (testing "a receipt is validated with sandbox if a 21007 status code is returned"
    (async done
     (go
       (let [receipt (:sandbox-expired fixture/receipts)
             response (<! (verify-receipt (:receipt-data receipt) (:password receipt)))]
         (is (record? response))
         (is (= "ProductionSandbox" (get-in response [:receipt :receipt_type]))))
       (done))))

  (testing "an invalid receipt is correctly returned"
    (async done
     (go
       (let [response (<! (verify-receipt "foo" "bar"))]
         (is (record? response))
         (is (= status-code/data-malformed (:status response))))
       (done)))))
