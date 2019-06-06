name := "SDQL"

version := "0.1"

scalaVersion := "2.12.8"

lazy val DQL = RootProject(uri("https://github.com/scalahub/DQL.git"))
lazy val ScalaUtils = RootProject(uri("https://github.com/scalahub/ScalaUtils.git"))
lazy val BetterDB = RootProject(uri("https://github.com/scalahub/BetterDB.git"))
lazy val EasyWeb = RootProject(uri("https://github.com/scalahub/EasyWeb.git"))
//lazy val DQL = RootProject(uri("../DQL"))
//lazy val ScalaUtils = RootProject(uri("../ScalaUtils"))
//lazy val BetterDB = RootProject(uri("../BetterDB"))
//lazy val EasyWeb = RootProject(uri("../EasyWeb"))

lazy val SolidityToDatalog = (project in file("SolidityToDatalog")).settings(
  resolvers += "Ethereumj Maven repository" at "https://dl.bintray.com/ethereum/maven/",
  libraryDependencies += "org.ethereum" % "ethereumj-core" % "1.12.0-RELEASE"
).dependsOn(DQL,ScalaUtils)

//SDQLDemo
lazy val root = (project in file(".")).settings(
  name := "SDQL",
  mainClass in (Compile, run) := Some("sdql.SDQLDemo")
  //mainClass in (Compile, run) := Some("sdql.GenerateWebBoxHTML")
  //mainClass in (Test, run) := Some("sdql.Test")	
).dependsOn(
  SolidityToDatalog,EasyWeb,BetterDB
).enablePlugins(JettyPlugin)

