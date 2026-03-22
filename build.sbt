import IdeSettings.packagePrefix
import sbt._
import sbt.Keys._

ThisBuild / description := "A simple static asset loader for Scala web servers."
ThisBuild / homepage := Some(url("https://github.com/SgtSwagrid/asset-loader"))

ThisBuild / organization         := "io.github.sgtswagrid"
ThisBuild / organizationName     := "Alec Dorrington"
ThisBuild / organizationHomepage := Some(url("https://github.com/SgtSwagrid"))

ThisBuild / scmInfo := Some(ScmInfo(
  url("https://github.com/SgtSwagrid/asset-loader"),
  "scm:git@github.com:SgtSwagrid/asset-loader.git",
))

ThisBuild / licenses :=
  List("MIT License" -> url("https://opensource.org/licenses/MIT"))

ThisBuild / developers := List(Developer(
  id = "SgtSwagrid",
  name = "Alec Dorrington",
  email = "alecdorrington@gmail.com",
  url = url("https://github.com/SgtSwagrid"),
))

ThisBuild / scalaVersion  := "3.8.2"
ThisBuild / versionScheme := Some("strict")

ThisBuild / sonatypeCredentialHost := "central.sonatype.com"
ThisBuild / publishMavenStyle      := true
Global / excludeLintKeys ++= Set(publishMavenStyle)

lazy val core = (project in file("core")).settings(
  name          := "asset-loader",
  packagePrefix := "io.github.sgtswagrid.assetloader",
)

lazy val tapir = (project in file("tapir"))
  .dependsOn(core)
  .settings(
    name          := "asset-loader-tapir",
    packagePrefix := "io.github.sgtswagrid.assetloader.tapir",
    libraryDependencies ++=
      Seq("com.softwaremill.sttp.tapir" %% "tapir-core" % "1.13.13"),
  )
