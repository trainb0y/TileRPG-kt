package io.github.trainb0y.tilerpg.terrain.chunk

import com.badlogic.gdx.graphics.OrthographicCamera
import io.github.trainb0y.tilerpg.terrain.TerrainHandler
import io.github.trainb0y.tilerpg.terrain.TerrainHandler.saveChunkToFile
import io.github.trainb0y.tilerpg.terrain.TilePosition
import kotlin.math.roundToInt

object ChunkLoader {
	/**
	 * Attempts to load all chunks visible to [camera] if they aren't already loaded. If they don't exist, create them.
	 * Loads [buffer] hidden chunks in all directions.
	 * Saves all non-visible chunks to files.
	 */
	fun loadVisibleChunks(camera: OrthographicCamera, buffer: Int = 0) {
		val bufferSize = TerrainHandler.chunkSize * buffer

		val minPos = TilePosition(
			// Subtracting chunkSize so that visible chunks with non-visible origins are still loaded
			((camera.position.x - (camera.viewportWidth / 2f)) - bufferSize).roundToInt() - TerrainHandler.chunkSize, // this
			((camera.position.y - (camera.viewportHeight / 2f)) - bufferSize).roundToInt() - TerrainHandler.chunkSize // feels
		)
		val maxPos = TilePosition(
			(camera.position.x + (camera.viewportWidth / 2f)).roundToInt() + bufferSize, // very
			(camera.position.y + (camera.viewportHeight / 2f)).roundToInt() + bufferSize // bad
		)

		// Save non-visible chunks
		val chunksToSave = mutableSetOf<Chunk>()
		TerrainHandler.chunks.forEach { (pos, chunk) ->
			if (pos.clampXY(minPos, maxPos) != pos) {
				chunksToSave.add(chunk)
			}
		}

		// todo: do this async
		chunksToSave.forEach{saveChunkToFile(it)}

		// Load visible chunks
		val newChunks = mutableMapOf<TilePosition, Chunk>()
		for (x in minPos.x..maxPos.x step TerrainHandler.chunkSize) {
			for (y in minPos.y..maxPos.y step TerrainHandler.chunkSize) {
				// All of these should be loaded
				newChunks[TilePosition(x, y).chunkOrigin] =
					TerrainHandler.getChunk(TilePosition(x, y), true)!! // Force will create/load it for us
			}
		}
		TerrainHandler.chunks = newChunks
	}
}
