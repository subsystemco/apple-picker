(ns apple-picker.core
  (:require [apple-receipt.record :as record]
            [apple-receipt.status-code :as status-code]
            [apple-picker.http :as http]
            #?@(:clj  [[clojure.core.async :as async :refer [go <!]]]
                :cljs [[cljs.core.async :as async :refer [<!]]]))
  #?(:cljs (:require-macros [cljs.core.async.macros :refer [go]])))

(def config
  {:prod "https://buy.itunes.apple.com/verifyReceipt"
   :sandbox "https://sandbox.itunes.apple.com/verifyReceipt"})

;; THE REQUEST
;; https://developer.apple.com/library/mac/documentation/NetworkingInternet/Conceptual/StoreKitGuide/Chapters/AppReview.html#//apple_ref/doc/uid/TP40008267-CH10-SW1
;; When validating receipts on your server, your server needs to be able to handle a production-signed app getting its receipts from Apple’s test environment. The recommended approach is for your production server to always validate receipts against the production App Store first. If validation fails with the error code “Sandbox receipt used in production”, validate against the test environment instead.

;; https://developer.apple.com/library/ios/technotes/tn2413/_index.html#//apple_ref/doc/uid/DTS40016228-CH1-RECEIPTURL
;; Always verify your receipt first with the production URL; proceed to verify with the sandbox URL if you receive a 21007 status code. Following this approach ensures that you do not have to switch between URLs while your application is being tested or reviewed in the sandbox or is live in the App Store.
(defn- query-prod-or-sand
  [body]
  (go
    (let [resp (<! (http/post (:prod config) body))]
      (if (= (get-in resp [:body :status]) status-code/from-test-env)
        (<! (http/post (:sandbox config) body))
        resp))))

(defn verify-receipt
  ([data] (verify-receipt data nil))
  ([data pass]
   (go
     (let [body {:receipt-data data
                 :password pass}
           resp (<! (query-prod-or-sand body))]
       (record/api-json->Response (:body resp))))))
