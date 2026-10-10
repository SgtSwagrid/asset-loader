// SBT plugins common to all of my Scala libraries.
// Automatically synchronised from 'https://github.com/SgtSwagrid/scala-library-config/'.

// sbt-ci-release bundles the following:
//   - sbt-dynver (git-tag versioning),
//   - sbt-pgp (PGP signing).
// It exposes the `ci-release` sbt command used by the GitHub Actions release workflow,
// which publishes to Maven Central through sbt's own support for the Central Portal.
// https://github.com/sbt/sbt-ci-release
addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.12.1")
