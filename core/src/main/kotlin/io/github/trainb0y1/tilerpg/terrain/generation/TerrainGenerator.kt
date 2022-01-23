package io.github.trainb0y1.tilerpg.terrain.generation

import io.github.trainb0y1.tilerpg.terrain.chunk.Chunk

object TerrainGenerator {

	fun generateChunk(chunk: Chunk) {
		TODO()
	}

	/* // Initial testing worldgen
	fun generateTerrain(width: Int) {
		for (x in 0..width) {
			val maxHeight =
				(OpenSimplex2.noise2(seed, x.toDouble() / wavelength, 0.0) * amplitude).toInt() + heightBonus
			for (y in 0..maxHeight) {
				TerrainHandler.setTile(vector2FromInt(x,y), Tiles.STONE)
			}
		}
	}
	 */
}