ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "tp1",
    idePackagePrefix := Some("fr.uge.ia")
  )

libraryDependencies += "org.apache.jena" % "jena-core" % "4.7.0"