package io.github.trainb0y.tilerpg.terrain.chunk

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.badlogic.gdx.physics.box2d.Fixture
import io.github.trainb0y.tilerpg.screen.GameScreen.Companion.physics
import io.github.trainb0y.tilerpg.terrain.TilePosition
import io.github.trainb0y.tilerpg.terrain.tile.TileData
import io.github.trainb0y.tilerpg.terrain.tile.TileLayer


class Chunk(private val size: Int = 16, val origin: TilePosition) {
	private val foregroundTiles = mutableMapOf<TilePosition, TileData>()
	private val backgroundTiles = mutableMapOf<TilePosition, TileData>()
	private val physicsBody : Body
	var fixtures = mutableSetOf<Fixture>()

	fun toRelativeCoordinates(globalPos: TilePosition): TilePosition = globalPos - origin
	fun toGlobalCoordinates(relativePos: TilePosition): TilePosition = relativePos + origin

	init {
		val bodyDef = BodyDef()
		bodyDef.type = BodyDef.BodyType.StaticBody
		bodyDef.position.set(origin.x.toFloat(), origin.y.toFloat())

		// add it to the world
		physicsBody = physics.world.createBody(bodyDef)
	}

	fun updatePhysicsShape() {

		val lines = mutableSetOf<Pair<Vector2, Vector2>>()

		foregroundTiles.keys.forEach {pos ->
			if (foregroundTiles[pos] == null) return@forEach
			if (foregroundTiles[pos + TilePosition(0, 1)] == null)
				lines.add(Pair(
					Vector2(pos.x - 0.5f, pos.y + 0.5f),
					Vector2(pos.x + 0.5f, pos.y + 0.5f)
				))

			if (foregroundTiles[pos + TilePosition(0, -1)] == null)
				lines.add(Pair(
					Vector2(pos.x - 0.5f, pos.y - 0.5f),
					Vector2(pos.x + 0.5f, pos.y - 0.5f)
				))

			if (foregroundTiles[pos + TilePosition(1, 0)] == null)
				lines.add(Pair(
					Vector2(pos.x + 0.5f, pos.y - 0.5f),
					Vector2(pos.x + 0.5f, pos.y + 0.5f)
				))

			if (foregroundTiles[pos + TilePosition(-1, 0)] == null)
				lines.add(Pair(
					Vector2(pos.x - 0.5f, pos.y - 0.5f),
					Vector2(pos.x - 0.5f, pos.y + 0.5f)
				))

		}
		fixtures.forEach { physicsBody.destroyFixture(it) }
		fixtures.clear()
		while (lines.isNotEmpty()){
			val line = lines.first()
			lines.remove(line)
			val shape = EdgeShape()
			shape.set(line.first, line.second)
			fixtures.add(
				physicsBody.createFixture(shape , 0f)
			)
			shape.dispose()
		}
	}

	/**
	 * @return whether the global position [pos] is contained in this chunk
	 */
	fun contains(pos: TilePosition) = containsRelative(toRelativeCoordinates(pos))

	/**
	 * @return whether relative position [pos] is contained in this chunk
	 */
	private fun containsRelative(pos: TilePosition) = pos.x < size && pos.y < size

	/**
	 * @return the tile at global coordinates [pos] in layer [layer]
	 * @throws InvalidTileLayerException if [layer] is [TileLayer.BOTH]
	 * @throws PositionNotInChunkException if [pos] is not part of this chunk
	 * @see getRelativeTile
	 */
	fun getTile(pos: TilePosition, layer: TileLayer): TileData? {
		return getRelativeTile(toRelativeCoordinates(pos), layer)
	}

	/**
	 * @return the tile at chunk coordinates [pos] in layer [layer]
	 * @throws InvalidTileLayerException if [layer] is [TileLayer.BOTH]
	 * @throws PositionNotInChunkException if [pos] is not part of this chunk
	 * @see getTile
	 */
	fun getRelativeTile(pos: TilePosition, layer: TileLayer): TileData? {
		if (!containsRelative(pos)) throw  PositionNotInChunkException(this, toGlobalCoordinates(pos))
		return when (layer) {
			TileLayer.BACKGROUND -> backgroundTiles[pos]
			TileLayer.FOREGROUND -> foregroundTiles[pos]
			else -> {
				throw InvalidTileLayerException("Can't get a tile in both layers! Specify foreground or background")
			}
		}
	}

	/**
	 * Sets the tile at global coordinates [pos] and layer [layer], and updates collision
	 * @throws PositionNotInChunkException if this chunk does not contain [pos]
	 * @return true if placing succeeded
	 * @see setRelativeTile
	 */
	fun setTile(pos: TilePosition, tile: TileData?, layer: TileLayer): Boolean {
		val result = setRelativeTile(toRelativeCoordinates(pos), tile, layer)
		if (layer == TileLayer.FOREGROUND || layer == TileLayer.BOTH) updatePhysicsShape()
		return result
	}

	/**
	 * Sets the tile at chunk relative coordinates [pos] and layer [layer]
	 * Does not update collision, even if [layer] is [TileLayer.FOREGROUND]
	 * @return true if placing succeeded <- should always be the case
	 * @see setTile
	 */
	fun setRelativeTile(pos: TilePosition, tile: TileData?, layer: TileLayer): Boolean {
		if (!containsRelative(pos)) throw  PositionNotInChunkException(this, toGlobalCoordinates(pos))
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
