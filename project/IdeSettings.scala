import sbt._
import sbt.Keys._
import sbtide.Keys._

/** Configuration for IntelliJ via SBT. */
object IdeSettings extends AutoPlugin {

  override def trigger = allRequirements

  /** Setting key for defining an implicit package prefix. */
  lazy val packagePrefix =
    settingKey[String]("For defining an implicit package prefix.")

  override lazy val buildSettings = Seq(
    packagePrefix := "",

    // Exclude build output and other metadata from IDE indexing and project view:
    ideExcludedDirectories := Seq(
      // Binary output directories:
      file("target"),
      file("project") / "target",

      // Build tools:
      file(".metals"),
      file(".bsp"),
      file(".bloop"),

      // IDEs:
      file(".idea"),
      file(".vscode"),
    ),
  )

  override lazy val projectSettings = Seq(
    // Apply package prefix to IDE settings for both main and test sources:
    idePackagePrefix := Some(packagePrefix.value).filter(_.nonEmpty),
    ideBasePackages  := Seq(packagePrefix.value).filter(_.nonEmpty),
  )

  override lazy val globalSettings = Seq(
    // Prevent a warning about unused setting keys:
    excludeLintKeys ++= Set(
      packagePrefix,
      idePackagePrefix,
      ideBasePackages,
      ideExcludedDirectories,
    ),
  )
}
