import sbt.Keys._
import sbtunidoc.Plugin.UnidocKeys._

lazy val root = (project in file("."))
  .aggregate(core, kkma, hannanum, twitter, komoran, eunjeon, server, kryo)
  .settings(unidocSettings: _*)
  .settings(
    publishArtifact := false,
    packagedArtifacts := Map.empty,
    publishLocal := {},
    publish := {},
    unidocProjectFilter in(ScalaUnidoc, unidoc) := inAnyProject -- inProjects(samples)
  ).settings(aggregate in update := true)

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers ++= Seq(
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)

sonatypeProfileName := "kr.bydelta"

lazy val core = (project in file("core"))
  .settings(projectWithConfig("core"): _*)
lazy val kkma = (project in file("kkma"))
  .settings(projectWithConfig("kkma"): _*)
  .settings(
    assemblyOption in assembly := (assemblyOption in assembly).value.
      copy(includeScala = false),
    artifact in(Compile, assembly) := {
      val art = (artifact in(Compile, assembly)).value
      art.copy(`classifier` = Some("assembly"))
    },
    addArtifact(artifact in(Compile, assembly), assembly))
  .dependsOn(core)
lazy val hannanum = (project in file("hannanum"))
  .settings(projectWithConfig("hannanum"): _*)
  .settings(
    assemblyOption in assembly := (assemblyOption in assembly).value.
      copy(includeScala = false),
    artifact in(Compile, assembly) := {
      val art = (artifact in(Compile, assembly)).value
      art.copy(`classifier` = Some("assembly"))
    },
    addArtifact(artifact in(Compile, assembly), assembly))
  .dependsOn(core)
lazy val eunjeon = (project in file("eunjeon"))
  .settings(projectWithConfig("eunjeon"): _*)
  .settings(
    crossScalaVersions := Seq("2.11.8"),
    dependencyOverrides += "org.scala-lang" % "scala-reflect" % "2.11.8",
    libraryDependencies += "org.bitbucket.eunjeon" %% "seunjeon" % "1.1.+"
  ).dependsOn(core)
lazy val twitter = (project in file("twitter"))
  .settings(projectWithConfig("twitter"): _*)
  .settings(
    libraryDependencies += "com.twitter.penguin" % "korean-text" % "4.+"
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
  .dependsOn(eunjeon, twitter, komoran, kkma, hannanum, server)
lazy val server = (project in file("server"))
  .settings(projectWithConfig("server"): _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.tumblr" %% "colossus" % "0.8.+",
      "com.tumblr" %% "colossus-testkit" % "0.8.+" % "test",
      "com.typesafe.play" %% "play-json" % "2.5.+"
    )
  )
  .dependsOn(core, kkma % "test")
lazy val kryo = (project in file("kryo"))
  .settings(projectWithConfig("kryo"))
  .settings(
    libraryDependencies += "com.twitter" %% "chill" % "0.8.+"
  )
  .dependsOn(core, kkma % "test")

def projectWithConfig(module: String) =
  Seq(
    organization := "kr.bydelta",
    name := s"koalaNLP-$module",
    version := "1.4.0",
    scalaVersion := "2.11.8",
    scalacOptions += "-target:jvm-1.7",
    scalacOptions in Test ++= Seq("-Yrangepos"),
    publishArtifact in Test := false,
    test in assembly := {},
    libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.+" % "test",
    dependencyOverrides ++= Set(
      "org.scala-lang" % "scala-reflect" % "2.11.8"
    ),
    homepage := Some(url("http://nearbydelta.github.io/KoalaNLP")),
    parallelExecution in Test := false,
    publishTo <<= version { v: String ⇒
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    licenses := Seq("GPL v3" -> url("https://opensource.org/licenses/GPL-3.0/")),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ ⇒ false },
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