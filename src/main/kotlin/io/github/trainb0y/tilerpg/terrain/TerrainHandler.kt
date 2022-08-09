package io.github.trainb0y.tilerpg.terrain

import com.badlogic.gdx.utils.Json
import io.github.trainb0y.tilerpg.terrain.chunk.Chunk
import io.github.trainb0y.tilerpg.terrain.generation.TerrainGenerator
import io.github.trainb0y.tilerpg.terrain.tile.TileData
import ktx.assets.toLocalFile
import ktx.json.fromJson

/**
 * Manages the terrain; chunks, tile set methods, etc.
 */
object TerrainHandler {
	var chunks = mutableMapOf<Position, Chunk>()
	val chunkSize = 16
	var world: World? = null

	/**
	 * @return the chunk at [pos]
	 * @param force whether to load/create the chunk if it isn't currently loaded
	 */
	fun getChunk(pos: Position, force: Boolean = false): Chunk? {
		val origin = pos.chunkOrigin
		return chunks[origin] ?: if (force) {
			loadChunkFromFile(origin) ?: TerrainGenerator.generateChunk(origin)
		} else null
	}

	/**
	 * Loads a chunk at [origin] from a file, overwrites any existing tiles
	 * @return the chunk
	 */
	fun loadChunkFromFile(origin: Position): Chunk? {
		// This could really use some logging
		val filename = getChunkFileName(origin)
		return try {Json().fromJson<Chunk>(filename.toLocalFile())} catch (e: Exception) {null} // This seems dumb
	}

	/**
	 * Save the chunk at [origin] to a file
	 * @return whether an older chunk was overwritten
	 */
	fun saveChunkToFile(origin: Position): Boolean{
		val filename = getChunkFileName(origin)
		println(Json().toJson(getChunk(origin, false)))
		return false
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
	fun setTile(pos: Position, tile: TileData?, force: Boolean = false): Boolean =
		getChunk(pos, force)?.setTile(pos, tile) ?: false

	/**
	 * Tries to load world generation values from saved world with [id].
	 * If no world is found, generates a new world with [id]
	 * @return whether an existing world was found
	 */
	fun loadWorld(id: String): Boolean {
		// TODO()
		world = World(id, 1111, 10, 20, 20)
		return false
	}

	/**
	 * @return the filename for the chunk at [origin]
	 */
	fun getChunkFileName(origin: Position): String {
		return "world-${world!!.id}/chunk-${origin.x}-${origin.y}.chunk"
	}
	/**
	 * @return the filename for the current world
	 */
	fun getWorldFileName(): String {
		return "world-${world!!.id}/${world!!.id}.world"
	}
}
