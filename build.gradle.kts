val ktor_version: String by project
val kotlin_version: String by project
val kmongo_version: String by project
val logback_version: String by project
val commons_codec_version: String by project
val koin_ktor_version: String by project
val koin_annotations_version: String by project

plugins {
    kotlin("jvm") version "1.7.22"
    id("io.ktor.plugin") version "2.2.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"
    id("com.google.devtools.ksp") version "1.6.21-1.0.6"
}

group = "hoang.myapp"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

dependencies {
    // Ktor Core
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")

    // Netty
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")

    // Ktor Auth
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")

    // Ktor Content Negotiation
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")

    // Ktor Call logging
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")

    // Kotlinx Serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")

    // Ktor Resources
    implementation("io.ktor:ktor-server-resources:$ktor_version")

    // Ktor Websocket
    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")

    // Logback Classic Module
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // KMongo
    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")

    // Apache Commons Codec
    implementation("commons-codec:commons-codec:$commons_codec_version")

    // Koin
    implementation("io.insert-koin:koin-ktor:$koin_ktor_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_ktor_version")
    implementation("io.insert-koin:koin-annotations:$koin_annotations_version")
    ksp("io.insert-koin:koin-ksp-compiler:$koin_annotations_version")

    // Testing
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}