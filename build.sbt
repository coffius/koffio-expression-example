lazy val root = (project in file(".")).
  settings(
    name := "koffio-expression-example",
    version := "0.0.1",
    scalaVersion := "2.11.7"
  )

resolvers += "jitpack.io" at "https://jitpack.io"

libraryDependencies += "com.github.jedesah" % "computation-expressions" % "5ef11fc97c"