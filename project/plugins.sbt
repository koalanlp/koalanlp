logLevel := Level.Warn

resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.3.3")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "latest.integration")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "latest.integration")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "latest.integration")