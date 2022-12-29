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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.22")

    // https://mvnrepository.com/artifact/io.dropwizard/dropwizard-core
    implementation("io.dropwizard:dropwizard-core:2.1.4")
    // https://mvnrepository.com/artifact/io.dropwizard/dropwizard-db
    implementation("io.dropwizard:dropwizard-db:2.1.4")
    // https://mvnrepository.com/artifact/io.dropwizard/dropwizard-jackson
    implementation("io.dropwizard:dropwizard-jackson:2.1.4")
    // https://mvnrepository.com/artifact/io.dropwizard/dropwizard-jdbi3
    implementation("io.dropwizard:dropwizard-jdbi3:2.1.4")
    // https://mvnrepository.com/artifact/io.dropwizard/dropwizard-migrations
    implementation("io.dropwizard:dropwizard-migrations:2.1.4")

    // https://mvnrepository.com/artifact/org.kodein.di/kodein-di
    implementation("org.kodein.di:kodein-di:7.16.0")

    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.5.1")

    // https://mvnrepository.com/artifact/com.github.kittinunf.result/result
    implementation("com.github.kittinunf.result:result:5.3.0")

    // https://mvnrepository.com/artifact/com.smoketurner/dropwizard-swagger
    implementation("com.smoketurner:dropwizard-swagger:2.0.12-1")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
