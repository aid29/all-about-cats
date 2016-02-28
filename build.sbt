name := "all-about-cats"

scalaVersion := "2.11.7"

libraryDependencies ++=
  Seq(
    "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.2" withSources(),
    "com.github.finagle" %% "finch-core" % "0.10.0" % "test" withSources(),
    "org.typelevel" %% "cats" % "0.4.1" % "compile" withSources()
  )
