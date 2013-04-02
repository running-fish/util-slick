import sbt._
import Keys._

object UtilSlickBuild extends Build {
  val sharedSettings = Seq(
    version := "0.2.0-SNAPSHOT",
    organization := "org.sazabi",
    scalaVersion := "2.10.1",
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-feature"
    ),
    resolvers ++= Seq(
      Resolver.url("My github releases", url("http://solar.github.com/ivy2/releases/"))(Resolver.ivyStylePatterns),
      "twitter" at "http://maven.twttr.com"
    ),
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2" % "1.14" % "test"
    )
  )

  lazy val all = Project(
    "util-slick-all",
    file("."),
    settings = Project.defaultSettings ++ Seq(
      scalaVersion := "2.10.1",
      publish := {},
      publishLocal := {}
    )
  ).aggregate(core, json, scalendar)


  lazy val core = Project(
    "util-slick-core",
    file("core"),
    settings = Project.defaultSettings ++ sharedSettings
  ).settings(
    name := "util-slick-core",
    libraryDependencies ++= Seq(
      "com.twitter" %% "util-core" % "6.2.4" % "compile",
      "com.typesafe.slick" %% "slick" % "1.0.0" % "compile"
    )
  )

  lazy val json = Project(
    "util-slick-json",
    file("json"),
    settings = Project.defaultSettings ++ sharedSettings
  ).settings(
    name := "util-slick-json",
    libraryDependencies ++= Seq(
      "org.sazabi" %% "util-json" % "0.6.0" % "compile"
    )
  ).dependsOn(core)

  lazy val scalendar = Project(
    "util-slick-scalendar",
    file("scalendar"),
    settings = Project.defaultSettings ++ sharedSettings
  ).settings(
    name := "util-slick-scalendar",
    libraryDependencies ++= Seq(
      "com.github.philcali" %% "scalendar" % "0.1.4" % "compile"
    )
  ).dependsOn(core)
}
