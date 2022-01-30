package io.github.trainb0y1.tilerpg.terrain.chunk

import com.badlogic.gdx.graphics.g2d.Batch
import io.github.trainb0y1.tilerpg.terrain.Position
import io.github.trainb0y1.tilerpg.terrain.tile.Textures
import io.github.trainb0y1.tilerpg.terrain.tile.TileData

// Possible concern: Position's values can be floats, so tiles can be placed at floats?
class Chunk(private val size: Int = 16, val origin: Position) {
	private val tiles = mutableMapOf<Position, TileData>()

	fun toRelativeCoordinates(pos: Position): Position = pos - origin
	fun toGlobalCoordinates(pos: Position): Position = pos + origin

	/**
	 * @return whether the global position [pos] is contained in this chunk
	 */
	fun contains(pos: Position): Boolean {
		return containsRelative(toRelativeCoordinates(pos))
	}

	/**
	 * @return whether relative position [pos] is contained in this chunk
	 */
	fun containsRelative(pos: Position): Boolean {
		if (pos.x > size || pos.y > size) return false
		return true
	}

	/**
	 * @return the tile at global coordinates [pos]
	 */
	fun getTile(pos: Position): TileData? {
		if (!contains(pos)) throw PositionNotInChunkException(this, pos)
		return getRelativeTile(toRelativeCoordinates(pos))
	}

	/**
	 * @return the tile at chunk coordinates [pos]
	 */
	fun getRelativeTile(pos: Position): TileData? {
		return tiles[Position(pos.x, pos.y)]
	}

	/**
	 * Sets the tile at global coordinates [pos]
	 * @throws PositionNotInChunkException if this chunk does not contain [pos]
	 * @return true if placing succeeded
	 */
	fun setTile(pos: Position, tile: TileData?): Boolean {
		if (!contains(pos)) throw PositionNotInChunkException(this, pos)
		return setRelativeTile(toRelativeCoordinates(pos), tile)
	}

	/**
	 * Sets the tile at chunk relative coordinates [pos]
	 * @return true if placing succeeded <- should always be the case
	 */
	fun setRelativeTile(pos: Position, tile: TileData?): Boolean {
		if (tile == null) {
			tiles.remove(Position(pos.x, pos.y))
			return true
		}
		tiles[Position(pos.x, pos.y)] = tile
		return true
	}

	/**
	 * Render the tiles to [batch]
	 */
	fun render(batch: Batch) {
		tiles.forEach { (pos, tile) ->
			batch.draw(Textures.tileTextures[tile.type.id], pos.x+origin.x, pos.y+origin.y, 1f, 1f)
		}
	}
}

class PositionNotInChunkException(chunk: Chunk, position: Position) :
	Exception("Position (${position.x},${position.y}) not is inside the chunk at (${chunk.origin.x},${chunk.origin.y})")