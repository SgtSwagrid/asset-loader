import IdeSettings.packagePrefix
import sbt._
import sbt.Keys._

lazy val `asset-loader` = project
  .in(file("core"))
  .settings(packagePrefix := "io.github.sgtswagrid.assetloader")

lazy val `asset-loader-tapir` = project
  .in(file("tapir"))
  .dependsOn(`asset-loader`)
  .settings(
    packagePrefix := "io.github.sgtswagrid.assetloader.tapir",
    libraryDependencies +=
      "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.13.13",
  )
