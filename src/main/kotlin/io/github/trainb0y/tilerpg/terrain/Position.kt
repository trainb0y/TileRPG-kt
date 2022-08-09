package io.github.trainb0y.tilerpg.terrain

import com.badlogic.gdx.math.Vector2
import io.github.trainb0y.tilerpg.terrain.chunk.Chunk
import io.github.trainb0y.tilerpg.terrain.tile.TileData

/**
 * Holds an x and y value.
 * Created because the number of extension functions on [Vector2] was getting ridiculous
 * @see Vector2
 */
data class Position(var x: Float, var y: Float) {
	constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat()) // might as well
	operator fun plus(other: Position) = Position(x + other.x, y + other.y)
	operator fun minus(other: Position) = Position(x - other.x, y - other.y)


	/**
	 * Force round x and y values to be integers
	 * @return this position for chaining
	 */
	fun roundToInt(): Position { // There's probably a better way to do this
		x = x.toInt().toFloat()
		y = y.toInt().toFloat()
		return this
	}

	/**
	 * Clamp this position's x and y coordinates between those of [min] and [max]
	 * @return this position clamped between [min] and [max]
	 */
	fun clampXY(min: Position, max: Position) = Position(x.coerceIn(min.x, max.x), y.coerceIn(min.y, max.y))

	/**
	 * the origin of the chunk that contains this position
	 */
	val chunkOrigin: Position
		get() = Position(x % TerrainHandler.chunkSize, y % TerrainHandler.chunkSize)

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
