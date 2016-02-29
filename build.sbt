name := "all-about-cats"

scalaVersion := "2.11.7"

libraryDependencies ++=
  Seq(
    "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.2" withSources(),
    "org.typelevel" %% "cats" % "0.4.1" % "compile" withSources(),
    "io.circe" %% "circe-core" % "0.3.0" % "compile" withSources(),
    "io.circe" %% "circe-generic" % "0.3.0" % "compile" withSources(),
    "io.circe" %% "circe-parser" % "0.3.0" % "compile" withSources(),
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "com.github.finagle" %% "finch-core" % "0.10.0" % "test" withSources(),
    "org.mockito" % "mockito-core" % "1.10.19"  % "test"
  )
