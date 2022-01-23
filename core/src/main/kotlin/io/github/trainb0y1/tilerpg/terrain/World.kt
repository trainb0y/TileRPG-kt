package io.github.trainb0y1.tilerpg.terrain

/**
 * Holds world-specific data
 */
data class World(
	val id: String,
	val seed: Int,
	val amplitude: Int,
	val wavelength: Int,
	val heightBonus: Int
	)