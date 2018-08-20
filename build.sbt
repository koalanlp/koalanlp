import sbt.Keys._

val VERSION = "2.0.0-SNAPSHOT"
val module = "core"

enablePlugins(ScalaUnidocPlugin, JavaUnidocPlugin)

organization := "kr.bydelta"
sonatypeProfileName := "kr.bydelta"

name := s"koalaNLP-$module"

version := VERSION

scalaVersion := "2.12.3"

fork in Test := true
testForkedParallel in Test := true
concurrentRestrictions in Global := Seq(Tags.limit(Tags.Test, 1))

resolvers ++= Seq("releases").map(Resolver.sonatypeRepo)
resolvers += Resolver.typesafeRepo("releases")
resolvers += Resolver.mavenLocal
resolvers += "jitpack" at "https://jitpack.io/"

unmanagedResourceDirectories in Compile += (baseDirectory.value / "src" / "main" / (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((maj, min)) => s"resources-$maj.$min"
  case _ => "resources"
}))

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

scalacOptions in Test ++= Seq("-Yrangepos")

crossScalaVersions := Seq("2.11.11", "2.12.3")

publishArtifact in Test := false

coverageExcludedPackages := ".*\\.helper\\..*"

test in assembly := {}

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.9.5" % "test",
  "org.log4s" %% "log4s" % "1.3.6",
  "org.slf4j" % "slf4j-simple" % "1.8.0-alpha2" % "test"
)

apiURL := Some(url("https://koalanlp.github.io/KoalaNLP-core/api/scala/"))

homepage := Some(url("http://koalanlp.github.io/KoalaNLP-core"))

publishTo := version { v: String ⇒
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}.value

licenses := Seq("MIT" -> url("https://tldrlegal.com/license/mit-license"))

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ ⇒ false }

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
