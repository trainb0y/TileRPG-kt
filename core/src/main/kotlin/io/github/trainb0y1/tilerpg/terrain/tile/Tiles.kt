package io.github.trainb0y1.tilerpg.terrain.tile

import com.badlogic.gdx.graphics.Texture
import ktx.assets.toInternalFile

object Tiles {
	val STONE = TileType("stone", Texture("tiles/stone.png".toInternalFile()))
}