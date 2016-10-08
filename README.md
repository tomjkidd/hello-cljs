# hello-cljs

A project to learn how the clojurescript compiler works

## Goal

The goal of this series is to get a feeling for _how_ your ClojureScript 
code turns into JavaScript code that runs in the browser.

It is meant to be a guided tour of the internals to create intuition for
where to look when you encounter situations that you don't understand so
that when you are in that situation you remain calm and can reason through
how to get your code working again.

NOTE: It is a work in progress...

### Assumptions about the Reader

* comfortable with shell commands
* comfortable with managing creation of files in a clojure project.

### FYI

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen


This project uses git tags to checkout small pieces of the project at a time 
to get complete snaptshots of code that correspond to each chapter.


## Chapters

[Chapter 1 - The Happy Path](/doc/chapter_001_the_happy_path.md)

[Chapter 2 - The Build Hook](/doc/chapter_002_the_build_hook.md)


## Resources/Prior Art

For a bare bones startup, David Nolen created the Quick Start documentstion.
This is essential reading for understanding how to start from scratch, and
gives context for how to use the clojurescript jar by itself. lein-cljsbuild
will make some of these things simpler, but read this document if you are
curious about how to create low-level scripts that use the compiler.

[David Nolen - Quick Start](https://github.com/clojure/clojurescript/wiki/Quick-Start)

[David Nolen - Mies](https://github.com/swannodette/mies)

In my research, I found a post that was close to my approach to the intitial setup.

[James Hughes - Basic ClojureScript Setup](https://yobriefca.se/blog/2014/05/30/basic-clojurescript-setup/)

## License

Copyright © 2016 Tom Kidd
