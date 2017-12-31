import sbt.Keys._

fork in Test := true
testForkedParallel in Test := true
concurrentRestrictions in Global := Seq(Tags.limit(Tags.Test, 1))

resolvers ++= Seq("releases").map(Resolver.sonatypeRepo)
resolvers += Resolver.typesafeRepo("releases")

sonatypeProfileName := "kr.bydelta"

/** Root project **/
lazy val root = (project in file("."))
  .enablePlugins(ScalaUnidocPlugin, JavaUnidocPlugin)
  .aggregate(core, kkma, hannanum, twitter, komoran, eunjeon, kryo, arirang, rhino, server)
  .settings(
    publishArtifact := false,
    packagedArtifacts := Map.empty,
    publishLocal := {},
    publish := {},
    unidocProjectFilter in(ScalaUnidoc, unidoc) := inAnyProject -- inProjects(samples, custom),
    unidocProjectFilter in(JavaUnidoc, unidoc) := inAnyProject -- inProjects(samples, custom)
  ).settings(aggregate in update := true)
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
/** 꼬꼬마 Project **/
lazy val kkma = (project in file("kkma"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("kkma"): _*)
  .settings(assemblySettings: _*)
  .dependsOn(core % "test->test;compile->compile")

/** 1. 라이브러리 포함 프로젝트 **/
/** 한나눔 프로젝트 **/
lazy val hannanum = (project in file("hannanum"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("hannanum"): _*)
  .settings(assemblySettings: _*)
  .dependsOn(core % "test->test;compile->compile")
/** 아리랑 프로젝트 **/
lazy val arirang = (project in file("arirang"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("arirang"): _*)
  .settings(assemblySettings: _*)
  .dependsOn(core % "test->test;compile->compile")
/** 라이노 프로젝트 **/
lazy val rhino = (project in file("rhino"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("rhino"): _*)
  .settings(assemblySettings: _*)
  .dependsOn(core % "test->test;compile->compile")
/** 은전한닢 프로젝트 **/
lazy val eunjeon = (project in file("eunjeon"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("eunjeon"): _*)
  .settings(
    libraryDependencies += (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 12)) => "org.bitbucket.eunjeon" %% "seunjeon" % "1.4.0" exclude("com.jsuereth", "sbt-pgp")
      case _ => "org.bitbucket.eunjeon" %% "seunjeon" % "1.3.1"
    })
  ).dependsOn(core % "test->test;compile->compile")

/** 2. 라이브러리 외부참조 프로젝트 **/
/** OpenKoreanText 프로젝트 **/
lazy val twitter = (project in file("twitter"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("twitter"): _*)
  .settings(
    libraryDependencies ++=
      (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 12)) => Seq("org.openkoreantext" % "open-korean-text" % "2.1.2")
        case _ => Seq("com.twitter.penguin" % "korean-text" % "4.4.4")
      })
  ).dependsOn(core % "test->test;compile->compile")
/** 코모란 프로젝트. **/
lazy val komoran = (project in file("komoran"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("komoran"): _*)
  .settings(
    resolvers += "jitpack" at "https://jitpack.io/",
    libraryDependencies ++= Seq(
      "com.github.shin285" % "KOMORAN" % "3.3.3"
    )
  ).dependsOn(core % "test->test;compile->compile")
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
            "com.typesafe.play" %% "play-json" % "2.6.3"
          )
        case _ =>
          Seq(
            "com.tumblr" %% "colossus" % "0.10.1",
            "com.tumblr" %% "colossus-testkit" % "0.10.1" % "test",
            "com.typesafe.play" %% "play-json" % "2.6.3"
          )
      })
  )
  .dependsOn(core, kkma % "test")

/** 3. 기타 도우미 프로젝트 **/
/** Kryo 직렬화 프로젝트 **/
lazy val kryo = (project in file("kryo"))
  .enablePlugins(GenJavadocPlugin)
  .settings(projectWithConfig("kryo"): _*)
  .settings(
    libraryDependencies += "com.twitter" %% "chill" % "0.9.2"
  )
  .dependsOn(core,
    kkma % "test", twitter % "test", komoran % "test", hannanum % "test", eunjeon % "test", arirang % "test")
/** 사용방법 샘플 프로젝트 **/
lazy val samples = (project in file("samples"))
  .settings(projectWithConfig("samples"): _*)
  .settings(
    libraryDependencies += "org.jsoup" % "jsoup" % "1.10.3"
  )
  .dependsOn(eunjeon, twitter, komoran, kkma, hannanum, server, arirang, rhino)
/** Customization **/
lazy val custom = (project in file("custom"))
  .settings(projectWithConfig("custom"): _*)
  .settings(
    resolvers += Resolver.sonatypeRepo("snapshots"),
    libraryDependencies ++= Seq(
      "org.apache.opennlp" % "opennlp-tools" % "1.8.2",
      "org.platanios" %% "tensorflow" % "0.1.0-SNAPSHOT",
      "org.platanios" %% "tensorflow" % "0.1.0-SNAPSHOT" % "test" classifier "linux-cpu-x86_64"
      //      "org.deeplearning4j" % "deeplearning4j-core" % "0.9.1"
    )
  ).dependsOn(core % "test->test;compile->compile")

/** 4. 모형 훈련 프로젝트 **/
/** Assembly Classifier 설정 **/
lazy val assemblySettings = Seq(
  assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false),
  artifact in(Compile, assembly) :=
    (artifact in(Compile, assembly)).value.withClassifier(Some("assembly")),
  addArtifact(artifact in(Compile, assembly), assembly))
/** 버전 **/
val VERSION = "1.9.2-SNAPSHOT"

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
    apiURL := Some(url("https://nearbydelta.github.io/KoalaNLP/api/scala/")),
    homepage := Some(url("http://nearbydelta.github.io/KoalaNLP")),
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