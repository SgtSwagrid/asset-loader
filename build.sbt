import IdeSettings.packagePrefix
import sbt.*
import sbt.Keys.*
import sbtcrossproject.CrossProject
import sbtunidoc.BaseUnidocPlugin.autoImport.*
import sbtunidoc.ScalaUnidocPlugin

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaUnidocPlugin)
  .aggregate(
    `asset-loader`,
    `asset-loader-common`.jvm,
    `asset-loader-common`.js,
    `asset-loader-tapir`,
    `asset-loader-tapir-common`.jvm,
    `asset-loader-tapir-common`.js,
  )
  .settings(
    ScalaUnidoc / unidoc / scalacOptions ++= Seq("-project", "Asset Loader"),
  )

lazy val `asset-loader` = project
  .in(file("core/server"))
  .dependsOn(`asset-loader-common`.jvm)
  .settings(packagePrefix := "com.alecdorrington.assetloader")

lazy val `asset-loader-common`: CrossProject =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("core/common"))
    .settings(packagePrefix := "com.alecdorrington.assetloader")

lazy val `asset-loader-tapir` = project
  .in(file("tapir/server"))
  .dependsOn(
    `asset-loader`,
    `asset-loader-tapir-common`.jvm,
  )
  .settings(
    packagePrefix := "com.alecdorrington.assetloader.tapir",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % "1.13.25",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % "1.13.25",
    ),
  )

lazy val `asset-loader-tapir-common`: CrossProject =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("tapir/common"))
    .dependsOn(`asset-loader-common`)
    .settings(
      packagePrefix := "com.alecdorrington.assetloader.tapir",
      libraryDependencies +=
        "com.softwaremill.sttp.tapir" %%% "tapir-core" % "1.13.25",
    )
