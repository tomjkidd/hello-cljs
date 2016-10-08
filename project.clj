(defproject hello-cljs "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [org.clojure/clojurescript "1.9.277"]]
  :plugins [[lein-ring "0.9.7"]
            [lein-cljsbuild "1.1.4"]]
  :ring {:handler hello-cljs.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]
                        ;piggieback is used to allow a ClojureScript REPL in emacs
                        ;NOTE: defaults to using rhino, mozilla's js engine written in Java
                        [com.cemerick/piggieback "0.2.1"]
                        [org.clojure/tools.nrepl "0.2.10"]
                        [cljsbuild "1.1.4"]]
         :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :incremental true
                        :assert true
                        :compiler {:output-to "resources/public/js/hello-cljs.js"
                                   :output-dir "resources/public/out"
                                   :warnings true
                                   :optimizations :none
                                   :source-map true
                                   :pretty-print true}}
                       {:id "prod"
                        :source-paths ["src"]
                        :compiler {:output-to "resources/public/js/hello-cljs.min.js"
                                   :output-dir "target/cljscompiler/prod"
                                   :elide-asserts true
                                   :optimizations :advanced
                                   }}]})
