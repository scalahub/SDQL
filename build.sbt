name := "SDQL"

version := "0.1"

scalaVersion := "2.12.8"

lazy val DQL = RootProject(uri("https://github.com/scalahub/DQL.git"))
lazy val ScalaUtils = RootProject(uri("https://github.com/scalahub/ScalaUtils.git"))
lazy val BetterDB = RootProject(uri("https://github.com/scalahub/BetterDB.git"))
lazy val EasyMirror = RootProject(uri("https://github.com/scalahub/EasyMirror.git"))
//lazy val DQL = RootProject(uri("../DQL"))
//lazy val ScalaUtils = RootProject(uri("../ScalaUtils"))
//lazy val BetterDB = RootProject(uri("../BetterDB"))
//lazy val EasyMirror = RootProject(uri("../EasyMirror"))

lazy val SolidityToDatalog = (project in file("SolidityToDatalog")).settings(
  resolvers += "Ethereumj Maven repository" at "https://dl.bintray.com/ethereum/maven/",
  libraryDependencies += "org.ethereum" % "ethereumj-core" % "1.12.0-RELEASE"
).dependsOn(DQL,ScalaUtils)

lazy val root = (project in file(".")).settings(
  name := "SDQL",
  mainClass in (Compile, run) := Some("sdql.GenerateWebBoxHTML")
  //mainClass in (Test, run) := Some("sdql.Test")	
).dependsOn(
  SolidityToDatalog,EasyMirror,BetterDB
).enablePlugins(JettyPlugin)

