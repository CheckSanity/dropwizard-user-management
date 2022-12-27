import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
}

group = "cc.sanity"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib-jdk8
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.22")

    // https://mvnrepository.com/artifact/io.dropwizard/dropwizard-core
    implementation("io.dropwizard:dropwizard-core:2.1.4")
    // https://mvnrepository.com/artifact/io.dropwizard/dropwizard-db
    implementation("io.dropwizard:dropwizard-db:2.1.4")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
