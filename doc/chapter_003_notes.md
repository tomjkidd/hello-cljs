`cljs.build/build` calls into `cljs.closure/build`

`add-implicit-options` diddles the compiler-options a little, calls
`get-upstream-deps` to get libs, foreign-libs, externs
When `optimizations` :none, set :cache-analysis and :source-map true
When `optimizations` :advance, set :static-fns and :optimize-constants to true if not explicitly set

`process-js-modules` Convert JavaScript modules to GCCJavaScript modules and write to disk
Applies `process-js-modules*` using compiler-options and :foreign-libs then :ups-foreign-libs keys

`process-js-modules*` compiler-opts and key
    builds up :js-module-index in env/*comiler* using parsed/munged goog.provides.
    `reduce` puts all modules into the index, and returns the updated compiler-options.
    Foreach IJavaScript, an entry is added to :libs.
    Finally, for the key, libs are `remove`d that were loaded

foreach lib found
    `cljs.js-deps/load-foreign-library`
        This is a memoized function, takes a Lib (library spec), return a LoadedLib
    `write-javascript`
        `cljs.util/output-directory`
        `cljs.util/changed?`
        `cljs.util/mkdirs`
        `cljs.js-deps/-source`
        If :preprocess then defmulti `js-transforms` called. This is a placeholder
            in case someone wants to do more ellaborate transform before js stage.
        If :module-type then defmulti `convert-js-module` called.
            :module-type is used to dispatch to proper handler.
            Handlers actually use GCC to create source code, as :source in return ijs
        `cljs.js-deps/-source` used to access the source code (which may be compiled)
        Writes all of this to file with a `spit`
        returns an IJavaScript, ijs, after merge with js input
    `cljs.js-deps/load-library`
        This is also a memoized function. It returns the :provides part of the IJavaScripts created
        `cljs.js-deps/load-library*`
            `find-js-resources`
                `find-js-fs`
            `parse-js-ns`
                Will read JavaScript code, on the prowl for goog.require and goog.provide. Like a true gentleman, it also lists an assumption that all of these happen before the first function call.
                This is what creates the :requires/:provides keys on the IJavaScripts
`check-output-to`
    NOTE: :print can be used for printing the output instead of using a file
`check-output-dir`
    Just checks that :output-dir is a string
`check-source-map`
    Checks relationships between :output-to :source-map :output-dir :optimizations
    using asserts
`check-source-map-path`
    Checks :source-map-path is a string, and presence of :output-to and :source-map
    for advanced compilation. Also uses asserts for this
`check-output-wrapper`
    Checks conditions for :output-wrapper option, which determines if the code
    is wrapped with (function(){ ...)(); wrapper to avoid pollution of the global
    namespace
`check-node-target`
    Checks conditions for :target and :optimizations so that :nodejs is not
    used with :whitespace
`swap!`
    Called on the compiler-env, adding compiler options as :options
    :target is transferred for availability
    :js-dependency-index uses `(deps/js-dependency-index opts)`
    `deps/js-dependency-index` 
        `build-index` 
            Uses `library-dependencies` and `goog-dependencies` to create
            a HashMap of :file to IJavaScript
        `library-dependencies`
            Uses `load-library` and `load-foreign-library` to save (and possibly compile)
            js sources from [:libs :foreign-libs :ups-libs :ups-foreign-libs] options
        `goog-dependencies`
            Uses _goog/deps.js_ file to parse _goog.addDependency_ calls to determine
            available libraries. Libs have :file, :provides, :requires, and :group
    `-find-sources`, the IJavaScript method return one or more JavaScripts, without compiling them
        Concerns itself with going from File, Directory, Url, Jar, clojure.lang.PersistentList, String, clojure.lang.PersistentVector to that file's source
        For the case where the argument is a Directory, `comp/find-root-sources` is used
        `cljs.compiler/find-root-sources`
            `cljs-files-in`
                Returns a sequence of .cljs and .cljc files in a directory
            `find-source`
                First use of the analyzer!
                `cljs.analyzer/parse-ns`
                Takes a file, returns IJavaScript compatible map (not AST node!)
                    `cljs.env.macros/ensure`
                        Ensures that cljs.env/\*compiler\* is set, then runs body
                    `util/ns->source`
                        Returns the io/resource for a given namespace symbol
                        `ns->relpath`
                            Uses `munge-path` to translate from namespace to a path string.
                                `munge-path` uses `clojure.lang.Compiler/munge` on a string to do this work.
                            Works to locate .cljs and .cljc files, trying to locate a resource in that order
                        `io/reader` used to make sure source is ready to be read
                        `source-path` is used with src to give file to
                        `forms-seq*`
                            `cljs.tools.reader/read`

`comp/compile-root`
