import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "8.3.5"
    id("de.eldoria.plugin-yml.bukkit") version "0.6.0"
}

group = "eu.virtusdevelops"
version = "unspecified"

val minecraftVersion: String by rootProject

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")


    compileOnly("io.papermc.paper:paper-api:$minecraftVersion-R0.1-SNAPSHOT")

    implementation(libs.bundles.cloudEcosystem)
    implementation(project(":core"))
    implementation(project(":api"))
}

tasks.test {
    useJUnitPlatform()
}


kotlin {
    jvmToolchain(21)
}

val authorsList = listOf("VirtusDevelops")

bukkit {
    name = "MobChunkLimiter"
    main = "eu.virtusdevelops.mobchunklimiter.plugin.MobChunkLimiterPlugin"
    apiVersion = "1.21"
    foliaSupported = false
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    authors = authorsList
    softDepend = listOf("RoseStacker")
}


tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set("MobChunkLimiter")
        //dependsOn(":api:shadowJar")
    }

    build {
        dependsOn(shadowJar)
    }
}