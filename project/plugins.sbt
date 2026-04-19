// SBT plugins that are specific to this library.

// For transpilation into JavaScript.
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.21.0")

// For cross-compilation into JVM/JS from the same subproject.
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.3.2")
