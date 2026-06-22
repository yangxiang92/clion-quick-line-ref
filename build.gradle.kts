plugins {
    kotlin("jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.7.1"
}

group = "com.shawnyang"
version = "0.1.0"

val clionLocalPath = providers.gradleProperty("clionLocalPath")
    .orElse(providers.environmentVariable("CLION_LOCAL_PATH"))
    .orNull
    ?: error("Please set -PclionLocalPath=<CLION_INSTALL_DIR> or CLION_LOCAL_PATH before building.")

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        local(clionLocalPath)
    }

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks {
    test {
        useJUnitPlatform()
    }

    wrapper {
        gradleVersion = "8.14.2"
    }
}

intellijPlatform {
    pluginConfiguration {
        name = "Quick Line Ref"
        description = "Copy real code line references to the clipboard from CLion."
        version = project.version.toString()

        ideaVersion {
            sinceBuild = "261"
            untilBuild = "261.*"
        }

        vendor {
            name = "ShawnYang"
        }
    }
}
