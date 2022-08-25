package io.github.trainb0y.tilerpg.screen

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FillViewport
import io.github.trainb0y.tilerpg.InputListener
import io.github.trainb0y.tilerpg.terrain.TerrainHandler
import io.github.trainb0y.tilerpg.terrain.chunk.ChunkLoader
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

	val viewport = FillViewport(80f, 60f, camera)

	init {
		Gdx.app.logLevel = Application.LOG_DEBUG
		Gdx.input.inputProcessor = InputListener()

		TerrainHandler.loadWorld(worldId)
	}

	override fun resize(width: Int, height: Int) {
		viewport.update(width, height)
	}


	override fun render(delta: Float) {
		clearScreen(red = 0.5f, green = 0.6f, blue = 1f)


		// this whole chunk is concentrated idiocy
		// delete this asap
		if (Gdx.input.isKeyPressed(Input.Keys.W)) camera.position.y += 0.55f
		if (Gdx.input.isKeyPressed(Input.Keys.S)) camera.position.y -= 0.55f
		if (Gdx.input.isKeyPressed(Input.Keys.A)) camera.position.x -= 0.55f
		if (Gdx.input.isKeyPressed(Input.Keys.D)) camera.position.x += 0.55f
		camera.update() // bad idea lol
		ChunkLoader.loadVisibleChunks(camera, 1)


		batch.projectionMatrix = camera.combined
		batch.use { batch ->
			TerrainHandler.chunks.forEach { (_, chunk) ->
				chunk.render(batch)
			}
		}
		// logger.log()
	}

	override fun dispose() {
		// we aren't disposing of tile images!
		batch.disposeSafely()
	}
}
