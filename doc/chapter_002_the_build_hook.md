# Chapter 2 - The Build Hook

This chapter picks up where we left off, having created a simple ClojureScript
function, `hello-cljs.core/run`, and verified that it works by calling it 
from our _index.html_ file. We used the `lein-cljsbuild` plugin to compile
our code, and a reasonable next step is too ask _how does the plugin use the 
compiler?_.

Our main goal is to see how that ClojureScript function we wrote is turned into 
JavaScript, and the focus of this section is finding an entry point into
the clojurescript compiler. The compiler library is made up of a decent 
amount of code and this will help us jump into the source code with a 
purpose.

## The starting point

We start with our build command.

```bash
lein cljsbuild once dev
```

The plugin _cljsbuild_ can be found on Github as [lein-cljsbuild](https://github.com/emezeske/lein-cljsbuild).

The [main plugin file](https://github.com/emezeske/lein-cljsbuild/blob/master/plugin/src/leiningen/cljsbuild.clj) 
has a function called `cljsbuild`. This is the function that is called with our
`lein cljsbuild once dev` command.

As you will see, the [lein.core.eval](https://github.com/technomancy/leiningen/blob/master/leiningen-core/src/leiningen/core/eval.clj) 
file is also needed to understand what is happening. It is linked here so
that you can view it to verify the call stack that is detailed below.

## Follow your nose

By doing a high-level analysis of the code used to execute our build command,
we should be able to identify all the function calls that it makes and follow 
their flow, possibly guessing what intermediate functions and let definitions 
are doing. This makes it so that we get a feel for how big the call stack is 
without reading all of the source code to start.

`lein cljsbuild once dev` is issued to the command line interface of lein. We
can think of the following substitutions being made as a result of running this
commmand.

contents of _project.clj_ -> project

"once" -> subtask

["dev"] -> args

NOTE: The `->` are being used as symbols for substitution. `"once" -> subtask`
is used to indicate that the string "once" will be refered to as subtask in
the documentation that follows.

`(cljsbuild project subtask & args)` is the entry point into the plugin code.

(config/extract-options project) -> options

`(once project options args)` is called, corresponding to our "once" subtask.

args -> build-ids
false -> watch?

`(run-compiler project options build-ids watch?)` is then called.

'(require 'cljsbuild.compiler 'cljsbuild.crossover 'cljsbuild.util) -> require-form

nested `cljsbuild.compiler/run-compiler` -> do-form

NOTE: I created the names `require-form` and `do-form` just for the purpose
of being able to talk about them with names.

NOTE: I have intentionally simplified my view of the code here to maintain
a high level perspective. Once we have seen what else the call entails, we
can focus here afterward.

`(run-local-project project crossover-path parsed-builds require-form do-form)` is then called.

(leiningen.cljsbuild.subproject/make-subproject project crossover-path parsed-builds) -> subproject

wrapped `do-form` in a try/catch -> try-catch-form

NOTE: Again, `try-catch-form` is a name I created for ease of discussion.

`(leiningen.core.eval/eval-in-project subproject try-catch-form require-form)`

## The signal versus the noise

The `cljsbuild`, `once`, `run-compiler`, and `run-local-project`
functions are easy to skim through locally. I can also guess at what `eval-in-project`
and `cljsbuild.compiler/run-compiler` do. The form that I have identified as
`do-form` contains the call to the compiler, and there is a decent amount of 
work done to set up the call. 

TODO: This is more useful as a start to the next chapter.
If we unpack the arguments that are created by
all of this plugin code, with the intent to limit the scope to what our 
specific `lein cljsbuild once dev` command does, we should be able to make an
equivalent call from the repl on our own as a starting point.

NOTE: [cljsbuild](https://github.com/emezeske/lein-cljsbuild/tree/master/support) is
a supporting project within the lein-cljsbuild project, that is where the
definition for `cljsbuild.compiler/run-compiler` is found.

At this point, it makes sense to drill down into `do-form`, ultimately this
is the code that we are looking for.

Again, we'll do call substitutions to get a feel for what steps are involved.

TODO: Fill in more of the gaps, what the values are for each argument

```clojure
(cljsbuild.compiler/run-compiler
   cljs-paths
   checkout-paths
   crossover-path
   crossover-macro-paths
   compiler-options
   notify-command
   incremental?
   assert?
   last-dependency-mtimes
   watching?)
```

```clojure
(compile-cljs
    cljs-paths
    compiler-options
    notify-command
    incremental?
    assert?
    watching?)
```

(apply cljs.build.api/inputs cljspaths) -> inputs

`(cljs.build.api/build inputs compiler-options)`

Now we have located the hook into the actual clojurescript compiler that is
responsible for turning our build command into action.
      
