package io.github.trainb0y1.tilerpg

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.github.trainb0y1.tilerpg.terrain.TerrainHandler
import io.github.trainb0y1.tilerpg.terrain.vector2FromInt
import io.github.trainb0y1.tilerpg.terrain.tile.Tiles
import io.github.trainb0y1.tilerpg.terrain.noise.OpenSimplex2
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use
import kotlin.random.Random


class TileRPG : KtxGame<KtxScreen>() {
	override fun create() {
		addScreen(FirstScreen())
		setScreen<FirstScreen>()
	}
}

class FirstScreen : KtxScreen {
	private val batch = SpriteBatch()

	companion object {
		val camera = OrthographicCamera(80f, 60f)
	}

	val logger = FPSLogger()

	// Worldgen settings
	val seed: Long = Random.nextLong(0, 100)
	val amplitude = 8
	val wavelength = 40
	val heightBonus = 20

	init {
		Gdx.app.logLevel = LOG_DEBUG
		generateTerrain(100)
		Gdx.input.inputProcessor = InputListener()
	}

	fun generateTerrain(width: Int) {
		for (x in 0..width) {
			val maxHeight =
				(OpenSimplex2.noise2(seed, x.toDouble() / wavelength, 0.0) * amplitude).toInt() + heightBonus
			for (y in 0..maxHeight) {
				TerrainHandler.setTile(vector2FromInt(x,y), Tiles.STONE)
			}
		}
	}

	override fun render(delta: Float) {
		clearScreen(red = 0.3f, green = 0.5f, blue = 0.9f)
		batch.projectionMatrix = camera.combined
		batch.use { batch ->
			TODO()
		}
		logger.log()
	}

	override fun dispose() {
		// we aren't disposing of tile images!
		batch.disposeSafely()
	}
}
