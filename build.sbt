import IdeSettings.packagePrefix
import sbt._
import sbt.Keys._

// Basic details about this library.
ThisBuild / name        := "scala-library-template"
ThisBuild / description := "A reusable template for Scala libraries."

ThisBuild / homepage :=
  Some(url("https://github.com/SgtSwagrid/scala-library-template"))

// The organisation who maintains this library.
ThisBuild / organization         := "org.example"
ThisBuild / organizationName     := "Example"
ThisBuild / organizationHomepage := Some(url("https://example.com"))

// The URL for the GitHub repository of this library.
ThisBuild / scmInfo := Some(ScmInfo(
  url("https://github.com/SgtSwagrid/scala-library-template"),
  "scm:git@github.com:SgtSwagrid/scala-library-template.git",
))

// The licence under which this library is released.
ThisBuild / licenses :=
  List("MIT License" -> url("https://opensource.org/licenses/MIT"))

ThisBuild / developers := List(
  Developer(
    id = "example",
    name = "Example Example",
    email = "example@example.com",
    url = url("http://github.com/example")
  )
)

ThisBuild / scalaVersion := "3.8.2"

// Target the Sonatype Central Portal (https://central.sonatype.com).
// Set SONATYPE_USERNAME and SONATYPE_PASSWORD as GitHub secrets.
ThisBuild / sonatypeCredentialHost := "central.sonatype.com"
ThisBuild / publishMavenStyle      := true
Global / excludeLintKeys ++= Set(name, publishMavenStyle)

lazy val root = (project in file(".")).settings(
  packagePrefix                          := (ThisBuild / organization).value,
  libraryDependencies += "org.scalameta" %% "munit" % "1.2.4" % Test,
)
