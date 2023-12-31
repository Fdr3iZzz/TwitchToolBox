plugins {
    kotlin("jvm") version "1.9.20"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")

}

application.mainClass.set("MainKt")
group = "de.Franz3"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

tasks {
    shadowJar {
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.danilopianini:khttp:1.3.1")
    implementation ("com.github.twitch4j:twitch4j:1.18.0")
    implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.4.12")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}


