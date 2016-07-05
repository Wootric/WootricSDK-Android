Contributing
=============

A guide for Wootric's development workflow. This workflow is based on a agile sprint model, where there will be (bi-)weekly releases to production with a number of features that have been completed and tested during the sprint.


Current Sprint Trello Board
--------------------

[https://trello.com/b/zi1i51gt/current-sprint](https://trello.com/b/zi1i51gt/current-sprint)

Overview
--------

Here's the basic process you should follow when developing a feature or fixing a bug. For specifics on how this works in Git, please read thoughtbot's [Git protocol].

* Choose a Trello card with a feature or bug.

* Create a feature branch off of `master`. Prefix the branch name with your initials, i.e. `pt-fix-dropdowns`.

* Finish your feature, & push your branch to GitHub.

* Create a pull request & request a code review.

* Move your Trello card to the correct list (i.e. "Code Review") on the "Current Sprint" board.

* Fix your code & squash the commits into fewer logical commits.

* Once you get a :+1: and everything looks good go ahead and merge into `master`. 1 :+1: is usually good enough but if you feel uncomfortable about it ask for more reviews.

* Move your Trello card to the correct list (i.e. "Finished Feature") on the "Current Sprint" board.

**IMPORTANT: Everything in `master` should be deployable. It's your responsibility to release a new version to Maven every time you merged to master.**

[Git protocol]: https://github.com/thoughtbot/guides/blob/master/protocol/git/README.md

Pull Requests
-------------

We follow a rigorous PR policy and we encourage everyone to address to it, the
workflow goes like this:

- Checkout a new branch for the feature or bug that you will work on, follow the
  convention: yourinitials-feature-name. Example: ma-js-email-validator

- After working and successfully testing your feature push the branch to GitHub
  and make a pull request.

- Check that CI passes and wait for the comments to roll in. For each comment    
  that you choose to address make a new commit so the history and conversation   
  is not lost.

- If your PR gets a :+1: your are good to merge. Remember to squash your
  comments into one commit and before you merge.

- After you've merged delete your branch both locally and on GitHub.
