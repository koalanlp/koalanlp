import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask
import org.gradle.api.plugins.ExtensionAware
import org.gradle.internal.impldep.junit.framework.Test
import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension
import org.junit.platform.gradle.plugin.JUnitPlatformPlugin

val ossrhUsername = project.property("ossrhUsername") as? String ?: "FOO"
val ossrhPassword = project.property("ossrhPassword") as? String ?: "BAR"
val archivesBaseName = project.property("archivesBaseName") as? String ?: "koalanlp-x"
val gitBaseName = project.property("gitBaseName") as? String ?: archivesBaseName

/**
 * Define Plugins
 */
buildscript {
    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.0")
    }
}

apply {
    plugin("org.junit.platform.gradle.plugin")
}

plugins {
    `build-scan`
    java
    maven
    signing
    jacoco
    kotlin("jvm") version "1.2.71"
    id("org.jetbrains.dokka") version "0.9.16"
}

/**
 * Define Dependencies
 */
repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven { url = uri("https://dl.bintray.com/spekframework/spek-dev") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // For test
    testCompile("org.jetbrains.spek:spek-api:1.1.5") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testRuntime("org.jetbrains.spek:spek-junit-platform-engine:1.1.5") {
        exclude(group = "org.junit.platform")
        exclude(group = "org.jetbrains.kotlin")
    }
    testRuntimeOnly(kotlin("reflect"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.30.2")
    testImplementation("org.amshove.kluent:kluent:1.42")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

/**
 * Build Scan
 */
buildScan {
    setLicenseAgreementUrl("https://gradle.com/terms-of-service")
    setLicenseAgree("yes")

    publishAlways()
}

tasks {

    /**
     * JavaDocs
     */
    val dokka by tasks.getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
    }

    val dokkaJar by tasks.creating(Jar::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        description = "Assembles Kotlin docs with Dokka"
        classifier = "javadoc"
        from(dokka)
    }

    val sourceJar by tasks.creating(Jar::class) {
        classifier = "sources"
        from(java.sourceSets["main"].allSource)
    }

    /**
     * Test options
     */
    configure<JUnitPlatformExtension> {
        filters {
            engines {
                include("spek")
            }
        }
    }

    jacoco {
        applyTo(getByName<JavaExec>("junitPlatformTest"))
        toolVersion = "0.8.2"
    }

    getByName<JavaExec>("junitPlatformTest") {
        withGroovyBuilder {
            "jacoco"{
                "destinationFile"(file("$buildDir/jacoco/junitExecutionData.exec"))
            }
        }
    }

    getByName<JacocoReport>("jacocoTestReport") {
        executionData = files("$buildDir/jacoco/junitExecutionData.exec")

        reports {
            xml.isEnabled = true
            html.isEnabled = true
        }
    }

    getByName("check").dependsOn("jacocoTestReport")

    /**
     * Publish options
     */
    artifacts {
        add("archives", dokkaJar)
        add("archives", sourceJar)
    }

    signing {
        sign(configurations["archives"])
    }

    getByName<Upload>("uploadArchives") {
        repositories {
            withConvention(MavenRepositoryHandlerConvention::class) {
                mavenDeployer {
                    withGroovyBuilder {
                        "beforeDeployment"{ signing.signPom(delegate as MavenDeployment) }

                        "repository"("url" to uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")) {
                            "authentication"("userName" to ossrhUsername, "password" to ossrhPassword)
                        }

                        "snapshotRepository"("url" to uri("https://oss.sonatype.org/content/repositories/snapshots/")) {
                            "authentication"("userName" to ossrhUsername, "password" to ossrhPassword)
                        }
                    }

                    pom.project {
                        withGroovyBuilder {
                            "parent"{
                                "groupId"(group)
                                "artifactId"(archivesBaseName)
                                "version"(version)
                            }

                            "packaging"("jar")
                            "url"("http://koalanlp.github.io/$gitBaseName")

                            "scm"{
                                "connection"("scm:git:git@github.com:koalanlp/$gitBaseName.git")
                                "developerConnection"("scm:git:git@github.com:koalanlp/$gitBaseName.git")
                                "url"("https://github.com/koalanlp/$gitBaseName")
                            }


                            "licenses"{
                                "license"{
                                    "name"("MIT License")
                                    "url"("https://tldrlegal.com/license/mit-license")
                                }
                            }

                            "developers"{
                                "developer"{
                                    "id"("nearbydelta")
                                    "name"("Bugeun Kim")
                                    "url"("http://bydelta.kr")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// extension for configuration
fun JUnitPlatformExtension.filters(setup: FiltersExtension.() -> Unit) {
    when (this) {
        is ExtensionAware -> extensions.getByType(FiltersExtension::class.java).setup()
        else -> throw Exception("${this::class} must be an instance of ExtensionAware")
    }
}

fun FiltersExtension.engines(setup: EnginesExtension.() -> Unit) {
    when (this) {
        is ExtensionAware -> extensions.getByType(EnginesExtension::class.java).setup()
        else -> throw Exception("${this::class} must be an instance of ExtensionAware")
    }
}