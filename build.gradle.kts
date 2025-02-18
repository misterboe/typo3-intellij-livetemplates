import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.intellij") version "1.17.3"
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.1.0")
    testImplementation("junit:junit:4.13.2")
}

intellij {
    version.set(properties("platformVersion").get())
    type.set(properties("platformType").get())
    plugins.set(properties("platformPlugins").get().split(',').map(String::trim).filter(String::isNotEmpty))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = properties("javaVersion").get()
        targetCompatibility = properties("javaVersion").get()
    }
    
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = properties("javaVersion").get()
            languageVersion = "1.9"
            apiVersion = "1.9"
        }
    }

    test {
        useJUnit()
    }

    buildSearchableOptions {
        enabled = false
    }

    patchPluginXml {
        version.set(properties("pluginVersion").get())
        sinceBuild.set(properties("pluginSinceBuild").get())
        untilBuild.set(properties("pluginUntilBuild").get())
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

fun properties(key: String) = providers.gradleProperty(key)

