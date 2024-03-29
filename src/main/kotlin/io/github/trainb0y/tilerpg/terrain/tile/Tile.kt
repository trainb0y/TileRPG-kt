package io.github.trainb0y.tilerpg.terrain.tile

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.assets.toInternalFile

val atlas = TextureAtlas("assets/packed/tiles/tiles.atlas".toInternalFile())

enum class Tile(val id: String, val texture: TextureAtlas.AtlasRegion) {
	STONE("stone", atlas.findRegion("stone")),
	DIRT("dirt", atlas.findRegion("dirt")),
	GRASS("grass", atlas.findRegion("grass")),
	SAND("sand", atlas.findRegion("sand")),
	IRON_ORE("iron-ore", atlas.findRegion("iron-ore"))
}
