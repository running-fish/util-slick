import AddSettings._

import Dependencies._

val files = Seq(file("../settings.sbt"),
  file("../publish.sbt"))

def proj(name: String) = Project(s"util-slick-$name", file(name))

lazy val root = project in(file(".")) autoSettings(
  userSettings, allPlugins, defaultSbtFiles
) settings(
  packagedArtifacts := Map.empty
) aggregate(
  bij,
  core,
  json,
  scal
)

lazy val bij = proj("bijection") autoSettings(
  userSettings, allPlugins, sbtFiles(files: _*)
) settings (
  libraryDependencies ++= bijection("core") :+ slick
)

lazy val core = proj("core") autoSettings(
  userSettings, allPlugins, sbtFiles(files: _*)
) settings (
  libraryDependencies ++= util("core") :+ slick
)

lazy val json = proj("json") autoSettings(
  userSettings, allPlugins, sbtFiles(files: _*)
) settings (
  libraryDependencies ++= json4s("core") :+ slick
)

lazy val scal = proj("scalendar") autoSettings(
  userSettings, allPlugins, sbtFiles(files: _*)
) settings (
  libraryDependencies ++= Seq(scalendar, slick)
)
