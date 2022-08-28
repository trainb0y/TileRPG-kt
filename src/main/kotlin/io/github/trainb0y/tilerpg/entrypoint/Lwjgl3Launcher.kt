@file:JvmName("Lwjgl3Launcher")

package io.github.trainb0y.tilerpg.entrypoint

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

/** Launches the desktop (LWJGL3) application. */
fun main() {
	Lwjgl3Application(TileRPG(), Lwjgl3ApplicationConfiguration().apply {
		setTitle("TileRPG")
		setWindowedMode(640, 480)
		// setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
	})
}
