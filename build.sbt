import sbt.Keys._
import sbtunidoc.Plugin.UnidocKeys._

lazy val root = (project in file("."))
  .aggregate(core_2_11, core_2_10,
    kkma_2_11, kkma_2_10, hannanum_2_11, hannanum_2_10, eunjeon,
    twitter_2_11, twitter_2_10, komoran_2_11, komoran_2_10)
  .settings(unidocSettings: _*)
  .settings(
    publishArtifact := false,
    packagedArtifacts := Map.empty,
    publishLocal := {},
    publish := {},
    unidocProjectFilter in(ScalaUnidoc, unidoc) := inAnyProject -- inProjects(samples)
  ).settings(aggregate in update := true)
lazy val core = (project in file("core"))
  .settings(projectWithConfig("core"): _*)
  .cross
lazy val core_2_10 = core("2.10.4")
lazy val core_2_11 = core("2.11.8")

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
    addArtifact(artifact in(Compile, assembly), assembly)).cross
lazy val kkma_2_10 = kkma("2.10.4").dependsOn(core_2_10)
lazy val kkma_2_11 = kkma("2.11.8").dependsOn(core_2_11)
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
  .cross
lazy val hannanum_2_10 = hannanum("2.10.4").dependsOn(core_2_10)
lazy val hannanum_2_11 = hannanum("2.11.8").dependsOn(core_2_11)
lazy val eunjeon = (project in file("eunjeon"))
  .settings(projectWithConfig("eunjeon"): _*)
  .settings(
    dependencyOverrides += "org.scala-lang" % "scala-reflect" % "2.11.8",
    libraryDependencies += "org.bitbucket.eunjeon" %% "seunjeon" % "1.1.0"
  ).dependsOn(core_2_11)
lazy val twitter = (project in file("twitter"))
  .settings(projectWithConfig("twitter"): _*)
  .settings(
    libraryDependencies += "com.twitter.penguin" % "korean-text" % "4.4"
  ).cross
lazy val twitter_2_10 = twitter("2.10.4").dependsOn(core_2_10)
lazy val twitter_2_11 = twitter("2.11.8").dependsOn(core_2_11)
lazy val komoran = (project in file("komoran"))
  .settings(projectWithConfig("komoran"): _*)
  .settings(
    assemblyOption in assembly := (assemblyOption in assembly).value.
      copy(includeScala = false),
    artifact in(Compile, assembly) := {
      val art = (artifact in(Compile, assembly)).value
      art.copy(`classifier` = Some("assembly"))
    },
    addArtifact(artifact in(Compile, assembly), assembly)).cross
lazy val komoran_2_10 = komoran("2.10.4").dependsOn(core_2_10)
lazy val komoran_2_11 = komoran("2.11.8").dependsOn(core_2_11)
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
lazy val ver = "1.0.2"

def projectWithConfig(module: String) =
  Seq(
    organization := "kr.bydelta",
    name := s"koalaNLP-$module",
    version := ver,
    scalaVersion := "2.11.8",
    scalacOptions += "-target:jvm-1.7",
    scalacOptions in Test ++= Seq("-Yrangepos"),
    publishArtifact in Test := false,
    test in assembly := {},
    libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.4" % "test",
    dependencyOverrides ++= Set(
      "org.scala-lang" % "scala-reflect" % "2.11.8",
      "org.scala-lang.modules" %% "scala-xml" % "1.0.5"
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