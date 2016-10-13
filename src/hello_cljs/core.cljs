(ns hello-cljs.core
  (:require [cljsjs.toastr]))

(defn log
  "Log a toast message"
  [msg]
  (.success js/toastr msg))

(defn ^:export run []
  (.log js/console "testing 1 2 3.")
  (log "testing toastr"))
