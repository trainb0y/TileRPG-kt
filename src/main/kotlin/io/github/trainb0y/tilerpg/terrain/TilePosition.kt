package io.github.trainb0y.tilerpg.terrain

import com.badlogic.gdx.math.Vector2
import io.github.trainb0y.tilerpg.terrain.tile.TileData
import io.github.trainb0y.tilerpg.terrain.tile.TileLayer
import kotlinx.serialization.Serializable

/**
 * Holds an x and y value.
 * Created because the number of extension functions on [Vector2] was getting ridiculous
 * @see Vector2
 */
@Serializable
data class TilePosition(val x: Int, val y: Int) {
	operator fun plus(other: TilePosition) = TilePosition(x + other.x, y + other.y)
	operator fun minus(other: TilePosition) = TilePosition(x - other.x, y - other.y)

	/**
	 * Clamp this position's x and y coordinates between those of [min] and [max]
	 * @return this position clamped between [min] and [max]
	 */
	fun clampXY(min: TilePosition, max: TilePosition) = TilePosition(x.coerceIn(min.x, max.x), y.coerceIn(min.y, max.y))

	/**
	 * the origin of the chunk that contains this position, if that chunk is loaded
	 */
	val chunkOrigin: TilePosition
		get() = this - TilePosition( // % can return negative, so use floorMod
			Math.floorMod(x, TerrainHandler.chunkSize),
			Math.floorMod(y, TerrainHandler.chunkSize)
		)

	/**
	 * The chunk that contains this position
	 * @see TerrainHandler.getChunk
	 */
	fun getChunk(force: Boolean = false) = TerrainHandler.getChunk(this, force)

	/**
	 * @see TerrainHandler.getTile
	 */
	fun getTile(force: Boolean = false, layer: TileLayer = TileLayer.BOTH): TileData? =
		TerrainHandler.getTile(this, force, layer)

	/**
	 * @see TerrainHandler.setTile
	 */
	fun setTile(data: TileData?, force: Boolean = false, layer: TileLayer = TileLayer.BOTH) =
		TerrainHandler.setTile(this, data, force, layer)
}
