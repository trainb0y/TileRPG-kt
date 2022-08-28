package io.github.trainb0y.tilerpg.terrain

/**
 * Holds world-specific data
 */
data class WorldData(
	val id: String,
	val seed: Int,

	// general terrain shape
	val amplitude: Int,
	val wavelength: Int,
	val heightBonus: Int,

	// caves
	val caveSize: Float,
	val caveCutoff: Float
)
