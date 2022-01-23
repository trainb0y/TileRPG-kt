package io.github.trainb0y1.tilerpg.terrain.chunk

import com.badlogic.gdx.math.Vector2

import io.github.trainb0y1.tilerpg.terrain.tile.TileType

// Possible concern: Vector2s can be floats, so tiles can be placed at floats?
class Chunk(private val size: Int = 16, val origin: Vector2) {
	private val tiles = mutableListOf<MutableList<TileType>>()

	fun toRelativeCoordinates(pos: Vector2): Vector2 {
		return pos.sub(origin)
	}
	fun toGlobalCoordinates(pos: Vector2): Vector2 {
		return pos.add(origin)
	}
	/**
	 * @return whether [x], [y] is contained in this chunk
	 */
	fun inChunk(pos: Vector2): Boolean {
		if (pos.x > size || pos.y > size) return false
		return true
	}
	/**
	 * @return the tile at global coordinates [x],[y]
	 */
	fun getTile(pos: Vector2): TileType? {
		if (!inChunk(pos)) throw PositionNotInChunkException(this, pos)
		return getRelativeTile(toRelativeCoordinates(pos))
	}
	/**
	 * @return the tile at chunk coordinates [x],[y]
	 */
	fun getRelativeTile(pos: Vector2): TileType? {
		return tiles[pos.x.toInt()][pos.y.toInt()]
	}

	/**
	 * Sets the tile at global coordinates [pos]
	 * @throws PositionNotInChunkException if this chunk does not contain [pos]
	 * @return true if placing succeeded
	 */
	fun setTile(pos: Vector2, tileType: TileType): Boolean {
		if (!inChunk(pos)) throw PositionNotInChunkException(this, pos)
		return setRelativeTile(toRelativeCoordinates(pos), tileType)
	}
	/**
	 * Sets the tile at chunk relative coordinates [pos]
	 * @return true if placing succeeded <- should always be the case
	 */
	fun setRelativeTile(pos: Vector2, tileType: TileType): Boolean {
		tiles[pos.x.toInt()][pos.y.toInt()] = tileType
		return true
	}
}

class PositionNotInChunkException(chunk: Chunk, position: Vector2):
	Exception("Position (${position.x},${position.y}) not is inside the chunk at (${chunk.origin.x},${chunk.origin.y})")