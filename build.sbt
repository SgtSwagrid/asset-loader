import IdeSettings.packagePrefix
import sbt.*
import sbt.Keys.*
import sbtunidoc.BaseUnidocPlugin.autoImport.*
import sbtunidoc.ScalaUnidocPlugin

val scala3 = "3.9.0"

ThisBuild / scalaVersion := scala3

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaUnidocPlugin)
  .aggregate(
    (`asset-loader-common`.projectRefs ++
      `asset-loader-tapir-common`.projectRefs ++
      Seq[ProjectReference](`asset-loader`, `asset-loader-tapir`)) *,
  )
  .settings(
    // Scaladoc is aggregated from the JVM side alone, as the JS side would
    // only document the shared sources a second time:
    ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(
      `asset-loader`,
      `asset-loader-common`.jvm(scala3),
      `asset-loader-tapir`,
      `asset-loader-tapir-common`.jvm(scala3),
    ),
    ScalaUnidoc / unidoc / scalacOptions ++= Seq("-project", "Asset Loader"),
  )

lazy val `asset-loader` = project
  .in(file("core/server"))
  .dependsOn(`asset-loader-common`.jvm(scala3))
  .settings(
    name          := "asset-loader",
    packagePrefix := "com.alecdorrington.assetloader",
  )

/**
  * Cross-compiled for JVM and JS. Its sources sit in their packages'
  * directories rather than under a package prefix, as IntelliJ never gives one
  * to the sources the two share.
  */
lazy val `asset-loader-common` = projectMatrix
  .in(file("core/common"))
  .settings(
    name := "asset-loader-common",

    // sbt 2.0.x resolves a matrix's sources against the working directory,
    // so they are pinned to this build's own base:
    sourceDirectory :=
      (ThisBuild / baseDirectory).value / "core" / "common" / "src",
  )
  .jvmPlatform(scalaVersions = Seq(scala3))
  .jsPlatform(scalaVersions = Seq(scala3))

lazy val `asset-loader-tapir` = project
  .in(file("tapir/server"))
  .dependsOn(
    `asset-loader`,
    `asset-loader-tapir-common`.jvm(scala3),
  )
  .settings(
    name          := "asset-loader-tapir",
    packagePrefix := "com.alecdorrington.assetloader.tapir",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % "1.14.0",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % "1.14.0",
    ),
  )

/** Cross-compiled for JVM and JS, its sources placed as the common core's. */
lazy val `asset-loader-tapir-common` = projectMatrix
  .in(file("tapir/common"))
  .dependsOn(`asset-loader-common`)
  .settings(
    name := "asset-loader-tapir-common",

    // sbt 2.0.x resolves a matrix's sources against the working directory,
    // so they are pinned to this build's own base:
    sourceDirectory :=
      (ThisBuild / baseDirectory).value / "tapir" / "common" / "src",
    libraryDependencies +=
      "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.14.0",
  )
  .jvmPlatform(scalaVersions = Seq(scala3))
  .jsPlatform(scalaVersions = Seq(scala3))
