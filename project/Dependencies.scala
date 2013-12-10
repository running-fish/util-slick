import sbt._

object Versions {
  val bijection = "0.6.0"
  val json4s = "3.2.6"
  val scalendar = "0.1.4"
  val slick = "2.0.0-M3"
  val util = "6.8.1"
}

object Dependencies {
  def bijection(names: String*) = names map { name =>
    "com.twitter" %% s"bijection-$name" % Versions.bijection % "compile"
  }

  def util(names: String*) = names map {
    case "zk" =>
      ("com.twitter" %% "util-zk" % Versions.util % "compile")
        .exclude("com.sun.jdmk", "jmxtools")
        .exclude("com.sun.jmx", "jmxri")
        .exclude("javax.jms", "jms")
    case name => "com.twitter" %% s"util-$name" % Versions.util % "compile"
  }

  def json4s(names: String*) = names map { name =>
    "org.json4s" %% s"json4s-$name" % Versions.json4s % "compile"
  }

  val scalendar =
    "com.github.philcali" %% "scalendar" % Versions.scalendar % "compile"

  val slick = "com.typesafe.slick" %% "slick" % Versions.slick % "compile"
}
