package io.github.trainb0y.tilerpg.terrain.generation

import io.github.trainb0y.tilerpg.terrain.TerrainHandler
import io.github.trainb0y.tilerpg.terrain.TilePosition
import io.github.trainb0y.tilerpg.terrain.chunk.Chunk
import io.github.trainb0y.tilerpg.terrain.noise.OpenSimplex2
import io.github.trainb0y.tilerpg.terrain.tile.Tile
import io.github.trainb0y.tilerpg.terrain.tile.TileData
import io.github.trainb0y.tilerpg.terrain.tile.TileLayer

object TerrainGenerator {

	/**
	 * Generates a chunk with [origin] as the origin
	 * @return the chunk
	 */
	fun generateChunk(origin: TilePosition): Chunk {
		val chunk = Chunk(TerrainHandler.chunkSize, origin)
		for (x in 0 until TerrainHandler.chunkSize) {
			val maxHeight = (OpenSimplex2.noise2(
				TerrainHandler.world!!.seed.toLong(),
				(x + origin.x).toDouble() / TerrainHandler.world!!.wavelength,
				0.0
			) * TerrainHandler.world!!.amplitude).toInt() + TerrainHandler.world!!.heightBonus
			for (y in 0 until (maxHeight - origin.y).coerceAtMost(TerrainHandler.chunkSize)) {
				if (y + 2 > maxHeight - origin.y) chunk.setRelativeTile(TilePosition(x, y), TileData(Tile.GRASS), TileLayer.BOTH)
				else if (y + 5 > maxHeight - origin.y) chunk.setRelativeTile(TilePosition(x, y), TileData(Tile.DIRT), TileLayer.BOTH)
				else chunk.setRelativeTile(TilePosition(x, y), TileData(Tile.STONE), TileLayer.BOTH)
			}
		}
		chunk.updatePhysicsShape()
		return chunk
	}
}
