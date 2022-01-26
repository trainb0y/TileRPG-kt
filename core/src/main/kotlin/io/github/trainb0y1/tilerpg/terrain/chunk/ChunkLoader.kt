package io.github.trainb0y1.tilerpg.terrain.chunk

import com.badlogic.gdx.graphics.OrthographicCamera
import io.github.trainb0y1.tilerpg.terrain.Position
import io.github.trainb0y1.tilerpg.terrain.TerrainHandler


object ChunkLoader {
	/**
	 * Attempts to load all chunks visible to [camera] if they aren't already loaded. If they don't exist, create them.
	 * Loads [buffer] hidden chunks in all directions.
	 * Saves all non-visible chunks to files.
	 */
	@Suppress("UnnecessaryVariable")
	fun loadVisibleChunks(camera: OrthographicCamera, buffer: Int = 0) {
		val bufferSize = TerrainHandler.chunkSize * buffer
		val minX = (camera.position.x - (camera.viewportWidth / 2)) - bufferSize // this
		val maxX = (camera.position.x + (camera.viewportWidth / 2)) + bufferSize // feels
		val minY = (camera.position.y - (camera.viewportWidth / 2)) - bufferSize // very
		val maxY = (camera.position.y + (camera.viewportWidth / 2)) + bufferSize // bad
		val minPos = Position(minX, minY)
		val maxPos = Position(maxX, maxY)

		// Save non-visible chunks
		val chunksToSave = mutableSetOf<Chunk>()
		TerrainHandler.chunks.forEach { (pos, chunk) ->
			val oldPos = pos // pos gets modified by clampXY
			if (pos.clampXY(minPos, maxPos) != oldPos) {
				chunksToSave.add(chunk)
			}
			// TerrainHandler.chunks.remove(pos) <- cant do this while we're iterating over it
		}
		saveChunks(chunksToSave)

		// Load visible chunks
		var newChunks = mutableMapOf<Position, Chunk>()
		for (x in minX.toInt()..maxX.toInt() step TerrainHandler.chunkSize) {
			for (y in minY.toInt()..maxY.toInt() step TerrainHandler.chunkSize) {
				// All of these should be loaded
				newChunks[Position(x,y)] = TerrainHandler.getChunk(Position(x, y), true)!! // Force will create/load it for us
			}
		}
		TerrainHandler.chunks = newChunks
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
		val chunkOrigin = pos.toChunkOrigin()
		// TODO
		return null
	}
}