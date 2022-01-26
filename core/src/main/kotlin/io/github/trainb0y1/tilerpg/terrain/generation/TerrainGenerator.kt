package io.github.trainb0y1.tilerpg.terrain.generation

import io.github.trainb0y1.tilerpg.terrain.Position
import io.github.trainb0y1.tilerpg.terrain.TerrainHandler
import io.github.trainb0y1.tilerpg.terrain.chunk.Chunk
import io.github.trainb0y1.tilerpg.terrain.noise.OpenSimplex2
import io.github.trainb0y1.tilerpg.terrain.tile.TileData
import io.github.trainb0y1.tilerpg.terrain.tile.Tiles

object TerrainGenerator {

	/**
	 * Generates a chunk with [origin] as the origin
	 * @return the chunk
	 */
	fun generateChunk(origin: Position): Chunk {
		val chunk = Chunk(TerrainHandler.chunkSize, origin)
		for (x in 0..TerrainHandler.chunkSize) {
			val maxHeight = (OpenSimplex2.noise2(
				TerrainHandler.world!!.seed.toLong(),
				(x+origin.x).toDouble() / TerrainHandler.world!!.wavelength,
				0.0
			) * TerrainHandler.world!!.amplitude).toInt() + TerrainHandler.world!!.heightBonus
			for (y in 0..(maxHeight-origin.y).coerceAtMost(TerrainHandler.chunkSize.toFloat()).toInt()) {
				chunk.setRelativeTile(Position(x,y), TileData(Tiles.STONE))
			}
		}
		return chunk
	}
}