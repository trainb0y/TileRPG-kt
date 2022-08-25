package io.github.trainb0y.tilerpg.terrain

import com.badlogic.gdx.math.Vector2
import io.github.trainb0y.tilerpg.terrain.chunk.Chunk
import io.github.trainb0y.tilerpg.terrain.tile.TileData

/**
 * Holds an x and y value.
 * Created because the number of extension functions on [Vector2] was getting ridiculous
 * @see Vector2
 */
data class TilePosition(val x: Int, val y: Int) {
	operator fun plus(other: TilePosition) = TilePosition(x + other.x, y + other.y)
	operator fun minus(other: TilePosition) = TilePosition(x - other.x, y - other.y)

	/**
	 * Clamp this position's x and y coordinates between those of [min] and [max]
	 * @return this position clamped between [min] and [max]
	 */
	fun clampXY(min: TilePosition, max: TilePosition) = TilePosition(x.coerceIn(min.x, max.x), y.coerceIn(min.y, max.y))

	/**
	 * the origin of the chunk that contains this position
	 */
	val chunkOrigin: TilePosition
		get() = this - TilePosition(x % TerrainHandler.chunkSize, y % TerrainHandler.chunkSize)

	/**
	 * The chunk that contains this position
	 * @see forceChunk
	 */
	val chunk: Chunk?
		get() = TerrainHandler.getChunk(this, false)

	/**
	 * The chunk that contains this position
	 * Creates/loads it if it is not currently loaded
	 * @see chunk
	 */
	val forceChunk: Chunk
		get() = TerrainHandler.getChunk(this, true)!!

	/**
	 * The tile at this position
	 * @see forceTile
	 */
	var tile: TileData?
		get() = TerrainHandler.getTile(this, false)
		set(value) {
			TerrainHandler.setTile(this, value, false)
		}

	/**
	 * The tile at this position.
	 * Creates/loads the chunk if the chunk is not loaded
	 * @see tile
	 */
	var forceTile: TileData?
		get() = TerrainHandler.getTile(this, true)
		set(value) {
			TerrainHandler.setTile(this, value, true)
		}
}
