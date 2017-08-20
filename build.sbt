import sbt.Keys._

fork in Test := true
testForkedParallel in Test := true
concurrentRestrictions in Global := Seq(Tags.limit(Tags.Test, 1))

lazy val root = (project in file("."))
  .enablePlugins(ScalaUnidocPlugin, JavaUnidocPlugin)
  .aggregate(core, kkma, hannanum, twitter, komoran, eunjeon, kryo, arirang)
  .settings(
    publishArtifact := false,
    packagedArtifacts := Map.empty,
    publishLocal := {},
    publish := {},
    unidocProjectFilter in(ScalaUnidoc, unidoc) := inAnyProject -- inProjects(samples, server, custom),
    unidocProjectFilter in(JavaUnidoc, unidoc) := inAnyProject -- inProjects(samples, server, custom)
  ).settings(aggregate in update := true)
lazy val core = (project in file("core"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("core"): _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.log4s" %% "log4s" % "1.3.6",
      "org.slf4j" % "slf4j-simple" % "1.8.0-alpha2" % "test"
    )
  )

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers ++= Seq(
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)

sonatypeProfileName := "kr.bydelta"

lazy val kkma = (project in file("kkma"))
  .enablePlugins(GenJavadocPlugin)
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
  .enablePlugins(GenJavadocPlugin)
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
lazy val arirang = (project in file("arirang"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("arirang"): _*)
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
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("eunjeon"): _*)
  .settings(
    libraryDependencies += "org.bitbucket.eunjeon" %% "seunjeon" % "1.3.0"
  ).dependsOn(core % "test->test;compile->compile")
lazy val twitter = (project in file("twitter"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("twitter"): _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.openkoreantext" % "open-korean-text" % "2.1.0"
    )
  ).dependsOn(core % "test->test;compile->compile")
lazy val komoran = (project in file("komoran"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("komoran"): _*)
  .settings(
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies ++= Seq(
      "com.github.shin285" % "KOMORAN" % "3.2.1.5"
    )
  ).dependsOn(core % "test->test;compile->compile")


lazy val samples = (project in file("samples"))
  .settings(projectWithConfig("samples"): _*)
  .dependsOn(eunjeon, twitter, komoran, kkma, hannanum, server, arirang)


lazy val server = (project in file("server"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("server"): _*)
  .settings(
    coverageEnabled := false,
    libraryDependencies ++= Seq(
      "com.tumblr" %% "colossus" % "[0.9,)",
      "com.tumblr" %% "colossus-testkit" % "[0.9,)" % "test",
      "com.typesafe.play" %% "play-json" % "[2.6,)"
    )
  )
  .dependsOn(core, kkma % "test")
lazy val kryo = (project in file("kryo"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("kryo"))
  .settings(
    libraryDependencies += "com.twitter" %% "chill" % "0.9.2"
  )
  .dependsOn(core,
    kkma % "test", twitter % "test", komoran % "test", hannanum % "test", eunjeon % "test", arirang % "test")
lazy val custom = (project in file("custom"))
  .settings(projectWithConfig("custom"))
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.opennlp" % "opennlp-tools" % "[1.8,)"
      //      "org.deeplearning4j" % "deeplearning4j-core" % "[0.9,)"
    )
  ).dependsOn(core % "test->test;compile->compile")

val VERSION = "1.6.1"

def projectWithConfig(module: String) =
  Seq(
    organization := "kr.bydelta",
    name := s"koalaNLP-$module",
    version := VERSION,
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
    scalacOptions in Test ++= Seq("-Yrangepos"),
    crossScalaVersions := Seq("2.11.8", "2.12.1"),
    publishArtifact in Test := false,
    coverageExcludedPackages := ".*\\.helper\\..*",
    test in assembly := {},
    libraryDependencies += "org.specs2" %% "specs2-core" % "[3.8,)" % "test",
    apiURL := Some(url("https://nearbydelta.github.io/KoalaNLP/api/scala/")),
    homepage := Some(url("http://nearbydelta.github.io/KoalaNLP")),
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
