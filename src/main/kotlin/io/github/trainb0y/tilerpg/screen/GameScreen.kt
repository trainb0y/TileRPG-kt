package io.github.trainb0y.tilerpg.screen

import box2dLight.ConeLight
import box2dLight.DirectionalLight
import box2dLight.RayHandler
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ScreenViewport
import io.github.trainb0y.tilerpg.InputListener
import io.github.trainb0y.tilerpg.PhysicsHandler
import io.github.trainb0y.tilerpg.terrain.TerrainHandler
import io.github.trainb0y.tilerpg.terrain.chunk.ChunkLoader
import io.github.trainb0y.tilerpg.terrain.tile.TileLayer
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

/**
 * The main game screen
 */
class GameScreen(worldId: String) : KtxScreen {
	private val foregroundBatch = SpriteBatch()
	private val backgroundBatch = SpriteBatch()

	companion object {
		val camera = OrthographicCamera(80f, 60f)
		val physics = PhysicsHandler()
		val rayHandler = RayHandler(physics.world)
		val light = ConeLight(rayHandler, 100, Color.GOLDENROD, 30f, 10f ,20f, -70f, 23f)
	}

	val viewport = ScreenViewport(camera)

	init {
		Gdx.app.logLevel = Application.LOG_DEBUG
		Gdx.input.inputProcessor = InputListener()
		viewport.unitsPerPixel = 0.06f // view scaling more or less

		rayHandler.setAmbientLight(0f, 0f, 0f, 0.1f);
		rayHandler.setBlurNum(3)

		// sunlight
		val sun = DirectionalLight(rayHandler, 1000, Color(0f, 0f, 0f, 1f), -90f)
		sun.isSoft = true
		sun.setSoftnessLength(15f)

		TerrainHandler.loadWorld(worldId)
	}

	override fun resize(width: Int, height: Int) {
		viewport.update(width, height)
	}


	override fun render(delta: Float) {
		physics.doPhysicsStep(delta)

		// this whole chunk is concentrated idiocy
		// delete this asap
		if (Gdx.input.isKeyPressed(Input.Keys.W)) camera.position.y += 0.55f
		if (Gdx.input.isKeyPressed(Input.Keys.S)) camera.position.y -= 0.55f
		if (Gdx.input.isKeyPressed(Input.Keys.A)) camera.position.x -= 0.55f
		if (Gdx.input.isKeyPressed(Input.Keys.D)) camera.position.x += 0.55f
		camera.update() // bad idea lol
		ChunkLoader.loadVisibleChunks(camera, 1)

		clearScreen(red = 0.5f, green = 0.6f, blue = 1f)


		// debugRenderer.render(world, camera.combined)
		backgroundBatch.projectionMatrix = camera.combined
		backgroundBatch.setColor(0.7f, 0.7f, 0.7f, 1f)
		backgroundBatch.use { batch ->
			TerrainHandler.chunks.forEach { (_, chunk) ->
				chunk.render(batch, TileLayer.BACKGROUND)
			}
		}

		foregroundBatch.projectionMatrix = camera.combined
		foregroundBatch.use { batch ->
			TerrainHandler.chunks.forEach { (_, chunk) ->
				chunk.render(batch, TileLayer.FOREGROUND)
			}
		}
		// logger.log()
		rayHandler.setCombinedMatrix(camera)

		// todo: only update if physics updated (just render())
		rayHandler.updateAndRender()

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			physics.debugRenderer.render(physics.world, camera.combined)
		}

	}

	override fun dispose() {
		// we aren't disposing of tile images!
		foregroundBatch.disposeSafely()
		backgroundBatch.disposeSafely()
		rayHandler.disposeSafely()
	}
}
