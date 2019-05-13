name := "SDQL"

version := "0.1"

scalaVersion := "2.12.8"

lazy val DQL = RootProject(uri("https://github.com/scalahub/DQL.git"))
lazy val CommonUtil = RootProject(uri("https://github.com/scalahub/CommonUtil.git"))
lazy val BetterDB = RootProject(uri("https://github.com/scalahub/BetterDB.git"))
lazy val CommonReflect = RootProject(uri("https://github.com/scalahub/CommonReflect.git"))
//lazy val DQL = RootProject(uri("../DQL"))
//lazy val CommonUtil = RootProject(uri("../CommonUtil"))
//lazy val BetterDB = RootProject(uri("../BetterDB"))
//lazy val CommonReflect = RootProject(uri("../CommonReflect"))

lazy val SolidityToDatalog = (project in file("SolidityToDatalog")).settings(
  resolvers += "Ethereumj Maven repository" at "https://dl.bintray.com/ethereum/maven/",
  libraryDependencies += "org.ethereum" % "ethereumj-core" % "1.12.0-RELEASE"
).dependsOn(DQL,CommonUtil)

lazy val root = (project in file(".")).settings(
  name := "SDQL",
  mainClass in (Compile, run) := Some("sdql.GenerateWebBoxHTML")
  //mainClass in (Test, run) := Some("sdql.Test")	
).dependsOn(
  SolidityToDatalog,CommonReflect,BetterDB
).enablePlugins(JettyPlugin)

