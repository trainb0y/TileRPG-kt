package io.github.trainb0y.tilerpg.terrain

import io.github.trainb0y.tilerpg.terrain.noise.OpenSimplex2
import io.github.trainb0y.tilerpg.terrain.tile.Tile
import io.github.trainb0y.tilerpg.terrain.tile.TileData
import io.github.trainb0y.tilerpg.terrain.tile.TileLayer

class TerrainGenerator(val world: WorldData) {

	/**
	 * Generates a chunk with [origin] as the origin
	 * @return the chunk
	 */
	fun generateChunk(origin: TilePosition): Chunk {
		val chunk = Chunk(TerrainHandler.chunkSize, origin)
		generateBaseTerrain(chunk)
		generateOres(chunk)
		carveCaves(chunk)
		chunk.updatePhysicsShape()
		return chunk
	}

	private fun generateBaseTerrain(chunk: Chunk) {
		for (x in 0 until TerrainHandler.chunkSize) {

			// calculate terrain height at this x
			val maxHeight = (OpenSimplex2.noise2(
				world.seed.toLong() + 43,
				(x + chunk.origin.x).toDouble() / world.wavelength,
				0.0
			) * world.amplitude).toInt() + world.heightBonus

			for (y in 0 until (maxHeight - chunk.origin.y).coerceAtMost(TerrainHandler.chunkSize)) {

				// determine material
				if (y + 2 > maxHeight - chunk.origin.y) chunk.setRelativeTile(
					TilePosition(x, y),
					TileData(Tile.GRASS, false),
					TileLayer.BOTH
				)
				else if (y + 5 > maxHeight - chunk.origin.y) chunk.setRelativeTile(
					TilePosition(x, y),
					TileData(Tile.DIRT, false),
					TileLayer.BOTH
				)
				else chunk.setRelativeTile(TilePosition(x, y), TileData(Tile.STONE, false), TileLayer.BOTH)
			}
		}
	}

	private fun generateOres(chunk: Chunk) {
		forEachXY { x, y ->
			if (chunk.getRelativeTile(TilePosition(x, y), TileLayer.FOREGROUND)?.type != Tile.STONE) return@forEachXY
			if (OpenSimplex2.noise2(
					world.seed.toLong() + 1434,
					(x + chunk.origin.x.toDouble()) / world.oreSize,
					(y + chunk.origin.y.toDouble()) / world.oreSize
				) < world.oreCutoff
			) chunk.setRelativeTile(TilePosition(x, y), TileData(Tile.IRON_ORE, false), TileLayer.BOTH)
		}
	}

	private fun carveCaves(chunk: Chunk) {
		for (x in 0 until TerrainHandler.chunkSize) {
			// calculate terrain height at this x
			val maxHeight = (OpenSimplex2.noise2(
				world.seed.toLong() + 12,
				(x + chunk.origin.x).toDouble() / world.wavelength,
				0.0
			) * world.amplitude).toInt()

			for (y in 0 until (maxHeight - chunk.origin.y).coerceAtMost(TerrainHandler.chunkSize)) {
				if (OpenSimplex2.noise2(
						world.seed.toLong() + 1234,
						(x + chunk.origin.x.toDouble()) / world.caveSize,
						(y + chunk.origin.y.toDouble()) / world.caveSize
					) < world.caveCutoff
				) chunk.setRelativeTile(TilePosition(x, y), null, TileLayer.FOREGROUND)
			}
		}
	}

	/**
	 * Call [op] for every x and y in 0 until chunk size
	 */
	private fun forEachXY(op: (Int, Int) -> Unit) {
		for (x in 0 until TerrainHandler.chunkSize) {
			for (y in 0 until TerrainHandler.chunkSize) {
				op(x, y)
			}
		}
	}
}
