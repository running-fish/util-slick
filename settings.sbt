version := "0.11.0-SNAPSHOT"

organization := "org.sazabi"

scalaVersion := "2.10.3"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"))

libraryDependencies += "org.scalatest" %% "scalatest" % "2.0" % "test"
