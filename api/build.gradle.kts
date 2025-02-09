plugins {
    kotlin("jvm")
    id("java")
}

group = "eu.virtusdevelops"
version = "unspecified"

val minecraftVersion: String by rootProject


dependencies {
    compileOnly("io.papermc.paper:paper-api:$minecraftVersion-R0.1-SNAPSHOT")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}