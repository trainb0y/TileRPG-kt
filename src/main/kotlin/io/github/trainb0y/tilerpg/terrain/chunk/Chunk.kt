package io.github.trainb0y.tilerpg.terrain.chunk

import com.badlogic.gdx.graphics.g2d.Batch
import io.github.trainb0y.tilerpg.terrain.TilePosition
import io.github.trainb0y.tilerpg.terrain.tile.TileData

// Possible concern: Position's values can be floats, so tiles can be placed at floats?
class Chunk(private val size: Int = 16, val origin: TilePosition) {
	private val tiles = mutableMapOf<TilePosition, TileData>()

	fun toRelativeCoordinates(globalPos: TilePosition): TilePosition = globalPos - origin
	fun toGlobalCoordinates(relativePos: TilePosition): TilePosition = relativePos + origin

	/**
	 * @return whether the global position [pos] is contained in this chunk
	 */
	fun contains(pos: TilePosition): Boolean {
		return containsRelative(toRelativeCoordinates(pos))
	}

	/**
	 * @return whether relative position [pos] is contained in this chunk
	 */
	fun containsRelative(pos: TilePosition): Boolean {
		if (pos.x > size || pos.y > size) return false
		return true
	}

	/**
	 * @return the tile at global coordinates [pos]
	 */
	fun getTile(pos: TilePosition): TileData? {
		if (!contains(pos)) throw PositionNotInChunkException(this, pos)
		return getRelativeTile(toRelativeCoordinates(pos))
	}

	/**
	 * @return the tile at chunk coordinates [pos]
	 */
	fun getRelativeTile(pos: TilePosition): TileData? {
		return tiles[TilePosition(pos.x, pos.y)]
	}

	/**
	 * Sets the tile at global coordinates [pos]
	 * @throws PositionNotInChunkException if this chunk does not contain [pos]
	 * @return true if placing succeeded
	 */
	fun setTile(pos: TilePosition, tile: TileData?): Boolean {
		if (!contains(pos)) throw PositionNotInChunkException(this, pos)
		return setRelativeTile(toRelativeCoordinates(pos), tile)
	}

	/**
	 * Sets the tile at chunk relative coordinates [pos]
	 * @return true if placing succeeded <- should always be the case
	 */
	fun setRelativeTile(pos: TilePosition, tile: TileData?): Boolean {
		if (tile == null) {
			tiles.remove(TilePosition(pos.x, pos.y))
			return true
		}
		tiles[TilePosition(pos.x, pos.y)] = tile
		return true
	}

	/**
	 * Render the tiles to [batch]
	 */
	fun render(batch: Batch) {
		tiles.forEach { (pos, tile) ->
			batch.draw(tile.type.texture, pos.x+origin.x.toFloat(), pos.y+origin.y.toFloat(), 1f, 1f)
		}
	}
}

class PositionNotInChunkException(chunk: Chunk, position: TilePosition) :
	Exception("Position (${position.x},${position.y}) not is inside the chunk at (${chunk.origin.x},${chunk.origin.y})")
