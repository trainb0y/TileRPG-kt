@file:JvmName("Lwjgl3Launcher")

package io.github.trainb0y1.tilerpg.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.tools.texturepacker.TexturePacker
import io.github.trainb0y1.tilerpg.TileRPG

/** Launches the desktop (LWJGL3) application. */
fun main() {
    val settings = TexturePacker.Settings()
    settings.maxHeight = 512 // TODO: a more permanent solution for texture packing, it shouldn't be done every run
    settings.maxWidth = 512

    TexturePacker.process(settings, "../assets/","../assets/packed/","packed")
    Lwjgl3Application(TileRPG(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("TileRPG")
        setWindowedMode(640, 480)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
