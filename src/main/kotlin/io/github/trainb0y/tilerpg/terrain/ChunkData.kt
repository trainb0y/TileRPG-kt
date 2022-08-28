package io.github.trainb0y.tilerpg.terrain

import io.github.trainb0y.tilerpg.terrain.tile.TileData
import kotlinx.serialization.Serializable

@Serializable
data class ChunkData(
	val foregroundTiles: MutableMap<TilePosition, TileData>,
	val backgroundTiles: MutableMap<TilePosition, TileData>
	)