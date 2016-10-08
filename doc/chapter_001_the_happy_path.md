# Chapter 1: The Happy Path

We'll start off by getting a simple example project set up and working with 
development and production builds. The goal of this chapter is to get our
programming environment into a working state.

## Download clojurescript

The clojurescript compiler is a tool that is integrated into projects in 
order to compile ClojureScript (note the reference here as the language,
as opposed to clojurescript, the project for the compiler) into JavaScript.

The clojurescript compiler does it's work using the Google Closure Compiler 
(GCC), a highly configurable JavaScript to JavaScript compiler. The main 
purpose of the GCC was to generate highly optimized code for large client
applications. The Google Closure Library (GCL) was created to facilitate
creating cross-browser friendly code that would then be optimized by the
GCC. By using the GCC and GCL, ClojureScript benefits by getting browser
support and optimization. 

For now, the only GCL code we need to think about are the functions provided
for namespaces. `require` and `provide`. But, more on that later...

The following commands will download clojurescript and attempt to build it.
The result of the build is a java .jar file that contains everything needed
to allow our project to turn ClojureScript files into JavaScript.

The build script will result in a version of org.clojure/clojurescript 
being created in your local repository (for me, that is _~/.m2/repository_),
which can then be used as a dependency in our project.

NOTE: You can use a stable release jar by going to [http://clojurescript.org](http://clojurescript.org). I've decided not to just so that I am using the most 
current code.

```bash
git clone https://github.com/clojure/clojurescript.git
cd clojurescript
./script/build
```
## Create an empty compojure project

Next, I want to create an empty project where we can create our code. There
are probably templates that provide more convenient setup, but the emphasis
of this article is to use the fewest number of extra tools so that we are
clear about which pieces are responsible for what functionality.

Compojure is used so that we can serve the web application files that we
create for the project.

```bash
cd ..
lein new compojure hello-cljs
```

NOTE: compojure will serve files from _resources/public_, which is where
we will put our compiled code later.

## Create a simple cljs file

A minimal .cljs file is created in order to have something to compile.

Create the file _src/hello\_cljs/core.cljs_, and then add this to it

```clojure
(ns hello-cljs.core)
(defn run []
  (.log js/console "testing 1 2 3."))
```

This should be enough to verify that compilation is working.

## Create a simple html file

A minimal .html file is created in order to verify that once compiled, our
code actually does something in the browser.

This is where the use of `goog.require` comes into the picture. It is
a function that serves the same purpose as `require` in Clojure, to make our
namespaced code available from a different namespace. The `hello_cljs.core.run`
function is available as a result, and our page just calls it.

NOTE: The hyphens in the namespace _hello-cljs.core_ become underscores
in JavaScript.

```html
<html>
    <head>
        <style>
         html,body {
             margin: 0;
             padding: 0;
             background-color: #EEEEEE;
         }
        </style>
    </head>
    <body>
        <div id="app"></div>
        <script src="out/goog/base.js" type="text/javascript"></script>
        <script src="js/hello-cljs.js" type="text/javascript"></script>
        <script type="text/javascript">goog.require("hello_cljs.core");</script>
        <script>
         hello_cljs.core.run()
        </script>
    </body>
</html>
```

## Update project.clj

The next important thing is to tell our project about the version of
clojurescript that we built (_1.9.277_). We also want to add the lein-cljsbuild
plugin to allow us to run the compiler using a simple command line interface.

```clojure
:dependencies [...
               [org.clojure/clojurescript "1.9.277"]
               ...]
:plugins [...
          [lein-cljsbuild "1.1.4"]
          ...]
```

After including clojurescript and cljsbuild, we want to create a dev 
build.

NOTE: You create a list of builds here, and the point of different ones
is to provide different build configurations.

```clojure
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
                       }]}
```

CAUTION: The directory specifactions are picky, and can give cryptic errors.
For :source-paths I had put "src" instead of ["src"], and I admit the name
:source-paths implies a list of paths, but I still had to deal with it.
For :output-dir, I had orignially put "/resources/public/js/hello-cljs.js" 
which threw an exception because of theleading forward slash.

:source-paths is a list of strings for where to look for .cljs files.

:output-to tells the compiler where to put the JavaScript file that is created.

:output-dir is where the supporting Google and ClojureScript core code will
go.

The other options can be ignored for now.

At this point the following command should succeed:

```bash
lein cljsbuild once dev
```

You should also be able to verify that hello-cljs.js was created.

## Web Server

Compojure was used to provide us with a built in web server for our static
files that are in resources/public.

Run the server with the following command:

```shell
lein ring server-headless
```

Then visit [http://localhost:3000/index.html](http://localhost:3000/index.html)
and open the developer tools. You should see the "Testing 1 2 3." in the 
console output.

At this point our base development environment is setup and working.
We want to creat a production build next.

## Verify the 'prod' build

Similar tasks to the previous cljs and html file creation can be done to verify
that the minified code also works.

### Cljs

NOTE: ^:export metadata needs to be added to run function to allow it to be 
available from JavaScript.

### Html

NOTE: out/goog/base.js dependency and the goog.require("hello_cljs.core") call
can be removed.

NOTE: js/hello-cljs.min.js is the minified production JavaScript buildfile.
This file is refered to below in the prod build definition.

### Lein

We also need to create a new build profile for prod in profiles.clj at
:cljsbuild :builds

```clojure
                       {:id "prod"
                        :source-paths ["src"]
                        :compiler {:output-to "resources/public/js/hello-cljs.min.js"
                                   :output-dir "target/cljscompiler/prod"
                                   :elide-asserts true
                                   :optimizations :advanced
                                   }
```

After saving this config change, the following command should work to create
the minified JavaScript file.

```bash
lein cljsbuild once prod
```

At this point, you should be able to access both index.html and index.min.html
and see the simple console output that our example ClojureScript code provided.

This is the happy path, we've created some ClojureScript code, compiled it
to JavaScript, and can verify that it works in the browser. The next chapter
will talk about ... TODO: lead into next chapter when actual content is done.

TODO: Talk about git tags for a working version of the code if you are having
trouble
