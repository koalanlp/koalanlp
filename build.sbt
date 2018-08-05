import sbt.Keys._

/** Root project **/
lazy val root = (project in file("."))
  .enablePlugins(ScalaUnidocPlugin, JavaUnidocPlugin)
  .aggregate(core, kryo, server)
  .settings(
    publishArtifact := false,
    packagedArtifacts := Map.empty,
    publishLocal := {},
    publish := {},
    unidocProjectFilter in(ScalaUnidoc, unidoc) := inAnyProject -- inProjects(samples),
    unidocProjectFilter in(JavaUnidoc, unidoc) := inAnyProject -- inProjects(samples)
  ).settings(aggregate in update := true)

fork in Test := true
testForkedParallel in Test := true
concurrentRestrictions in Global := Seq(Tags.limit(Tags.Test, 1))

resolvers ++= Seq("releases").map(Resolver.sonatypeRepo)
resolvers += Resolver.typesafeRepo("releases")
resolvers += Resolver.mavenLocal

sonatypeProfileName := "kr.bydelta"
/** 분석기 서버 프로젝트 **/
lazy val server = (project in file("server"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("server"): _*)
  .settings(
    coverageEnabled := false,
    libraryDependencies ++=
      (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 11)) =>
          Seq(
            "com.tumblr" %% "colossus" % "0.9.1",
            "com.tumblr" %% "colossus-testkit" % "0.9.1" % "test",
            "kr.bydelta" %% "koalanlp-kkma" % "1.9.4" % "test" classifier "assembly",
            "org.json" % "json" % "20171018"
          )
        case _ =>
          Seq(
            "com.tumblr" %% "colossus" % "0.10.1",
            "com.tumblr" %% "colossus-testkit" % "0.10.1" % "test",
            "kr.bydelta" %% "koalanlp-kkma" % "1.9.4" % "test" classifier "assembly",
            "org.json" % "json" % "20171018"
          )
      })
  )
  .dependsOn(core)
/** Core Project (Data structure, Trait, etc.) **/
lazy val core = (project in file("core"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("core"): _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.log4s" %% "log4s" % "1.3.6",
      "org.slf4j" % "slf4j-simple" % "1.8.0-alpha2" % "test"
    )
  )
/** Kryo 직렬화 프로젝트 **/
lazy val kryo = (project in file("kryo"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("kryo"): _*)
  .settings(
    libraryDependencies ++= Seq("com.twitter" %% "chill" % "0.9.2",
      "kr.bydelta" %% "koalanlp-kkma" % "1.9.4" % "test" classifier "assembly",
      "kr.bydelta" %% "koalanlp-twitter" % "1.9.4" % "test",
      "kr.bydelta" %% "koalanlp-komoran" % "1.9.4" % "test",
      "kr.bydelta" %% "koalanlp-hannanum" % "1.9.4" % "test" classifier "assembly",
      "kr.bydelta" %% "koalanlp-eunjeon" % "1.9.4" % "test",
      "kr.bydelta" %% "koalanlp-arirang" % "1.9.4" % "test" classifier "assembly",
    )
  )
  .dependsOn(core)

/** 3. 기타 도우미 프로젝트 **/
/** 사용방법 샘플 프로젝트 **/
lazy val samples = (project in file("samples"))
  .settings(projectWithConfig("samples"): _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.jsoup" % "jsoup" % "1.10.3",
      "kr.bydelta" %% "koalanlp-kkma" % "latest.integration" classifier "assembly",
      "kr.bydelta" %% "koalanlp-twitter" % "latest.integration",
      "kr.bydelta" %% "koalanlp-komoran" % "latest.integration",
      "kr.bydelta" %% "koalanlp-hannanum" % "latest.integration" classifier "assembly",
      "kr.bydelta" %% "koalanlp-eunjeon" % "latest.integration",
      "kr.bydelta" %% "koalanlp-arirang" % "latest.integration" classifier "assembly",
      "kr.bydelta" %% "koalanlp-rhino" % "latest.integration" classifier "assembly",
    )
  )
  .dependsOn(server)
/** 버전 **/
val VERSION = "1.9.5-SNAPSHOT"

/** 공통 프로젝트 Configuration **/
def projectWithConfig(module: String) =
  Seq(
    organization := "kr.bydelta",
    name := s"koalaNLP-$module",
    version := VERSION,
    scalaVersion := "2.12.3",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
    scalacOptions in Test ++= Seq("-Yrangepos"),
    crossScalaVersions := Seq("2.11.11", "2.12.3"),
    publishArtifact in Test := false,
    coverageExcludedPackages := ".*\\.helper\\..*",
    test in assembly := {},
    libraryDependencies += "org.specs2" %% "specs2-core" % "3.9.5" % "test",
    apiURL := Some(url("https://koalanlp.github.io/KoalaNLP-core/api/scala/")),
    homepage := Some(url("http://koalanlp.github.io/KoalaNLP-core")),
    publishTo := version { v: String ⇒
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    }.value,
    licenses := Seq("MIT" -> url("https://tldrlegal.com/license/mit-license")),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ ⇒ false },
    pomExtra :=
      <scm>
        <url>git@github.com:koalanlp/KoalaNLP-core.git</url>
        <connection>scm:git:git@github.com:koalanlp/KoalaNLP-core.git</connection>
      </scm>
        <developers>
          <developer>
            <id>nearbydelta</id>
            <name>Bugeun Kim</name>
            <url>http://bydelta.kr</url>
          </developer>
        </developers>
  )