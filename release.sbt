ThisBuild / name           := "Asset Loader"
ThisBuild / normalizedName := "asset-loader"
ThisBuild / description    := "A simple static asset loader for Scala web servers."
ThisBuild / homepage       := Some(url("https://github.com/SgtSwagrid/asset-loader"))

ThisBuild / organization         := "io.github.sgtswagrid"
ThisBuild / organizationName     := "SgtSwagrid"
ThisBuild / organizationHomepage := Some(url("https://github.com/SgtSwagrid"))

ThisBuild / scalaVersion  := "3.8.2"
ThisBuild / versionScheme := Some("strict")

ThisBuild / licenses :=
  List("MIT" -> url("https://opensource.org/licenses/MIT"))

ThisBuild / scmInfo := Some(ScmInfo(
  url("https://github.com/SgtSwagrid/asset-loader"),
  "scm:git@github.com:SgtSwagrid/asset-loader.git",
))

ThisBuild / developers := List(
  Developer(
    id = "SgtSwagrid",
    name = "Alec Dorrington",
    email = "alecdorrington@gmail.com",
    url = url("https://github.com/SgtSwagrid")
  )
)

// Target the Sonatype Central Portal (https://central.sonatype.com).
// Requires SONATYPE_USERNAME and SONATYPE_PASSWORD as GitHub secrets or environment variables.
ThisBuild / sonatypeCredentialHost := "central.sonatype.com"
ThisBuild / publishMavenStyle      := true
Global    / excludeLintKeys ++= Set(publishMavenStyle)
