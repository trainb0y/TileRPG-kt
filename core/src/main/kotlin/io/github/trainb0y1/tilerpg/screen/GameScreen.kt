package io.github.trainb0y1.tilerpg.screen

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.github.trainb0y1.tilerpg.InputListener
import io.github.trainb0y1.tilerpg.terrain.TerrainHandler
import io.github.trainb0y1.tilerpg.terrain.chunk.ChunkLoader
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

/**
 * The main game screen
 */
class GameScreen(worldId: String) : KtxScreen {
	private val batch = SpriteBatch()

	companion object {
		val camera = OrthographicCamera(80f, 60f)
	}

	val logger = FPSLogger()

	init {
		Gdx.app.logLevel = Application.LOG_DEBUG
		Gdx.input.inputProcessor = InputListener()
		TerrainHandler.loadWorld(worldId)
	}


	override fun render(delta: Float) {
		clearScreen(red = 0.3f, green = 0.5f, blue = 0.9f)
		// ChunkLoader.loadVisibleChunks(camera)
		batch.projectionMatrix = camera.combined
		batch.use { batch ->
			// TODO()
		}
		logger.log()
	}

	override fun dispose() {
		// we aren't disposing of tile images!
		batch.disposeSafely()
	}
}