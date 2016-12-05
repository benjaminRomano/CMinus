# C-

Project for Compiler Construction (CS534)

Implements the following [grammar](http://cs434.cs.ua.edu/spring2010/projects.htm) with additional features.
C- includes for, break and continue statements. The C- compiler uses a shift-reduce parser which is implemented with JFlex and CUP.

## Setup

Run the following commands to build the compiler
```
   $ > sudo apt-get install ant
   $ > ant
   $ > cd dist/
   $ > g++ SM.cpp
```

## How To Run
Note: Input files are included in the input directory.
```
   $ > cd dist/
   $ > java -jar Compiler.jar <input file here>
   $ > ./a.out output.sm
```