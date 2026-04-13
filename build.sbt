import IdeSettings.packagePrefix
import sbt.*
import sbt.Keys.*
import sbtcrossproject.CrossProject

lazy val `asset-loader` = project
  .in(file("core"))
  .dependsOn(`asset-loader-common`.jvm)
  .settings(packagePrefix := "io.github.sgtswagrid.assetloader")

lazy val `asset-loader-common`: CrossProject =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("common"))
    .settings(packagePrefix := "io.github.sgtswagrid.assetloader")

lazy val `asset-loader-tapir` = project
  .in(file("tapir"))
  .dependsOn(
    `asset-loader`,
    `asset-loader-tapir-common`.jvm,
  )
  .settings(packagePrefix := "io.github.sgtswagrid.assetloader.tapir")

lazy val `asset-loader-tapir-common`: CrossProject =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("tapir-common"))
    .dependsOn(`asset-loader-common`)
    .settings(
      packagePrefix := "io.github.sgtswagrid.assetloader.tapir",
      libraryDependencies +=
        "com.softwaremill.sttp.tapir" %%% "tapir-core" % "1.13.15",
    )
