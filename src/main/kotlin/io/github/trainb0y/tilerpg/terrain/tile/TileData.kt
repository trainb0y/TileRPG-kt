package io.github.trainb0y.tilerpg.terrain.tile

import kotlinx.serialization.Serializable

@Serializable
data class TileData(
	var type: Tile,
	var ignoreSunlight: Boolean = false
)
