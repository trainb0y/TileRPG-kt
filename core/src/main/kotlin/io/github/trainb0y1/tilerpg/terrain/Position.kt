package io.github.trainb0y1.tilerpg.terrain

import com.badlogic.gdx.math.Vector2
import io.github.trainb0y1.tilerpg.terrain.chunk.Chunk
import io.github.trainb0y1.tilerpg.terrain.tile.TileType

/**
 * Holds an x and y value.
 * Created because the number of extension functions on Vector2 was getting ridiculous
 */
data class Position(var x: Float, var y: Float) {
	constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat()) // might as well

	operator fun plus(other: Position): Position = Position(x+other.x, y+other.y)
	operator fun minus(other: Position): Position = Position(x-other.x, y-other.y)


	/**
	 * Force round x and y values to be integers
	 */
	fun roundToInt() { // There's probably a better way to do this
		x = x.toInt().toFloat()
		y = y.toInt().toFloat()
	}

	/**
	 * Clamp this position's x and y coordinates between those of [min] and [max]
	 * @return this position for chaining
	 */
	fun clampXY(min: Position, max: Position): Position {
		this.x = this.x.coerceIn(min.x, max.x)
		this.y = this.y.coerceIn(min.y, max.y)
		return this
	}

	/**
	 * Set this vector to the chunk origin of the chunk that contains its position
	 * @return this position for chaining
	 */
	fun toChunkOrigin() {
		x -= x % TerrainHandler.chunkSize
		y -= y % TerrainHandler.chunkSize
	}

	/**
	 * Convenience reference to TerrainHandler.getChunk(this, [force])
	 * @see TerrainHandler.getChunk
	 */
	fun getChunk(force: Boolean = false): Chunk? = TerrainHandler.getChunk(this, force)

	/**
	 * Convenience reference to TerrainHandler.getTile(this, [force])
	 * @see TerrainHandler.getTile
	 */
	fun getTile(force: Boolean = false): TileType? = TerrainHandler.getTile(this)

	/**
	 * Convenience reference to TerrainHandler.setTile(this, [tileType], [force])
	 * @see TerrainHandler.setTile
	 */
	fun setTile(pos: Position, tileType: TileType, force: Boolean = false): Boolean = TerrainHandler.setTile(this, tileType, force)

	/**
	 * Self explanatory, if you don't understand it, go visit the hospital
	 */
	fun toVector2(): Vector2 = Vector2(x,y)
}