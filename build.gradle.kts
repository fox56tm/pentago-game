plugins {
    kotlin("jvm") version "1.9.24"
    id("org.jetbrains.compose") version "1.6.11"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}

group = "game.project"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(compose.desktop.currentOs)
    implementation("org.xerial:sqlite-jdbc:3.44.1.0")

    testImplementation(kotlin("test"))
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
compose.desktop {
    application {
        mainClass = "main.MainKt"
    }
}
tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
