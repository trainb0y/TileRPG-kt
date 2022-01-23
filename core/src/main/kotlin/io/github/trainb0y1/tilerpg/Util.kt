package io.github.trainb0y1.tilerpg

import com.badlogic.gdx.math.Vector2


fun vector2FromInt(x: Int, y: Int): Vector2 = Vector2(x.toFloat(), y.toFloat())

/**
 * Force the x and y values to be integers
 */
fun Vector2.roundToInt() { // There's probably a better way to do this
	this.x = this.x.toInt().toFloat()
	this.y = this.y.toInt().toFloat()
}