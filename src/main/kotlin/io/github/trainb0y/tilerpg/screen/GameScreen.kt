package io.github.trainb0y.tilerpg.screen

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.ScreenViewport
import io.github.trainb0y.tilerpg.InputListener
import io.github.trainb0y.tilerpg.PhysicsHandler
import io.github.trainb0y.tilerpg.terrain.TerrainHandler
import io.github.trainb0y.tilerpg.terrain.chunk.ChunkLoader
import io.github.trainb0y.tilerpg.terrain.tile.TileLayer
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.graphics.use

/**
 * The main game screen
 */
class GameScreen(worldId: String) : KtxScreen {
	private val foregroundBatch = SpriteBatch()
	private val backgroundBatch = SpriteBatch()
	var b2dworld: World
	var debugRenderer: Box2DDebugRenderer

	companion object {
		val camera = OrthographicCamera(80f, 60f)
	}

	val viewport = ScreenViewport(camera)

	init {
		Gdx.app.logLevel = Application.LOG_DEBUG
		Gdx.input.inputProcessor = InputListener()
		b2dworld = createWorld(gravity = Vector2(0f, -0.5f))
		viewport.unitsPerPixel = 0.1f // view scaling more or less
		debugRenderer = Box2DDebugRenderer(true, true, true, true, true, true)
		TerrainHandler.loadWorld(worldId)
	}

	override fun resize(width: Int, height: Int) {
		viewport.update(width, height)
	}


	override fun render(delta: Float) {
		PhysicsHandler.doPhysicsStep(b2dworld, delta)
		clearScreen(red = 0.5f, green = 0.6f, blue = 1f)

		// this whole chunk is concentrated idiocy
		// delete this asap
		if (Gdx.input.isKeyPressed(Input.Keys.W)) camera.position.y += 0.55f
		if (Gdx.input.isKeyPressed(Input.Keys.S)) camera.position.y -= 0.55f
		if (Gdx.input.isKeyPressed(Input.Keys.A)) camera.position.x -= 0.55f
		if (Gdx.input.isKeyPressed(Input.Keys.D)) camera.position.x += 0.55f
		camera.update() // bad idea lol
		ChunkLoader.loadVisibleChunks(camera, 1)


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
	}

	override fun dispose() {
		// we aren't disposing of tile images!
		foregroundBatch.disposeSafely()
		backgroundBatch.disposeSafely()
	}
}
