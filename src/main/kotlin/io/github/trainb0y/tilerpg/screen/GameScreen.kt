package io.github.trainb0y.tilerpg.screen

import box2dLight.DirectionalLight
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ScreenViewport
import io.github.trainb0y.tilerpg.CollisionMasks.LIGHT
import io.github.trainb0y.tilerpg.CollisionMasks.SUNLIGHT_BLOCKING
import io.github.trainb0y.tilerpg.InputListener
import io.github.trainb0y.tilerpg.PhysicsHandler
import io.github.trainb0y.tilerpg.terrain.TerrainHandler
import io.github.trainb0y.tilerpg.terrain.tile.TileLayer
import kotlinx.serialization.json.Json
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.log.Logger

/**
 * The main game screen
 */
class GameScreen(worldId: String) : KtxScreen {
	private val foregroundBatch = SpriteBatch()
	private val backgroundBatch = SpriteBatch()

	companion object {
		val camera = OrthographicCamera(80f, 60f)
		val physics = PhysicsHandler()
		val logger = Logger("Game")
		val json = Json {
			allowStructuredMapKeys = true
			prettyPrint = true
		}
	}

	private val viewport = ScreenViewport(camera)

	init {
		Gdx.app.logLevel = Application.LOG_DEBUG
		Gdx.input.inputProcessor = InputListener()
		viewport.unitsPerPixel = 0.06f // view scaling more or less

		physics.rayHandler.setAmbientLight(0f, 0f, 0f, 0.1f)
		physics.rayHandler.setBlurNum(3)

		// sunlight
		val sun = DirectionalLight(physics.rayHandler, 1000, Color(0f, 0f, 0f, 1f), -90f)
		sun.isSoft = true
		sun.setContactFilter(LIGHT, 0, SUNLIGHT_BLOCKING)
		sun.setSoftnessLength(15f)

		TerrainHandler.loadWorld(worldId)
	}

	override fun resize(width: Int, height: Int) {
		// called on window resize
		viewport.update(width, height)
	}


	override fun render(delta: Float) {
		// possibly step physics
		physics.doPhysicsStep(delta)

		// this whole chunk is concentrated idiocy
		// delete this asap
		if (Gdx.input.isKeyPressed(Input.Keys.W)) camera.position.y += 0.55f
		if (Gdx.input.isKeyPressed(Input.Keys.S)) camera.position.y -= 0.55f
		if (Gdx.input.isKeyPressed(Input.Keys.A)) camera.position.x -= 0.55f
		if (Gdx.input.isKeyPressed(Input.Keys.D)) camera.position.x += 0.55f
		camera.update() // bad idea lol
		TerrainHandler.loadVisibleChunks(camera, 1)

		// background (sky) color
		clearScreen(red = 0.5f, green = 0.6f, blue = 1f)

		// render background tiles
		backgroundBatch.projectionMatrix = camera.combined
		backgroundBatch.setColor(0.7f, 0.7f, 0.7f, 1f)
		backgroundBatch.use { batch ->
			TerrainHandler.chunks.forEach { (_, chunk) ->
				chunk.render(batch, TileLayer.BACKGROUND)
			}
		}

		// render foreground tiles
		foregroundBatch.projectionMatrix = camera.combined
		foregroundBatch.use { batch ->
			TerrainHandler.chunks.forEach { (_, chunk) ->
				chunk.render(batch, TileLayer.FOREGROUND)
			}
		}

		// render lights
		physics.rayHandler.setCombinedMatrix(camera)
		physics.rayHandler.render()

		// physics debug
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			physics.debugRenderer.render(physics.world, camera.combined)
		}
	}

	override fun dispose() {
		// we aren't disposing of tile images!
		foregroundBatch.disposeSafely()
		backgroundBatch.disposeSafely()
		physics.rayHandler.disposeSafely()
	}
}
