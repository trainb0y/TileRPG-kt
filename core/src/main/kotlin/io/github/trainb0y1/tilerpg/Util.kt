package io.github.trainb0y1.tilerpg

import com.badlogic.gdx.math.Vector2
import io.github.trainb0y1.tilerpg.terrain.TerrainHandler


fun vector2FromInt(x: Int, y: Int): Vector2 = Vector2(x.toFloat(), y.toFloat())

/**
 * Force the x and y values to be integers
 */
fun Vector2.roundToInt() { // There's probably a better way to do this
	this.x = this.x.toInt().toFloat()
	this.y = this.y.toInt().toFloat()
}

/**
 * Clamp this vector's x and y coordinates between those of [min] and [max]
 * @return this vector for chaining
 */
fun Vector2.clampXY(min: Vector2, max: Vector2): Vector2 {
	this.x = this.x.coerceIn(min.x, max.x)
	this.y = this.y.coerceIn(min.y, max.y)
	return this
}

/**
 * Set this vector to the chunk origin of the chunk that contains its position
 * @return this vector for chaining
 */
fun Vector2.toChunkOrigin() {
	this.x -= this.x % TerrainHandler.chunkSize
	this.y -= this.y % TerrainHandler.chunkSize
}