// import io.gitlab.arturbosch.detekt.Detekt
// import org.jetbrains.changelog.closure
// import org.jetbrains.changelog.markdownToHTML

// import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Java support
    id("java")
    // Kotlin support
//    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "0.7.3"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "1.1.2"
    // detekt linter - read more: https://detekt.github.io/detekt/gradle.html
//    id("io.gitlab.arturbosch.detekt") version "1.15.0"
    // ktlint linter - read more: https://github.com/JLLeitschuh/ktlint-gradle
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
}

// Import variables from gradle.properties file
val pluginGroup: String by project
// `pluginName_` variable ends with `_` because of the collision with Kotlin magic getter in the `intellij` closure.
// Read more about the issue: https://github.com/JetBrains/intellij-platform-plugin-template/issues/29
val pluginName_: String by project
val pluginVersion: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project
val pluginVerifierIdeVersions: String by project

val platformType: String by project
val platformVersion: String by project
val platformPlugins: String by project
val platformDownloadSources: String by project

group = pluginGroup
version = pluginVersion

// Configure project's dependencies
repositories {
    mavenCentral()
    jcenter()
}
dependencies {
//    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.15.0")
    implementation("com.google.code.gson:gson:2.7")
// https://mvnrepository.com/artifact/org.everit.json/org.everit.json.schema
    implementation("org.everit.json:org.everit.json.schema:1.5.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0'")
    implementation("com.networknt:json-schema-validator:1.0.46")
    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j-impl
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
}

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName = pluginName_
//    version = platformVersion
    type = platformType
    downloadSources = platformDownloadSources.toBoolean()
    updateSinceUntilBuild = true
    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    setPlugins(*platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty).toTypedArray())
}

// Configure detekt plugin.
// Read more: https://detekt.github.io/detekt/kotlindsl.html
// detekt {
//    config = files("./detekt-config.yml")
//    buildUponDefaultConfig = true
//
//    reports {
//        html.enabled = false
//        xml.enabled = false
//        txt.enabled = false
//    }
// }

tasks {
    // Set the compatibility versions to 1.8
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
//    withType<KotlinCompile> {
//        kotlinOptions.jvmTarget = "1.8"
//    }
//
//    withType<Detekt> {
//        jvmTarget = "1.8"
//    }

    patchPluginXml {
        version(pluginVersion)
        sinceBuild(pluginSinceBuild)
        untilBuild(null)

//        Extract the <!--Plugin description--> section from README.md and provide for the plugin's manifest
//        pluginDescription(
//                closure {
//                    File("./README.md").readText().lines().run {
//                        val start = "<!-- Plugin description -->"
//                        val end = "<!-- Plugin description end -->"
//
//                        if (!containsAll(listOf(start, end))) {
//                            throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
//                        }
//                        subList(indexOf(start) + 1, indexOf(end))
//                    }.joinToString("\n").run { markdownToHTML(this) }
//                }
//        )
//
//        // Get the latest available change notes from the changelog file
//        changeNotes(
//                closure {
//                    changelog.getLatest().toHTML()
//                }
//        )
    }

    runPluginVerifier {
        ideVersions(pluginVerifierIdeVersions)
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token(System.getenv("PUBLISH_TOKEN"))
        // pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://jetbrains.org/intellij/sdk/docs/tutorials/build_system/deployment.html#specifying-a-release-channel
        channels(pluginVersion.split('-').getOrElse(1) { "default" }.split('.').first())
    }
}
