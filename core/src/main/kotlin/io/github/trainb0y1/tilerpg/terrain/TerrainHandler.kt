package io.github.trainb0y1.tilerpg.terrain

import io.github.trainb0y1.tilerpg.terrain.chunk.Chunk
import io.github.trainb0y1.tilerpg.terrain.tile.TileData

/**
 * Manages the terrain; chunks, tile set methods, etc.
 */
object TerrainHandler {
	val chunks = mutableMapOf<Position, Chunk>()
	val chunkSize = 16
	var world: World? = null

	/**
	 * @return the chunk at [pos]
	 * @param force whether to load/create the chunk if it isn't currently loaded
	 */
	fun getChunk(pos: Position, force: Boolean = false): Chunk? {
		pos.toChunkOrigin()
		return chunks[pos] ?: if (force) {
			attemptLoadChunk(pos) ?: createChunk(pos)
		} else {
			null
		}
	}

	/**
	 * Creates the chunk at [origin]. Overwrites any existing chunks
	 */
	fun createChunk(origin: Position): Chunk {
		val chunk = Chunk(chunkSize, origin)
		chunks[origin] = chunk
		return chunk
	}

	/**
	 * Attempts to load a chunk for that origin from a file
	 */
	fun attemptLoadChunk(origin: Position): Chunk? {
		// TODO
		return null
	}

	/**
	 * @return the TileData at [pos]
	 * @param force whether to load/create the chunk if that chunk is not currently loaded
	 */
	fun getTile(pos: Position, force: Boolean = false): TileData? = getChunk(pos, force)?.getTile(pos)

	/**
	 * Set the [tile] at [pos]
	 * @param force whether to load/create the chunk if it is not currently loaded
	 * @return whether placing succeeded
	 */
	fun setTile(pos: Position, tile: TileData, force: Boolean = false): Boolean =
		getChunk(pos, force)?.setTile(pos, tile) ?: false

	/**
	 * Tries to load world generation values from saved world with [id].
	 * If no world is found, generates a new world with [id]
	 * @return whether an existing world was found
	 */
	fun loadWorld(id: String): Boolean {
		TODO()
	}
}