name := "hax"

version := "1.0.0"

scalaVersion := "2.10.0-RC1"

scalacOptions ++= Seq(
  "-deprecation",
  "-Xexperimental"
)

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "pircbot" % "pircbot" % "1.5.0",
  "com.typesafe" % "config" % "1.0.0",
  "com.typesafe" % "slick_2.10.0-M7" % "0.11.1",
  "org.joda" % "joda-convert" % "1.2",
  "joda-time" % "joda-time" % "2.1",
  "commons-io" % "commons-io" % "2.4"
)
