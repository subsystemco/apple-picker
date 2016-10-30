(defproject apple-picker "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure        "1.8.0"]
                 [org.clojure/clojurescript  "1.9.293"]
                 [org.clojure/core.async     "0.2.395"]
                 [com.cemerick/piggieback    "0.2.1"]
                 [com.squareup.okhttp/okhttp "2.5.0"]
                 [cheshire "5.6.3"]
                 [happy "0.5.2"]]
  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-npm       "0.6.2"]]
  :npm {:dependencies [[source-map-support "0.4.0"]
                       [xhr2 "0.1.3"]]}
  :cljsbuild
  {:builds [{:id "apple-picker"
             :source-paths ["src"]
             :compiler {:output-to     "target/apple-picker/apple_picker.js"
                        :output-dir    "target/apple-picker"
                        :source-map    "target/apple-picker/apple_picker.js.map"
                        :target        :nodejs
                        :language-in   :ecmascript5
                        :optimizations :advanced
                        }}]}
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]})
