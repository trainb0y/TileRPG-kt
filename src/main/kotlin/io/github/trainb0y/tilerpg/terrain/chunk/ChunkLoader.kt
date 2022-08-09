package io.github.trainb0y.tilerpg.terrain.chunk

import com.badlogic.gdx.graphics.OrthographicCamera
import io.github.trainb0y.tilerpg.terrain.Position
import io.github.trainb0y.tilerpg.terrain.TerrainHandler
import ktx.log.info


object ChunkLoader {
	/**
	 * Attempts to load all chunks visible to [camera] if they aren't already loaded. If they don't exist, create them.
	 * Loads [buffer] hidden chunks in all directions.
	 * Saves all non-visible chunks to files.
	 */
	@Suppress("UnnecessaryVariable")
	fun loadVisibleChunks(camera: OrthographicCamera, buffer: Int = 0) {
		val bufferSize = TerrainHandler.chunkSize * buffer

		val minPos = Position(
			// Subtracting chunkSize so that visible chunks with non-visible origins are still loaded
			((camera.position.x - (camera.viewportWidth / 2)) - bufferSize) - TerrainHandler.chunkSize, // this
			((camera.position.y - (camera.viewportHeight / 2)) - bufferSize) - TerrainHandler.chunkSize // feels
		).roundToInt()
		val maxPos = Position(
			(camera.position.x + (camera.viewportWidth / 2)) + bufferSize, // very
			(camera.position.y + (camera.viewportHeight / 2)) + bufferSize // bad
		).roundToInt()

		// Save non-visible chunks
		val chunksToSave = mutableSetOf<Chunk>()
		TerrainHandler.chunks.forEach { (pos, chunk) ->
			if (pos.clampXY(minPos, maxPos) != pos) {
				chunksToSave.add(chunk)
			}
		}
		saveChunks(chunksToSave)

		// Load visible chunks
		var newChunks = mutableMapOf<Position, Chunk>()
		for (x in minPos.x.toInt()..maxPos.x.toInt() step TerrainHandler.chunkSize) {
			for (y in minPos.y.toInt()..maxPos.y.toInt() step TerrainHandler.chunkSize) {
				// All of these should be loaded
				newChunks[Position(x,y)] = TerrainHandler.getChunk(Position(x, y), true)!! // Force will create/load it for us
			}
		}
		TerrainHandler.chunks = newChunks

		info {
			"Camera position: (${camera.position.x}, ${camera.position.y})  Min: (${minPos.x}, ${minPos.y})  Max: (${maxPos.x}, ${maxPos.y}) Chunks: ${newChunks.size}"
		}
	}

	/**
	 * Save [chunks] to a file
	 */
	fun saveChunks(chunks: MutableSet<Chunk>) {
		val world = TerrainHandler.world!!.id // name of the world, so we know where to save to
		// Can we do this async?
		chunks.forEach { chunk ->
			// Save the chunk to a file

			// TODO
		}
	}

	/**
	 * @return the chunk at [pos] loaded from a file, null if it was never saved
	 */
	fun loadChunk(pos: Position): Chunk? {
		val chunkOrigin = pos.chunkOrigin
		val world = TerrainHandler.world!!.id
		// TODO
		return null
	}
}
