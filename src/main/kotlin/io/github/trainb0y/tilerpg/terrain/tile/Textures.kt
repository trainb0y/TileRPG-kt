package io.github.trainb0y.tilerpg.terrain.tile

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion

import ktx.assets.toInternalFile

object Textures {
	val atlas = TextureAtlas("assets/packed/tiles/tiles.atlas".toInternalFile())
	val tileTextures = mutableMapOf<String, AtlasRegion>( // findRegion is slow, so save the output
		"stone" to atlas.findRegion("stone")
	)
}
