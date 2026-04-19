import IdeSettings.packagePrefix
import sbt.*
import sbt.Keys.*
import sbtcrossproject.CrossProject
import sbtunidoc.BaseUnidocPlugin.autoImport.*
import sbtunidoc.ScalaUnidocPlugin

val stageReadmeDocs = taskKey[File]("Stages README files into a siteroot for Scaladoc.")

lazy val root = project
  .in(file("."))
  .aggregate(
    `asset-loader`,
    `asset-loader-common`.jvm,
    `asset-loader-common`.js,
    `asset-loader-tapir`,
    `asset-loader-tapir-common`.jvm,
    `asset-loader-tapir-common`.js,
  )
  .enablePlugins(ScalaUnidocPlugin)
  .settings(
    stageReadmeDocs := {
      val base    = (ThisBuild / baseDirectory).value
      val staging = target.value / "readme-docs"
      val excluded = Set("target", ".git", "project")
      val readmes = (base ** "README.md")
        .filter(f => excluded.forall(ex => !f.getPath.replace('\\', '/').contains(s"/$ex/")))
        .get
      IO.delete(staging)
      readmes.foreach(f => IO.copyFile(f, staging / f.relativeTo(base).get.getPath))
      staging
    },
    ScalaUnidoc / unidoc / unidocProjectFilter :=
      inAnyProject -- inProjects(`asset-loader-common`.js, `asset-loader-tapir-common`.js),
    ScalaUnidoc / unidoc / scalacOptions ++= Seq("-siteroot", stageReadmeDocs.value.getAbsolutePath),
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
      "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % "1.13.15",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % "1.13.15",
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
        "com.softwaremill.sttp.tapir" %%% "tapir-core" % "1.13.15",
    )
