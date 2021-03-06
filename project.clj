(defproject co.subsystem/apple-picker "0.1.0-SNAPSHOT"
  :description "A Clojure(Script) library for fetching updates from the Apple receipt validation server"
  :url "https://github.com/leppert/apple-picker"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure        "1.8.0"]
                 [org.clojure/clojurescript  "1.9.293"]
                 [org.clojure/core.async     "0.2.395"]
                 [com.cemerick/piggieback    "0.2.1"]
                 [com.squareup.okhttp/okhttp "2.5.0"]
                 [cheshire "5.6.3"]
                 [happy "0.5.2"]
                 [co.subsystem/apple-receipt "0.1.0-SNAPSHOT"]
                 [two-headed-boy "0.1.0"]]
  :plugins [[s3-wagon-private "1.2.0"]
            [lein-cljsbuild "1.1.4"]
            [lein-npm       "0.6.2"]
            [lein-doo       "0.1.7"]]
  :npm {:dependencies [[source-map-support "0.4.6"]
                       [xhr2 "0.1.3"]]}

  :repositories {"snapshots" {:url "s3p://libs.subsystem.co/snapshots"
                              :username :env/aws_libs_access_key
                              :passphrase :env/aws_libs_secret_key}
                 "releases" {:url "s3p://libs.subsystem.co/releases"
                             :username :env/aws_libs_access_key
                             :passphrase :env/aws_libs_secret_key}}

  :doo {:build "test"
        :alias {:default [:node]}}

  :cljsbuild
  {:builds {:production {:source-paths ["src"]
                         :compiler {:output-to     "target/apple-picker/apple_picker.js"
                                    :output-dir    "target/apple-picker"
                                    :source-map    "target/apple-picker/apple_picker.js.map"
                                    :target        :nodejs
                                    :language-in   :ecmascript5
                                    :optimizations :advanced
                                    }}
            :test {:source-paths ["src" "test"]
                   :compiler {:output-to     "target/apple-picker-test/apple_picker.js"
                              :output-dir    "target/apple-picker-test"
                              :target        :nodejs
                              :language-in   :ecmascript5
                              :optimizations :none
                              :main          apple-picker.test-runner}}}}

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]})
