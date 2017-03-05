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
concurrentRestrictions in Global := Seq(
  Tags.limit(Tags.CPU, 2),
  Tags.limit(Tags.Network, 10),
  Tags.limit(Tags.Test, 1),
  Tags.limitAll(15)
)

sonatypeProfileName := "kr.bydelta"

lazy val core = (project in file("core"))
  .settings(projectWithConfig("core"): _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.log4s" %% "log4s" % "latest.integration",
      "org.slf4j" % "slf4j-simple" % "latest.integration"
    )
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
    addArtifact(artifact in(Compile, assembly), assembly))
  .dependsOn(core % "test->test;compile->compile")
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
  .dependsOn(core % "test->test;compile->compile")
lazy val eunjeon = (project in file("eunjeon"))
  .settings(projectWithConfig("eunjeon"): _*)
  .settings(
    libraryDependencies += "org.bitbucket.eunjeon" %% "seunjeon" % "[1.1.0,)"
  ).dependsOn(core % "test->test;compile->compile")
lazy val twitter = (project in file("twitter"))
  .settings(projectWithConfig("twitter"): _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.openkoreantext" % "open-korean-text" % "latest.integration"
    )
  ).dependsOn(core % "test->test;compile->compile")
lazy val komoran = (project in file("komoran"))
  .settings(projectWithConfig("komoran"): _*)
  .settings(
    assemblyOption in assembly := (assemblyOption in assembly).value.
      copy(includeScala = false),
    artifact in(Compile, assembly) := {
      val art = (artifact in(Compile, assembly)).value
      art.copy(`classifier` = Some("assembly"))
    },
    addArtifact(artifact in(Compile, assembly), assembly))
  .dependsOn(core % "test->test;compile->compile")
lazy val samples = (project in file("samples"))
  .settings(projectWithConfig("samples"): _*)
  .dependsOn(eunjeon, twitter, komoran, kkma, hannanum, server)
lazy val server = (project in file("server"))
  .settings(projectWithConfig("server"): _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.tumblr" %% "colossus" % "0.8.3",
      "com.tumblr" %% "colossus-testkit" % "0.8.3" % "test",
      "com.typesafe.play" %% "play-json" % "latest.integration"
    )
  )
  .dependsOn(core, kkma % "test")
lazy val kryo = (project in file("kryo"))
  .settings(projectWithConfig("kryo"))
  .settings(
    libraryDependencies += "com.twitter" %% "chill" % "latest.integration"
  )
  .dependsOn(core, kkma % "test", twitter % "test")
lazy val model = (project in file("model"))
  .settings(projectWithConfig("model"))
  .settings(
    libraryDependencies += "cc.factorie" %% "factorie" % "latest.integration"
  ).dependsOn(core)

def projectWithConfig(module: String) =
  Seq(
    organization := "kr.bydelta",
    name := s"koalaNLP-$module",
    version := "1.5.0",
    scalaVersion := "2.11.8",
    scalacOptions += "-target:jvm-1.7",
    scalacOptions in Test ++= Seq("-Yrangepos"),
    crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.1"),
    publishArtifact in Test := false,
    coverageExcludedPackages := "kr\\.bydelta\\.koala\\..*\\.helper\\..*",
    test in assembly := {},
    libraryDependencies += "org.specs2" %% "specs2-core" % "latest.integration" % "test",
    dependencyOverrides ++= Set(
      "org.scala-lang.modules" %% "scala-xml" % "1.0.5"
    ),
    homepage := Some(url("http://nearbydelta.github.io/KoalaNLP")),
    parallelExecution in Test := false,
    publishTo := version { v: String ⇒
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    }.value,
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