import org.gradle.jvm.toolchain.JavaLanguageVersion.of

val gdxVersion = "1.11.0"
val ktxVersion = "1.11.0-rc2"
val kotlinVersion = "1.7.10"

plugins {
	java
	application
	kotlin("jvm") version "1.7.10"
	id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
	mavenCentral()
	maven(url = "https://s01.oss.sonatype.org")
	maven(url = "https://jitpack.io")
	mavenLocal()
}

java.sourceSets["main"].java.srcDir("assets")

dependencies {
	setOf("ktx-async", "ktx-app", "ktx-json", "ktx-assets", "ktx-log", "ktx-box2d", "ktx-graphics").forEach{
		api(group = "io.github.libktx", name = it, version = ktxVersion)
	}
	implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
	implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
	implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
	implementation("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
	implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
	implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

application.mainClass.set("io.github.trainb0y.tilerpg.Lwjgl3Launcher")

tasks.jar { manifest.attributes("Main-Class" to "io.github.trainb0y.tilerpg.Lwjgl3Launcher") }

java.toolchain.languageVersion.set(of(17))

tasks {
	shadowJar { archiveFileName.set("../TileRPG.jar") }
	build { dependsOn("shadowJar") }
}
