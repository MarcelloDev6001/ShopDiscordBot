plugins {
    id("java")
}

group = "com.shop.discordbot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JDA stuffs
    implementation("net.dv8tion:JDA:5.6.1")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    // other stuffs
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.github.oshi:oshi-core:6.6.5")

    // database
    implementation("com.google.firebase:firebase-admin:9.2.0")

    // IntelliJ Idea default libs
    implementation("org.mongodb:mongodb-driver-sync:4.0.1")
    implementation("org.slf4j:slf4j-jdk14:1.7.28")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}