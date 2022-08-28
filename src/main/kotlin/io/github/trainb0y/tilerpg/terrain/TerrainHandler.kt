package io.github.trainb0y.tilerpg.terrain

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import io.github.trainb0y.tilerpg.screen.GameScreen.Companion.json
import io.github.trainb0y.tilerpg.screen.GameScreen.Companion.logger
import io.github.trainb0y.tilerpg.terrain.chunk.Chunk
import io.github.trainb0y.tilerpg.terrain.tile.TileData
import io.github.trainb0y.tilerpg.terrain.tile.TileLayer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.math.roundToInt

/**
 * Manages the terrain; chunks, tile set methods, etc.
 */
object TerrainHandler {
	var chunks = mutableMapOf<TilePosition, Chunk>()
	const val chunkSize = 16
	var world: WorldData? = null
	var generator: TerrainGenerator? = null

	/**
	 * @return the chunk at [pos]
	 * @param force whether to load/create the chunk if it isn't currently loaded
	 */
	fun getChunk(pos: TilePosition, force: Boolean = false): Chunk? {
		val origin = pos.chunkOrigin
		return chunks[origin] ?: if (force) {
			loadChunkFromFile(origin) ?: generator!!.generateChunk(origin)
		} else null
	}

	/**
	 * Loads a chunk at [origin] from a file, overwrites any existing tiles
	 * @return the chunk
	 */
	@OptIn(ExperimentalSerializationApi::class)
	fun loadChunkFromFile(origin: TilePosition): Chunk? {
		// This could really use some logging
		val filename = getChunkFileName(origin)
		if (!filename.exists()) return null
		return try {
			val chunk = Chunk(chunkSize, origin)
			chunk.reconstitute(json.decodeFromStream(filename.toFile().inputStream()))
			return chunk
		} catch (e: Exception) {
			logger.error(e) { "Error loading chunk at ${origin.x}, ${origin.y}" }
			null
		} // This seems dumb
	}

	/**
	 * Save the [chunk] to a file
	 * @return whether an older chunk was overwritten
	 */
	fun saveChunkToFile(chunk: Chunk): Boolean {
		return chunk.saveToFile(getChunkFileName(chunk))
	}

	/**
	 * @return the TileData at [pos]
	 * @param force whether to load/create the chunk if that chunk is not currently loaded
	 */
	fun getTile(pos: TilePosition, force: Boolean = false, layer: TileLayer = TileLayer.BOTH): TileData? =
		getChunk(pos, force)?.getTile(pos, layer)

	/**
	 * Set the [tile] at [pos]
	 * @param force whether to load/create the chunk if it is not currently loaded
	 * @return whether placing succeeded
	 */
	fun setTile(
		pos: TilePosition,
		tile: TileData?,
		force: Boolean = false,
		layer: TileLayer = TileLayer.BOTH
	): Boolean =
		getChunk(pos, force)?.setTile(pos, tile, layer) ?: false

	/**
	 * Tries to load world generation values from saved world with [id].
	 * If no world is found, generates a new world with [id]
	 * @return whether an existing world was found
	 */
	fun loadWorld(id: String): Boolean {
		// TODO()
		world = WorldData(id, 1111, 10, 40, 20, 40f, -0.4f, 10f, -0.8f)
		generator = TerrainGenerator(world!!)
		return false
	}

	/**
	 * @return the filename for the chunk at [origin]
	 */
	private fun getChunkFileName(origin: TilePosition) =
		getWorldDirectory().resolve("chunks").createDirectories().resolve("chunk_${origin.x}_${origin.y}.chunk")

	/**
	 * @return the filename for the [chunk]
	 */
	private fun getChunkFileName(chunk: Chunk) = getChunkFileName(chunk.origin)

	/**
	 * @return the filename for the current world
	 */
	private fun getWorldFileName() = getWorldDirectory().resolve("${world!!.id}.world")

	/**
	 * @return the name of the directory for the current world
	 */
	private fun getWorldDirectory(): Path = Gdx.files.external("tilerpg/world-${world!!.id}").file().toPath().createDirectories()

	/**
	 * Attempts to load all chunks visible to [camera] if they aren't already loaded. If they don't exist, create them.
	 * Loads [buffer] hidden chunks in all directions.
	 * Saves all non-visible chunks to files.
	 */
	fun loadVisibleChunks(camera: OrthographicCamera, buffer: Int = 0) {
		val bufferSize = chunkSize * buffer

		// find the bounds of the area where chunks should be loaded
		val minPos = TilePosition(
			// Subtracting chunkSize so that visible chunks with non-visible origins are still loaded
			((camera.position.x - (camera.viewportWidth / 2f)) - bufferSize).roundToInt() - chunkSize, // this
			((camera.position.y - (camera.viewportHeight / 2f)) - bufferSize).roundToInt() - chunkSize // feels
		)
		val maxPos = TilePosition(
			(camera.position.x + (camera.viewportWidth / 2f)).roundToInt() + bufferSize, // very
			(camera.position.y + (camera.viewportHeight / 2f)).roundToInt() + bufferSize // bad
		)


		// Load visible chunks
		val newChunks = mutableMapOf<TilePosition, Chunk>()
		for (x in minPos.x..maxPos.x step chunkSize) {
			for (y in minPos.y..maxPos.y step chunkSize) {
				// All of these should be loaded
				newChunks[TilePosition(x, y).chunkOrigin] =
					getChunk(TilePosition(x, y), true)!! // Force will create/load it for us
			}
		}

		// Save old chunks that aren't visible
		// todo: do it async
		chunks.keys.filter { !newChunks.containsKey(it) }.forEach {
			saveChunkToFile(chunks[it]!!)
		}

		chunks = newChunks
	}
}
