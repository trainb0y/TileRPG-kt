package io.github.trainb0y.tilerpg.terrain.chunk

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import io.github.trainb0y.tilerpg.terrain.TilePosition
import io.github.trainb0y.tilerpg.terrain.tile.Tile
import io.github.trainb0y.tilerpg.terrain.tile.TileData
import io.github.trainb0y.tilerpg.terrain.tile.TileLayer

class Chunk(private val size: Int = 16, val origin: TilePosition) {
	private val foregroundTiles = mutableMapOf<TilePosition, TileData>()
	private val backgroundTiles = mutableMapOf<TilePosition, TileData>()

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
	private fun containsRelative(pos: TilePosition): Boolean {
		if (pos.x > size || pos.y > size) return false
		return true
	}

	/**
	 * @return the tile at global coordinates [pos] in layer [layer]
	 * @throws InvalidTileLayerException if [layer] is [TileLayer.BOTH]
	 * @see getRelativeTile
	 */
	fun getTile(pos: TilePosition, layer: TileLayer): TileData? {
		if (!contains(pos)) throw PositionNotInChunkException(this, pos)
		return getRelativeTile(toRelativeCoordinates(pos), layer)
	}

	/**
	 * @return the tile at chunk coordinates [pos] in layer [layer]
	 * @throws InvalidTileLayerException if [layer] is [TileLayer.BOTH]
	 * @see getTile
	 */
	fun getRelativeTile(pos: TilePosition, layer: TileLayer): TileData? {
		return when (layer) {
			TileLayer.BACKGROUND -> backgroundTiles[pos]
			TileLayer.FOREGROUND -> foregroundTiles[pos]
			else -> {
				throw InvalidTileLayerException("Can't get a tile in both layers! Specify foreground or background")
			}
		}
	}

	/**
	 * Sets the tile at global coordinates [pos] and layer [layer]
	 * @throws PositionNotInChunkException if this chunk does not contain [pos]
	 * @return true if placing succeeded
	 * @see setRelativeTile
	 */
	fun setTile(pos: TilePosition, tile: TileData?, layer: TileLayer): Boolean {
		if (!contains(pos)) throw PositionNotInChunkException(this, pos)
		return setRelativeTile(toRelativeCoordinates(pos), tile, layer)
	}

	/**
	 * Sets the tile at chunk relative coordinates [pos] and layer [layer]
	 * @return true if placing succeeded <- should always be the case
	 * @see setTile
	 */
	fun setRelativeTile(pos: TilePosition, tile: TileData?, layer: TileLayer): Boolean {
		val tiles = when (layer) {
			TileLayer.FOREGROUND -> foregroundTiles
			TileLayer.BACKGROUND -> backgroundTiles
			TileLayer.BOTH -> run {
				return setRelativeTile(pos, tile, TileLayer.BACKGROUND) &&
						setRelativeTile(pos, tile, TileLayer.FOREGROUND)
			}
		}
		if (tile == null) {
			tiles.remove(TilePosition(pos.x, pos.y))
			return true
		}
		tiles[TilePosition(pos.x, pos.y)] = tile
		return true
	}

	/**
	 * Render the tiles to [batch]
	 * @throws InvalidTileLayerException if [layer] is [TileLayer.BOTH]
	 */
	fun render(batch: Batch, layer: TileLayer) {
		when (layer) {
			TileLayer.FOREGROUND -> foregroundTiles.forEach { (pos, tile) ->
				batch.draw(tile.type.texture, pos.x + origin.x.toFloat(), pos.y + origin.y.toFloat(), 1f, 1f)
			}
			TileLayer.BACKGROUND -> backgroundTiles.forEach { (pos, tile) ->
				if (!foregroundTiles.containsKey(pos))  // don't bother rendering if there's foreground here
					batch.draw(tile.type.texture, pos.x + origin.x.toFloat(), pos.y + origin.y.toFloat(), 1f, 1f)
			}
			TileLayer.BOTH -> throw InvalidTileLayerException("Can't render both layers at once!")
		}
	}
}

class InvalidTileLayerException(text: String): Exception(text)

class PositionNotInChunkException(chunk: Chunk, position: TilePosition) :
	Exception("Position (${position.x},${position.y}) not is inside the chunk at (${chunk.origin.x},${chunk.origin.y})")
