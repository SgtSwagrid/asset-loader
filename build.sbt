import IdeSettings.packagePrefix
import sbt._
import sbt.Keys._

// Basic details about this library.
ThisBuild / name        := "asset-loader"
ThisBuild / description := "A simple static asset loader for Scala web servers."

ThisBuild / homepage :=
  Some(url("https://github.com/SgtSwagrid/asset-loader"))

// The organisation who maintains this library.
ThisBuild / organization         := "io.github.sgtswagrid"
ThisBuild / organizationName     := "Alec Dorrington"
ThisBuild / organizationHomepage := Some(url("https://github.com/SgtSwagrid"))

// The URL for the GitHub repository of this library.
ThisBuild / scmInfo := Some(ScmInfo(
  url("https://github.com/SgtSwagrid/asset-loader"),
  "scm:git@github.com:SgtSwagrid/asset-loader.git",
))

// The licence under which this library is released.
ThisBuild / licenses :=
  List("MIT License" -> url("https://opensource.org/licenses/MIT"))

ThisBuild / developers := List(
  Developer(
    id = "SgtSwagrid",
    name = "Alec Dorrington",
    email = "alecdorrington@gmail.com",
    url = url("http://github.com/SgtSwagrid")
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
