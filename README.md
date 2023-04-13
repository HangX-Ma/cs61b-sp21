# CS 61B Data Structures, Spring 2021

This is a repository contains CS61B labs and projects, which also records my self-learning process.

> The Gradescope course activation code is MB7ZPY. You can enroll this class at [Grade Scope](www.gradescope.com).

## Configuration

- VSCode Extensions
  - Extension Pack for Java, Microsoft
  - Language Support for Java(TM) by Red Hat, Red Hat
  - Java Debugger, Don Jayamanne
- IntelliJ Idea Extensions
  - IdeaVim, JetBrains s.r.o. ([customed vim key mapping](https://gist.github.com/HangX-Ma/5966a45a65131c2b0275bdf2c936b782))

If you want to code with VSCode, you can use this easy configuration. [config/template-make.sh](config/template-make.sh) is provided to assist your project development. You can have a glance at it if you are interested it!

> Note: MAVEN is used to manage the project since lab2. If you have installed the `Extension Pack for Java`, please configure your user [setting.json](config/template-setting.json).  What't more, don't forget to set the local repository property in `setting.xml` to where the `library-sp21/javalib` locates at. 

> **I think it is too hard to develop with maven in VSCode, though I have configured the debug function. However, the debug function needs project hash code to enable visualization and Java test integeration tool fails when using maven. _USE IDE FIRST_ until the perfect maven debug and test tools provided by VSCdoe! Anyway, I love VSCode~**

## Labs

- [x] Lab 1: Welcome to Java
- [x] Lab 2: JUnit Tests and Debugging

## Projects

- [x] Project 0: 2048

## License

MIT License
