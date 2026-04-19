// SBT plugins common to all of my Scala libraries.
// Automatically synchronised from 'https://github.com/SgtSwagrid/scala-library-config/'.

// sbt-ci-release bundles the following:
//   - sbt-dynver (git-tag versioning),
//   - sbt-pgp (PGP signing),
//   - sbt-sonatype (publishing to Maven Central)
// It exposes the `ci-release` sbt command used by the GitHub Actions release workflow.
// https://github.com/sbt/sbt-ci-release
addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.9.3")
