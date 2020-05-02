name := "SDQL"

version := "0.1"

lazy val ScalaUtils = RootProject(uri("https://github.com/scalahub/ScalaUtils.git"))
lazy val DQL = RootProject(uri("https://github.com/scalahub/DQL.git"))
lazy val EasyWeb = RootProject(uri("https://github.com/scalahub/EasyWeb.git"))
lazy val ScalaDB = RootProject(uri("https://github.com/scalahub/ScalaDB.git"))

lazy val SolidityToDatalog = (project in file("SolidityToDatalog")).settings(
  resolvers += "Ethereumj Maven repository" at "https://dl.bintray.com/ethereum/maven/",
  libraryDependencies += "org.ethereum" % "ethereumj-core" % "1.12.0-RELEASE",
  libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.4"
).dependsOn(DQL, ScalaUtils)

lazy val root = (project in file(".")).settings(
  name := "SDQL",
  mainClass in (Compile, run) := Some("sdql.SDQLDemo")
).dependsOn(
  SolidityToDatalog, EasyWeb, ScalaDB
).enablePlugins(JettyPlugin)

