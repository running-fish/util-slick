import sbt._
import Keys._

import com.typesafe.sbt.SbtPgp.PgpKeys._

object UtilSlickBuild extends Build {
  val sharedSettings = Seq(
    version := "0.2.0-SNAPSHOT",
    organization := "org.sazabi",
    scalaVersion := "2.10.2",
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-feature"
    ),
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots")
    ),
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2" % "1.14.1-SNAPSHOT" % "test"
    ),
    useGpg := true,
    publishMavenStyle := true,
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    pomExtra := (
      <url>https://github.com/solar/util-slick</url>
      <licenses>
        <license>
          <name>Apache 2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.txt"</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:solar/util-slick.git</url>
        <connection>scm:git:git@github.com:solar/util-slick.git</connection>
      </scm>
      <developers>
        <developer>
          <id>solar</id>
          <name>Shinpei Okamura</name>
          <url>https://github.com/solar</url>
        </developer>
      </developers>)
  )

  lazy val all = Project(
    "util-slick-all",
    file("."),
    settings = Project.defaultSettings ++ sharedSettings ++ Seq(
      publish := {},
      publishLocal := {},
      publishSigned := {}
    )
  ).aggregate(bijection, core, json, scalendar)

  lazy val bijection = Project(
    "util-slick-bijection",
    file("bijection"),
    settings = Project.defaultSettings ++ sharedSettings
  ).settings(
    name := "util-slick-bijection",
    libraryDependencies ++= Seq(
      "com.twitter" %% "bijection-core" % "0.4.0" % "compile"
    )
  ).dependsOn(core)

  lazy val core = Project(
    "util-slick-core",
    file("core"),
    settings = Project.defaultSettings ++ sharedSettings
  ).settings(
    name := "util-slick-core",
    libraryDependencies ++= Seq(
      "com.twitter" %% "util-core" % "6.3.5" % "compile",
      "com.typesafe.slick" %% "slick" % "1.0.1" % "compile"
    )
  )

  lazy val json = Project(
    "util-slick-json",
    file("json"),
    settings = Project.defaultSettings ++ sharedSettings
  ).settings(
    name := "util-slick-json",
    libraryDependencies ++= Seq(
      "org.sazabi" %% "util-json" % "0.8.2" % "compile"
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
