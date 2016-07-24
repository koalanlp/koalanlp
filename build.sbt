import sbt.Keys._

lazy val root = (project in file("."))
  .aggregate(core, kkma, hannanum, eunjeon, twitter, komoran)
  .settings(
    publishArtifact := false,
    publishLocal := {},
    publish := {}
  ).settings(aggregate in update := true)
lazy val core = (project in file("core")).settings(projectWithConfig("core"): _*)

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers ++= Seq(
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)
lazy val kkma = (project in file("kkma"))
  .settings(projectWithConfig("kkma"): _*)
  .settings(
    assemblyOption in assembly := (assemblyOption in assembly).value.
      copy(includeScala = false),
    artifact in(Compile, assembly) := {
      val art = (artifact in(Compile, assembly)).value
      art.copy(`classifier` = Some("assembly"))
    },
    addArtifact(artifact in(Compile, assembly), assembly)).dependsOn(core)
lazy val hannanum = (project in file("hannanum"))
  .settings(projectWithConfig("hannanum"): _*)
  .settings(
    assemblyOption in assembly := (assemblyOption in assembly).value.
      copy(includeScala = false),
    artifact in(Compile, assembly) := {
      val art = (artifact in(Compile, assembly)).value
      art.copy(`classifier` = Some("assembly"))
    },
    addArtifact(artifact in(Compile, assembly), assembly)).dependsOn(core)
lazy val eunjeon = (project in file("eunjeon"))
  .settings(projectWithConfig("eunjeon"): _*)
  .settings(
    dependencyOverrides += "org.scala-lang" % "scala-reflect" % "2.11.8",
    libraryDependencies += "org.bitbucket.eunjeon" %% "seunjeon" % "1.1.0"
  ).dependsOn(core)
lazy val twitter = (project in file("twitter"))
  .settings(projectWithConfig("twitter"): _*)
  .settings(
    libraryDependencies += "com.twitter.penguin" % "korean-text" % "4.4"
  ).dependsOn(core)
lazy val komoran = (project in file("komoran"))
  .settings(projectWithConfig("komoran"): _*)
  .settings(
    assemblyOption in assembly := (assemblyOption in assembly).value.
      copy(includeScala = false),
    artifact in(Compile, assembly) := {
      val art = (artifact in(Compile, assembly)).value
      art.copy(`classifier` = Some("assembly"))
    },
    addArtifact(artifact in(Compile, assembly), assembly)).dependsOn(core)
lazy val samples = (project in file("samples"))
  .settings(projectWithConfig("samples"): _*)
  .settings(
    libraryDependencies ++= Seq(
      "kr.bydelta" %% "koalanlp-core" % ver,
      "kr.bydelta" %% "koalanlp-eunjeon" % ver,
      "kr.bydelta" %% "koalanlp-hannanum" % ver classifier "assembly",
      "kr.bydelta" %% "koalanlp-twitter" % ver,
      "kr.bydelta" %% "koalanlp-kkma" % ver classifier "assembly",
      ("kr.bydelta" %% "koalanlp-komoran" % ver) classifier "assembly"
    )
  )
lazy val ver = "0.9"

def projectWithConfig(module: String) =
  Seq(
    organization := "kr.bydelta",
    name := s"koalaNLP-$module",
    version := ver,
    scalaVersion := "2.11.8",
    homepage := Some(url("http://nearbydelta.github.io/KoalaNLP")),
    publishTo <<= version { v: String ⇒
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { x ⇒ false },
    pomExtra :=
      <scm>
        <url>git@github.com:nearbydelta/KoalaNLP.git</url>
        <connection>scm:git:git@github.com:nearbydelta/KoalaNLP.git</connection>
      </scm>
        <developers>
          <developer>
            <id>nearbydelta</id>
            <name>Bugeun Kim</name>
            <url>http://bydelta.kr</url>
          </developer>
        </developers>
  )