lazy val root = (project in file(".")).
  settings(
    name := "koffio-expression-example",
    version := "0.0.1",
    scalaVersion := "2.11.7"
  )
//add jitpack.io to resolvers
resolvers += "jitpack.io" at "https://jitpack.io"

//add github project as a dependency
//https://github.com/jedesah -> "com.github.jedesah" as a group id
//project name "computation-expressions" as an artifact id
//and commit id "5ef11fc97c" as a revision
libraryDependencies += "com.github.jedesah" % "computation-expressions" % "5ef11fc97c"