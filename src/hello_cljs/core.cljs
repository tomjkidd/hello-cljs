(ns hello-cljs.core)

(defn ^:export run []
  (.log js/console "testing 1 2 3."))
