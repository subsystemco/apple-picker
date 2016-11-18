(ns apple-picker.http
  (:require [happy.core :as h]
            [apple-picker.representor :as rep]
            #?@(:clj  [[clojure.core.async :as async]
                       [happy.client.okhttp :as hc]]
                :cljs [[cljs.core.async :as async]
                       [cljs.nodejs :as nodejs]
                       [happy.client.xmlhttprequest :as hc]])))

;; Backfill since Happy is browser focused
#?(:cljs (set! js/XMLHttpRequest (nodejs/require "xhr2")))

(h/merge-options! {:client (hc/create)
                   ; Apple doesn't send back a Response-Type, so add it here
                   :override-response-mime-type "application/json"})
(rep/merge-representors! true)

(defn request
  [options]
  (let [channel (async/chan 1)]
    (h/send! options
             {:handler (fn [response]
                         (async/put! channel response (fn [_] (async/close! channel))))})
    channel))

(defn post
  [url data]
  (request {:url url
            :method "POST"
            :headers {"content-type" "application/json"}
            :body data}))
