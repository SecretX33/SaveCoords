plugins {
    java
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.github.secretx33"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "codemc-repo"
        url = uri("https://repo.codemc.org/repository/maven-public/")
    }
    maven {
        name = "gradle-repo"
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT") // Spigot API dependency
    compileOnly(fileTree("libs"))      // Spigot server dependency
}

// Disables the normal jar task
tasks.jar { enabled = false }

// And enables shadowJar task
artifacts.archives(tasks.shadowJar)

tasks.shadowJar {
    archiveFileName.set(rootProject.name + ".jar")
    exclude("META-INF/**")
}

tasks.withType<JavaCompile> { options.encoding = "UTF-8" }

tasks.processResources {
    expand("name" to rootProject.name, "version" to project.version)
}