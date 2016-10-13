(ns types
  (:require [schema.core :as s]))

(defn union
  "Will pass the type checking, but allows me to specify known union types"
  [& rest]
  s/Any)

(defn union-experimental
  "Will allow union type to be specified as a list of types"
  [& union-types]
  (let [cond-type-pairs (mapcat #(list (fn [x] (= (type x) %))
                               %) union-types)]
    (apply s/conditional cond-type-pairs)))

(defn unknown
  [& rest]
  s/Any)

(def Path
  s/String)

(def Lib
  "A JavaScript library

  See also:

  Related Compiler Options
  :foreign-libs
  :ups-foreign-libs"
  :file (unknown)
  :provides (unknown)
  :module-type (union :commonjs :amd :es6))

(def LoadedLib
  "Here min refers to minified versions of the lib"
  :file (union s/Path s/Url)
  :file-min (s/Maybe s/File)
  :foreign s/Boolean
  :requires (unknown)
  :provides (unknown)
  :url-min (s/Maybe s/Url)
  :url s/Url
  :source-file (s/Maybe Path)
  :preprocess s/Boolean)

(def IJavaScript
  ""
  :url s/Url
  :file Path
  :module-type (union :amd :commonjs :es6)
  :out-file Path
  :requires (unknown)
  :provides (unknown)
  :group (unknown)
  :source-file (s/Maybe String)
  :source-forms (s/Maybe (unknown))
  :macros-ns (unknown)
  :lines s/Num ;The count of lines in a IJavaScript file
  )

(def LibraryGraphNode
  "Also refered to as AST"
  :url s/Url
  :require (unknown)
  :provides (unknown)
  :closure-lib s/Boolean
  :lib-path (unknown))


;; DEFINED IN CLOJURESCRIPT (but here for convenience)

;; closure.clj

(defprotocol Inputs
  (-paths [this] "Returns the file paths to the source inputs"))

(defprotocol Compilable
  (-compile [this opts] "Returns one or more IJavaScripts.")
  (-find-sources [this opts] "Returns one or more IJavascripts, without compiling them."))

(defprotocol ISourceMap
  (-source-url [this] "Return the CLJS source url")
  (-source-map [this] "Return the CLJS compiler generated JS source mapping"))

; js_deps.cljc

(defprotocol IJavaScript
  (-foreign? [this] "Whether the Javascript represents a foreign
  library (a js file that not have any goog.provide statement")
  (-closure-lib? [this] "Whether the Javascript represents a Closure style
  library")
  (-url [this] "The URL where this JavaScript is located. Returns nil
  when JavaScript exists in memory only.")
  (-provides [this] "A list of namespaces that this JavaScript provides.")
  (-requires [this] "A list of namespaces that this JavaScript requires.")
  (-source [this] "The JavaScript source string."))


; The compiler uses this with AST nodes from the analyzer to actually produce javascript
(defmulti emit* :op)
