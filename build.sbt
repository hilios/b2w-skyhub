name := """b2w-skyhub"""

lazy val root = (project in file(".")).enablePlugins(PlayScala, BuildInfoPlugin, GitVersioning)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version),
    buildInfoPackage := "b2w.skyub"
  )

scalaVersion := "2.11.7"

git.formattedShaVersion := git.gitHeadCommit.value map(sha => s"${sha.substring(0, 8)}")

libraryDependencies ++= Seq(
  ws,
  filters,
  "com.sksamuel.scrimage" %% "scrimage-core" % "1.4.2",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.0.0",
  "org.mockito" % "mockito-core" % "2.7.22" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)
