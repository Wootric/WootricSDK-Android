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

* Finish your feature & push your branch to GitHub.

* Create a pull request & request a code review.

* Move your Trello card to the correct list (i.e. "Code Review") on the "Current Sprint" board.

* Fix your code & squash the commits into fewer logical commits.

* Once you get a :+1: and everything looks good go ahead and merge into `master`. 1 :+1: is usually good enough but if you feel uncomfortable about it ask for more reviews.

* Move your Trello card to the correct list (i.e. "Finished Feature") on the "Current Sprint" board.

**IMPORTANT: Everything in `master` should be deployable. It's your responsibility to release a new version to Maven every time you merged to master.**

[Git protocol]: https://github.com/thoughtbot/guides/blob/master/protocol/git/README.md

Build & Release
---------------

The library is stored on [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ca:%22wootric-sdk-android%22). To upload a new version, please follow these steps:

* Make code changes

* Update VERSION_NAME and VERSION_CODE in gradle.properties file. Convention for VERSION_NAME are 3 digits specifying “Major number.Minor number.Patch number”. Convention for VERSION_CODE is 1 number reflecting VERSION_NAME without the dots (so if VERSION_NAME=1.2.3, then VERSION_CODE=123)

* Add confidential fields. Of nexus sonatype (platform for distribution management):
	```
	nexusUsername=<nexusUsername>
	nexusPassword=<Password for the nexusUsername>
	```
	Of signing (differs for every developer, needed for signArchives tasks):
	```
	signing.keyId=<secrings ID>
	signing.password=<secrings password>
	signing.secretKeyRingFile=<path to secrings file>
	```
	For more information on signing check this [guide](https://docs.gradle.org/current/userguide/signing_plugin.html) and this [guide](http://central.sonatype.org/pages/gradle.html)

	**IMPORTANT: confidential fields shouldn’t be committed to repo**

* Run deploy task `gradle uploadArchive` from Terminal or Android Studio

* Login to [Sonatype](https://oss.sonatype.org/#stagingRepositories). Dashboard looks like this:
![Dashboard](https://cloud.githubusercontent.com/assets/1431421/16775100/aa4b3a9e-4824-11e6-9608-c6add82f2723.png)

* Click on the new version row. “Close” action button should be enabled now. Click it, confirm the dialog

* Wait until “Release” button is active (you may click “refresh” a few times). Click it and confirm a dialog

* If all went successful, the new version should be available in maven in a few hours

* When new version is available update the version code in [README](https://github.com/Wootric/WootricSDK-Android/blob/master/README.md) and create a new Github release

Useful information about deployment process in [this article](https://medium.com/android-news/the-complete-guide-to-creating-an-android-library-46628b7fc879)

**NOTE: ask CEO or CTO for access and credentials (`nexusUsername` & `nexusPassword`)**

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
