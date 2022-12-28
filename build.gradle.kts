val ktorVersion: String by project
val kotlinVersion: String by project
val kmongoVersion: String by project
val logbackVersion: String by project
val commonsCodecVersion: String by project
val koinKtorVersion: String by project
val koinAnnotationsVersion: String by project
val kotlinxCoroutinesJdk8Version: String by project
val twilioVersion: String by project
val libphonenumberVersion: String by project
val jmailVersion: String by project

plugins {
    kotlin("jvm") version "1.7.22"
    id("io.ktor.plugin") version "2.2.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"
    id("com.google.devtools.ksp") version "1.7.22-1.0.8"
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
    // Netty
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")

    // Ktor Core
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")

    // Ktor CORS
    implementation("io.ktor:ktor-server-cors:$ktorVersion")

    // Ktor Auth
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktorVersion")

    // Ktor Content Negotiation
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")

    // Ktor Call logging
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")

    // Kotlinx Serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")

    // Ktor Resources
    implementation("io.ktor:ktor-server-resources:$ktorVersion")

    // Ktor Websocket
    implementation("io.ktor:ktor-server-websockets-jvm:$ktorVersion")

    // Logback Classic Module
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // KMongo
    implementation("org.litote.kmongo:kmongo:$kmongoVersion")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongoVersion")

    // Apache Commons Codec
    implementation("commons-codec:commons-codec:$commonsCodecVersion")

    // Koin
    implementation("io.insert-koin:koin-ktor:$koinKtorVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinKtorVersion")
    implementation("io.insert-koin:koin-annotations:$koinAnnotationsVersion")
    ksp("io.insert-koin:koin-ksp-compiler:$koinAnnotationsVersion")

    // Kotlinx Coroutines JDK8
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinxCoroutinesJdk8Version")

    // Twilio
    implementation("com.twilio.sdk:twilio:$twilioVersion")

    // libphonenumber
    implementation("com.googlecode.libphonenumber:libphonenumber:$libphonenumberVersion")

    // Jmail
    implementation("com.sanctionco.jmail:jmail:$jmailVersion")

    // Testing
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}