package io.github.trainb0y1.tilerpg.terrain.chunk

import com.badlogic.gdx.graphics.Camera

object ChunkLoader {
	/**
	 * Attemtps to load all visible chunks if they aren't already loaded. If they don't exist, create them.
	 * Saves all non-visible chunks to files.
	 */
	fun loadVisibleChunks(camera: Camera) {
		TODO()
	}

	/**
	 * Save [chunks] to a file
	 */
	fun saveChunks(chunks: MutableSet<Chunk>) {
		chunks.forEach{ chunk ->
			// Save the chunk to a file
			TODO()
		}
	}
}