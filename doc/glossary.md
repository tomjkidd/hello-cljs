* build: The name of a hashmap used to configure a build with lein-cljsbuild

* [checkout](https://github.com/technomancy/leiningen/blob/master/doc/TUTORIAL.md#checkout-dependencies):
In this context, a type of project dependency that is specific to leiningen.

* lib: A Closure Compiler compatible JavaScript library (using goog.provide and goog.require)
* munge: Process text in a way that converts it from ClojureScript land to/from JavaScript

* module: In the JavaScript context, (union :commonjs :amd :es6)

* munge: String replacement to support naming differences between clojure and
  other supporting systems. For example, replacing `-` with `_` when converting
  a namespace to the corresponding filename on the file system.

* path: Usually a String that refers to a file on the file system.

* upstream: ups - 

## Compiler Options

:closure-defines
There is a macro, cljs.core/goog-define, that allows you to set variable, like 'goog.Debug.

If the key is a Symbol, comp/munge is used to prep it for JS

(def ClojureDefines
 { (s/Str | s/Keyword) s/Any})


:browser-repl

If included, will create a :preloads option with the value 'clojure.browser.repl.preload
 
 :preprocess
 
 Used to transform a file that will then be interpretted as JavaScript
 
 :output-wrapper
 
 s/Boolean for whether or not to wrap in an IEEF, to avoid global namespace pollution
