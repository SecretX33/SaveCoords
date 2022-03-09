plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.github.secretx33"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT") // Spigot API dependency
    compileOnly(fileTree("libs"))                               // Spigot server dependency
    compileOnly("org.jetbrains:annotations:22.0.0")
    compileOnly("org.checkerframework:checker-qual:3.18.1")
}

// Disables the normal jar task
tasks.jar { enabled = false }

// And enables shadowJar task
artifacts.archives(tasks.shadowJar)

tasks.shadowJar {
    archiveFileName.set("${rootProject.name}.jar")
    exclude("META-INF/**")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
}

tasks.processResources {
    outputs.upToDateWhen { false }
    val main_class = "${project.group}.${project.name.toLowerCase()}.${project.name}"
    expand("name" to project.name, "version" to project.version, "mainClass" to main_class)
}
