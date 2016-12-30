val libVersion = "0.1"

lazy val finchVersion = "0.11.+"
lazy val finagleVersion = "6.40.0"
lazy val utilVersion = "6.39.0"
lazy val twitterServerVersion = "1.25.0"
lazy val circeVersion = "0.6.1"
lazy val catsVersion = "0.8.1"
lazy val catbirdVersion = "0.9.0"

lazy val commonDeps = Seq(
  "com.github.finagle" %% "finch-core" % finchVersion,
  "com.github.finagle" %% "finch-circe" % finchVersion,

  "io.circe" %% "circe-generic" % circeVersion,

  "com.twitter" %% "twitter-server" % twitterServerVersion
  //"com.twitter" %% "util-eval" % utilVersion
)

lazy val repos = Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Maven Central" at "https://repo1.maven.org/maven2/",
  "Twitter Repo" at "http://maven.twttr.com",
  "eaio.com" at "http://eaio.com/maven2"
)

lazy val commonSettings = Seq(
  version := libVersion,
  organization := "com.justinrmiller",
  scalaVersion := "2.11.8",
  test in assembly := {},
  libraryDependencies ++= commonDeps,
  fork in run := true,
  resolvers ++= repos
)

val myAssemblySettings = Seq(
  assemblyMergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("com", "twitter", xs @ _*) => MergeStrategy.last
    case n if n.startsWith("META-INF/MANIFEST.MF") => MergeStrategy.discard
    case _ => MergeStrategy.first
  }
  }
)

lazy val app = (project in file(".")).
  settings(commonSettings: _*).
  settings(myAssemblySettings: _*).
  settings(
    mainClass in assembly := Some("com.justinrmiller.finchexample.Main")
  )

artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.copy(`classifier` = Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)

