---
title: "Gradle Build System"
weight: 1
---

The Radio Telescope project and the Spring framework are built and deployed using the 
Gradle build system. The build script has several Gradle tasks set up to streamline the 
develop->test->publish workflow.

## Gradle Tasks
The syntax for running a Gradle task from command line is as follows:
```bash
gradle taskName # run a single task
gradle task1 task2 task3 # run multiple tasks one after the other, stopping after first failure
```