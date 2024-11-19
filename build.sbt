ThisBuild / scalaVersion := "3.3.1"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "ascii-art",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.15" % Test
    )
  )
