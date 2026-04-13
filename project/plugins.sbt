// External plugins for SBT.

// sbt-ci-release bundles the following:
//   - sbt-dynver (git-tag versioning),
//   - sbt-pgp (PGP signing),
//   - sbt-sonatype (publishing to Maven Central)
// It exposes the `ci-release` sbt command used by the GitHub Actions release workflow.
// https://github.com/sbt/sbt-ci-release
addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.9.3")

// Allow SBT to configure IntelliJ project settings.
addSbtPlugin("org.jetbrains.scala" % "sbt-ide-settings" % "1.1.4")

// Scalafmt is a linter for Scala, supporting automatic code reformatting.
// See 'https://scalameta.org/scalafmt', and '.scalafmt.conf' in the project root.
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.6")

// For transpilation into JavaScript.
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.21.0")

// For cross-compilation into JVM/JS from the same subproject.
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.3.2")
