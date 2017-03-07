import sbt.Keys._
import sbtunidoc.Plugin.UnidocKeys._

lazy val root = (project in file("."))
  .aggregate(core, kkma, hannanum, twitter, komoran, eunjeon, kryo)
  .settings(unidocSettings: _*)
  .settings(
    publishArtifact := false,
    packagedArtifacts := Map.empty,
    publishLocal := {},
    publish := {},
    unidocProjectFilter in(ScalaUnidoc, unidoc) := inAnyProject -- inProjects(samples, model, server)
  ).settings(aggregate in update := true)

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers ++= Seq(
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)

sonatypeProfileName := "kr.bydelta"

lazy val core = (project in file("core"))
  .settings(projectWithConfig("core"): _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.log4s" %% "log4s" % "[1.3.4,)",
      "org.slf4j" % "slf4j-simple" % "[1.7.24,)"
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
      "org.openkoreantext" % "open-korean-text" % "[1.1,)"
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
    coverageEnabled := false,
    libraryDependencies ++= Seq(
      "com.tumblr" %% "colossus" % "0.8.3",
      "com.tumblr" %% "colossus-testkit" % "0.8.3" % "test",
      "com.typesafe.play" %% "play-json" % "[2.4,)"
    )
  )
  .dependsOn(core, kkma % "test")
lazy val kryo = (project in file("kryo"))
  .settings(projectWithConfig("kryo"))
  .settings(
    libraryDependencies += "com.twitter" %% "chill" % "[0.9.2,)"
  )
  .dependsOn(core, kkma % "test", twitter % "test")
lazy val model = (project in file("model"))
  .settings(projectWithConfig("model"))
  .settings(
    libraryDependencies += "cc.factorie" %% "factorie" % "[1.2,)"
  ).dependsOn(core)

def projectWithConfig(module: String) =
  Seq(
    organization := "kr.bydelta",
    name := s"koalaNLP-$module",
    version := "1.5.0",
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq("-target:jvm-1.7", "-deprecation", "-unchecked", "-feature"),
    scalacOptions in Test ++= Seq("-Yrangepos"),
    crossScalaVersions := Seq("2.11.8", "2.12.1"),
    publishArtifact in Test := false,
    coverageExcludedPackages := ".*\\.helper\\..*",
    test in assembly := {},
    libraryDependencies += "org.specs2" %% "specs2-core" % "[3.8,)" % "test",
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