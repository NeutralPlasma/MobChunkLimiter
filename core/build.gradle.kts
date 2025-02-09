plugins {
    kotlin("jvm")
    id("java")
}

group = "eu.virtusdevelops"
version = "unspecified"

val minecraftVersion: String by rootProject

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("dev.rosewood:rosestacker:1.5.31")
    compileOnly("io.papermc.paper:paper-api:$minecraftVersion-R0.1-SNAPSHOT")
    implementation(libs.bundles.kotlinxEcosystem)

    implementation(project(":api"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}