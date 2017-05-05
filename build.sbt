name := """b2w-skyhub"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  ws,
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.0.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

