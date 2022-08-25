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
	var chunks = mutableMapOf<TilePosition, Chunk>()
	const val chunkSize = 16
	var world: World? = null

	/**
	 * @return the chunk at [pos]
	 * @param force whether to load/create the chunk if it isn't currently loaded
	 */
	fun getChunk(pos: TilePosition, force: Boolean = false): Chunk? {
		val origin = pos.chunkOrigin
		return chunks[origin] ?: if (force) {
			loadChunkFromFile(origin) ?: TerrainGenerator.generateChunk(origin)
		} else null
	}

	/**
	 * Loads a chunk at [origin] from a file, overwrites any existing tiles
	 * @return the chunk
	 */
	private fun loadChunkFromFile(origin: TilePosition): Chunk? {
		// This could really use some logging
		val filename = getChunkFileName(origin)
		return try {
			Json().fromJson<Chunk>(filename.toLocalFile())
		} catch (e: Exception) {
			null
		} // This seems dumb
	}

	/**
	 * Save the chunk at [origin] to a file
	 * @return whether an older chunk was overwritten
	 */
	private fun saveChunkToFile(origin: TilePosition): Boolean {
		val filename = getChunkFileName(origin)
		println(Json().toJson(getChunk(origin, false)))
		return false
	}

	/**
	 * @return the TileData at [pos]
	 * @param force whether to load/create the chunk if that chunk is not currently loaded
	 */
	fun getTile(pos: TilePosition, force: Boolean = false): TileData? = getChunk(pos, force)?.getTile(pos)

	/**
	 * Set the [tile] at [pos]
	 * @param force whether to load/create the chunk if it is not currently loaded
	 * @return whether placing succeeded
	 */
	fun setTile(pos: TilePosition, tile: TileData?, force: Boolean = false): Boolean =
		getChunk(pos, force)?.setTile(pos, tile) ?: false

	/**
	 * Tries to load world generation values from saved world with [id].
	 * If no world is found, generates a new world with [id]
	 * @return whether an existing world was found
	 */
	fun loadWorld(id: String): Boolean {
		// TODO()
		world = World(id, 1111, 10, 40, 20)
		return false
	}

	/**
	 * @return the filename for the chunk at [origin]
	 */
	private fun getChunkFileName(origin: TilePosition) =
		getWorldDirectory() + "/chunks/chunk-${origin.x}-${origin.y}.chunk"

	/**
	 * @return the filename for the current world
	 */
	private fun getWorldFileName() = getWorldDirectory() + "/${world!!.id}.world"

	/**
	 * @return the name of the directory for the current world
	 */
	private fun getWorldDirectory() = "world-${world!!.id}"
}
