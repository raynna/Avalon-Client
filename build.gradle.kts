plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.rs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.runelite.net")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly(fileTree("libs"))
    compileOnly("org.projectlombok:lombok:1.18.16")
    annotationProcessor("org.projectlombok:lombok:1.18.16")

    implementation("net.runelite.pushingpixels:substance:8.0.02")
    implementation("net.runelite.pushingpixels:trident:1.5.00")

    implementation("com.google.guava:guava:32.0.1-jre")
    implementation("com.google.code.gson:gson:2.13.0")

    implementation("com.google.inject:guice:3.0")
    implementation("commons-io:commons-io:2.14.0")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("net.sf.jopt-simple:jopt-simple:4.7")
    implementation("org.apache.commons:commons-text:1.13.1")
}

tasks.test {
    useJUnitPlatform()
}