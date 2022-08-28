import org.gradle.jvm.toolchain.JavaLanguageVersion.of
import com.badlogic.gdx.tools.texturepacker.TexturePacker

val gdxVersion = "1.11.0"
val ktxVersion = "1.11.0-rc2"
val kotlinVersion = "1.7.10"

plugins {
	java
	application
	kotlin("jvm") version "1.7.10"
	kotlin("plugin.serialization") version "1.7.10"
	id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
	mavenCentral()
	maven(url = "https://s01.oss.sonatype.org")
	maven(url = "https://jitpack.io")
	mavenLocal()
}

buildscript {
	dependencies {
		classpath("com.badlogicgames.gdx:gdx-tools:1.11.0")
	}
}

java.sourceSets["main"].java.srcDir("assets")

dependencies {
	setOf("ktx-async", "ktx-app", "ktx-json", "ktx-assets", "ktx-log", "ktx-box2d", "ktx-graphics").forEach{
		api(group = "io.github.libktx", name = it, version = ktxVersion)
	}
	implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
	implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
	implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
	implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
	implementation("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
	implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
	implementation("com.badlogicgames.box2dlights:box2dlights:1.4")
	implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
}

application.mainClass.set("io.github.trainb0y.tilerpg.Lwjgl3Launcher")

java.toolchain.languageVersion.set(of(17))

tasks {
	jar { manifest.attributes("Main-Class" to "io.github.trainb0y.tilerpg.Lwjgl3Launcher") }
	shadowJar { archiveFileName.set("../TileRPG.jar") }
	build { dependsOn("shadowJar") }
	register("texturePacker") {
		TexturePacker.process(project.projectDir.toString() + "/assets/tiles/", project.projectDir.toString() + "/assets/packed/tiles/", "tiles")
	}
}
