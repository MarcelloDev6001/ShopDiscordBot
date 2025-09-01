plugins {
    id("java")
}

group = "com.shop.discordbot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:6.0.0-rc.3")
    implementation("org.mongodb:mongodb-driver-sync:4.0.1")
    implementation("org.slf4j:slf4j-jdk14:1.7.28")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}