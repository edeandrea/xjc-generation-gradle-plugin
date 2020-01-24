_Have something you'd like to contribute? We welcome pull requests, but ask that you carefully read this document first to understand how best to submit them; what kind of changes are likely to be accepted; and what to expect from the team when evaluating your submission._

_Please refer back to this document as a checklist before issuing any pull request; this will save time for everyone!_

# Importing into IDE
The project uses Gradle as its build tool, so any IDE which supports Gradle can/should work . The project is written entirely in [Kotlin](https://kotlinlang.org). Whatever IDE you choose should have good Kotlin tooling available.

# Understand the basics 
Not sure what a pull request is, or how to submit one? Take a look at GitHub's excellent [help documentation first](https://help.github.com/articles/using-pull-requests).

# Search GitHub issues - create an issue if necessary
Is there already an issue that addresses your concern? Do a bit of searching in our [GitHub issues](https://github.com/edeandrea/xjc-generation-gradle-plugin/issues?q=is%3Aissue) to see if you can find something similar. If not, please create a new issue before submitting a pull request.

We use labels in our issues to have different meanings:
- **help wanted** - Requests for help from the community
- **investigating** - We are currently looking into the issue
- **question** - A question from the community
- **vx.x** - Which release a particular issue is intended for
- **waiting-on-reporter** - Our team is waiting on the original reporter of the issue for input
- **waiting-on-validation** - Our team has done work for the reporter and is awaiting validation/verification from the reporter
- **enhancement** - An issue we deem to be an enhancement of functionality
- **bug** - An issue we deem to be a defect/bug with something

You can search through the issues to see if what you are looking for has already been done or answered. Please take the time to do this, otherwise any work you've done may become throw-away if we already have a ticket for it or it's already been done.

# Discuss non-trivial contribution ideas with committers
If you're considering anything more than correcting a typo or fixing a minor bug, please discuss it by [creating an issue on our issue tracker](https://github.com/edeandrea/xjc-generation-gradle-plugin/issues?q=is%3Aissue) before submitting a pull request. We're happy to provide guidance but please spend an hour or two researching the subject on your own including searching the forums for prior discussions.

If your submission includes additions of new 3rd party dependencies, please be ready to have a very good reason for doing so. We try to keep the dependency footprint as small as possible and are very selective and conservative about bringing in external dependencies.

# Create your branch from master
Create your topic branch to be submitted as a pull request from master. The team will consider your pull request for backporting on a case-by-case basis; you don't need to worry about submitting anything for backporting.

# Use short branch names
Branches used when submitting pull requests should preferably be named according to GitHub issues, e.g. '1234' or '1234-fix-npe'. Otherwise, use succinct, lower-case, dash (-) delimited names, such as 'fix-warnings', 'fix-typo', etc. This is important, because branch names show up in the merge commits that result from accepting pull requests, and should be as expressive and concise as possible.

# Keep commits focused
Remember each ticket should be focused on a single item of interest since the tickets are used to produce the changelog. Since each commit should be tied to a single GitHub issue, ensure that your commits are focused. For example, do not include an update to a transitive library in your commit unless the GitHub is to update the library. Reviewing your commits is essential before sending a pull request.

# Code style/formatting
The project source code has its code style & formatting rules checked into source control as part of the project. Please adhere to these rules by making sure your IDE is set up to use the per-project formatting and style rules. Our build process will soon also enforces checkstyle rules which will catch a lot of things.

# Use @since tags for newly-added public API types and methods
e.g.
```kotlin
/**
 * ...
 *
 * @author First Last Month Year
 * @since 1.4
 * @see ...
 */
```

```kotlin
/**
* This method does something
* @param input Some input that is used to do something
* @return Some value that can be used in some way, shape, or fashion
* @since 2.1
*/
fun someNewMethodInSomeExistingClass(input: String): String {
  return "Hello"
}
```

# KDocs
Relevant [KDocs](https://kotlinlang.org/docs/reference/kotlin-doc.html) must exist on any and all public classes and methods - no exceptions. We will have rules that will fail a build if they are not found.

- Any new classes created should also contain your name, month, & year in the `@author` tag
- Any new classes created **MUST** contain the appropriate Apache License text at the top
   ```kotlin
  /*
   * Copyright 2014-2020 the original author or authors.
   *
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   *     http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   */
  ```
- If the method can potentially throw some kind of  exception, make sure to document that using the `@throws`/`@exception` KDoc tag
    - Kotlin doesn't have checked exceptions, but its a good practice to document if and when you know something could be thrown

# Submit JUnit test cases for all behavior changes
Search the codebase to find related unit tests and add additional `@Test` methods within. 

1. Any new test classes should end in the name `Tests` (note this is plural). For example, a valid name would be `XjcPluginTests`. An invalid name would be `XjcPluginTest`.
1. New test methods should not start with test. This is an old JUnit 3 convention and is not necessary since the method is annotated with `@Test`.
1. Make sure your tests are written using JUnit Jupiter/JUnit 5, not JUnit 4
    - This means the `@Test` annotation should come from the `org.junit.jupiter.api` package and not the `org.junit` package
1. All assertions done in tests should use the [AssertJ](http://joel-costigliola.github.io/assertj/) assertion library, not the assertions built into the JUnit library

# Squash commits
Use `git rebase --interactive`, `git add --patch` and other tools to "squash" multiple commits into atomic changes. In addition to the man pages for git, there are many resources online to help you understand how these tools work. Here is one: http://book.git-scm.com/4_interactive_rebasing.html.

# Use real name in git commits
Please configure git to use your real first and last name for any commits you intend to submit as pull requests. For example, this is not acceptable:

```
Author: Nickname <user@somewhere.com>
```
Rather, please include your first and last name, properly capitalized:

```
Author: First Last <user@somewhere.com>
```
This helps ensure traceability and also goes a long way to ensuring useful output from tools like `git shortlog` and others.

You can configure this globally via the account admin area (useful for fork-and-edit cases); globally with

```
git config --global user.name "First Last"
git config --global user.email user@somewhere.com
```

or locally for the acid-framework repository only by omitting the '--global' flag:

```
cd xjc-generation-gradle-plugin
git config user.name "First Last"
git config user.email user@somewhere.com
```

# Format commit messages

```
Short (50 chars or less) summary of changes

More detailed explanatory text, if necessary.  Wrap it to about 72
characters or so.  In some contexts, the first line is treated as the
subject of an email and the rest of the text as the body.  The blank
line separating the summary from the body is critical (unless you omit
the body entirely); tools like rebase can get confused if you run the
two together.

Further paragraphs come after blank lines.

 - Bullet points are okay, too

 - Typically a hyphen or asterisk is used for the bullet, preceded by a
   single space, with blank lines in between, but conventions vary here

Fixes #123
```

1. Keep the subject line to 50 characters or less if possible
1. Do not end the subject line with a period
1. In the body of the commit message, explain how things worked before this commit, what has changed, and how things work now
1. Include `Fixes #<issue-number>` at the end if this fixes a GitHub issue  
1. Avoid markdown, including back-ticks identifying code

# Run all tests prior to submission

On Unix/Mac:
```
cd xjc-generation-gradle-plugin
./gradlew clean build
```

On Windows:
```
cd xjc-generation-gradle-plugin
gradlew.bat clean build
```

If any tests fail (regardless if they are tests you wrote/modified or not) then it is your responsibility to figure out what went wrong before submitting. We don't keep code that doesn't pass tests in our codebase. When you first cloned the repository all tests would have passed. If a test you didn't touch now doesn't pass, then something you did in the changes you made has broken an existing test.

# Sync your local fork
Prior to committing, make sure you [sync your local fork](https://help.github.com/articles/syncing-a-fork/) from the upstream project. This is essential to making sure your code will merge properly and all regressions are run.

# Submit your pull request
Subject line:

Follow the same conventions for pull request subject lines as mentioned above for commit message subject lines.

In the body:

1. Explain your use case. What led you to submit this change? Why were existing mechanisms in the insufficient? Make a case that this is a general-purpose problem and that yours is a general-purpose solution, etc
1. Add any additional information and ask questions; start a conversation, or continue one from GitHub Issues
1. Mention any GitHub Issues

Note that for pull requests containing a single commit, GitHub will default the subject line and body of the pull request to match the subject line and body of the commit message. This is fine, but please also include the items above in the body of the request.

Once created, your pull request will go through a build process. Any test failures or quality violations will automatically make the pull request "red", meaning it can not be merged until they are resolved. Continue pushing changes to the same topic branch to add new commits to the pull request.

# Mention your pull request on the associated GitHub issue
Add a comment to the associated GitHub issue(s) linking to your new pull request.

# Expect discussion and rework
The team takes a very conservative approach to accepting contributions. This is to keep code quality and stability as high as possible, and to keep complexity at a minimum. Your changes, if accepted, may be heavily modified prior to merging. You will retain "Author:" attribution for your Git commits granted that the bulk of your changes remain intact. You may be asked to rework the submission for style (as explained above) and/or substance. Again, we strongly recommend discussing any serious submissions with the team prior to engaging in serious development work.

Note that you can always force push (`git push -f`) reworked/rebased commits against the branch used to submit your pull request (i.e. you do not need to issue a new pull request when asked to make changes).
