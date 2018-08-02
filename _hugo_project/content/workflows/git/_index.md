---
title: "Git"
---

{{% notice info %}}
**AT ALL COSTS, AVOID PUSHING CODE THAT WILL FAIL TO BUILD!**

For the Radio Telescope Contracts project, this means that before you push any changes to remote,
you should run the `gradle verify` task; this is essentially an alias for 
`gradle build test jacocoTestCoverageVerification`.

**IMPORTANT**: if you use the git client built into IntelliJ IDEA, make sure you set 'update' to use 
`git rebase` instead of `git pull`.
{{% /notice %}}

## Learning Git
If you walked away from CS320 absolutely hating Git, check out [this site](https://learngitbranching.js.org/). It is the best hands-on 
tool I have found to learn 
about the different git workflows and really teaches you how to harness the power Git offers.

## Git Commit Messages
When writing a commit message, **write in the present tense** (e.g. use "add" and not "added", or
"implement" instead of "implemented"). The first line should be short (~50 characters), concise,
and descriptive (include some context of the module you're working on). Then, if more detail 
is needed to describe the commit contents, an optional extra description should be included on the 
second line. Lastly, **you must include the related YouTrack issue ID(s) as the last line of your commit 
message**. This can be done through the IntelliJ git client commit message editor, or from the command 
line (each appearance of the -m flag indicates a new line of the message):

`git commit -m "Implement Address Entity create command object" -m "that will persist
a new Address record"`

See [What Makes a Good Commit Message?](https://github.com/erlang/otp/wiki/Writing-good-commit-messages)

## Branching 
Each **major** feature/project should get it's own branch; typically, these will correlate with 
something marked accordingly on whatever task management software is being used.
If you need to branch off of one of the major branches, that's okay, but it should be 
merged back into a major branch as soon as possible, and **the branch should be removed 
once it is merged!** We don't want 35 stale branches sitting around on our repository and following
this convention will ensure this doesn't happen.

## Development Process
Typically, the process of pushing new code should be:

```bash
git status # show changes 
git add . # or specific files; DO NOT COMMIT IDE FILES/FOLDERS
git status # double check staged changes 
git fetch # fetch remote changes; if no output is produced here, you can skip the next step
git rebase # use rebase, NOT pull; avoids merge conflicts 
git push # push to remote
```

## Merging Branches
`git rebase` can also be used to avoid merge conflicts when merging branches. Procedure is as follows:
```bash
# current branch: source-branch
git fetch  # make sure current
git rebase # branch is up to date
git checkout dest-branch # checkout destination branch
git fetch  # make sure it is 
git rebase # up to date
git rebase source-branch # rebase source-branch onto dest-branch
git push # push to remote
```
For merging something onto master, you will need to submit a pull request on GitHub. 
If you are unfamiliar with how to do that, [go here](https://help.github.com/articles/creating-a-pull-request/)

Nobody (aside from the repository owner) should be able to push code directly onto master.