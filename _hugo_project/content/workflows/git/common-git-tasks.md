---
title: "Common Git Tasks"
---

## Committing/Pushing Changes
When committing changes, its important to avoid merge conflicts, and to avoid committing 
files that should not be added to version control; this includes IDE files/folders like 
`*.iml` files and the `.idea` directory, as well as any keyfiles like `~/.gradle/gradle.properties`.
The workflow to ensure this goes smoothly typically looks something like:
```bash
git status  # show your local repo state
git add .   # or alternatively, git add path/to/file1.kotlin path/to/file2.vue ... etc.
git status  # double check which changes have been staged 
git push    # push to remote
# if git push fails from non-fast-forward, continue to these steps
git fetch   # fetch changes from remote
git rebase  # rebase remote changes onto local branch
git push    # should work now, unless there are merge conflicts
```

## Temporarily Revert Local Changes 
If you have local changes that you want to roll back, but don't want to lose, and the changes 
are not appropriate for a commit (an example might be some frontend debugging or testing code),
you can use `git stash`. To stash committed or staged changes, simply run `git stash`, or to stash 
*unstaged* changes, run `git stash -u`. For example:
```bash
git stash -u   # repository is now in clean state (mirrors remote); local changes are gone
# do other stuff you wanted to do 
git stash pop  # your changes are back!
```

#### To a NEW Branch
To move these 3 most recent commits to a **NEW** branch, you can do the following:
```bash
# note that anything not committed will be lost 
git branch new-branch-name   # all commits up to local HEAD will be on new branch
git reset --hard HEAD~3      # sets HEAD to HEAD minus 3 commits
git checkout new-branch-name # now on new branch with the moved commits
```

#### To an EXISTING Branch
To move commits to an **EXISTING** branch, **you need to first merge the changes into the existing 
local branch**. To do that, you can use the following procedure:
```bash
# again, note that anything not committed will be lost
git checkout existing-branch-name  # checkout the existing branch you want to move to
git merge master                   # merge the changes from the source branch into the other
git checkout master                # go back to the source branch 
git reset --hard HEAD~3            # sets HEAD to HEAD minus 3 commits
git checkout existing-branch-name  # now on the other branch with the moved commits
```