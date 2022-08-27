package io.github.trainb0y.tilerpg.terrain

/**
 * Holds world-specific data
 */
data class GameWorld(
	val id: String,
	val seed: Int,
	val amplitude: Int,
	val wavelength: Int,
	val heightBonus: Int
)
