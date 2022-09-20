package io.github.trainb0y.tilerpg.screen

import box2dLight.ConeLight
import box2dLight.DirectionalLight
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.BodyDef
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
import ktx.async.KtxAsync
import ktx.box2d.body
import ktx.box2d.circle
import ktx.graphics.use
import ktx.log.Logger

/**
 * The main game screen
 */
class GameScreen(worldId: String) : KtxScreen {
	private val foregroundBatch = SpriteBatch()
	private val backgroundBatch = SpriteBatch()

	companion object {
		var renderPhysicsDebug = false;
		var renderLighting = true;
		var renderTextures = true;

		val camera = OrthographicCamera(80f, 60f)
		val physics = PhysicsHandler()
		val logger = Logger("Game")
		val viewport = ScreenViewport(camera)
		val json = Json {
			allowStructuredMapKeys = true
			prettyPrint = true
		}
		val player = physics.world.body {
			circle(1f) {
				restitution = 0.5f
				density = 1f
			}
			type = BodyDef.BodyType.DynamicBody
			position.set(0f, 30f)
			fixedRotation = true;
		}
		var freecamEnabled: Boolean = false;

		val flashlight = ConeLight(
			physics.rayHandler,
			300,
			Color(160f, 150f, 140f, 1f),
			30f,
			player.position.x,
			player.position.y,
			0f,
			30f
		)
	}

	init {
		Gdx.app.logLevel = Application.LOG_DEBUG
		Gdx.input.inputProcessor = InputListener()

		KtxAsync.initiate()

		viewport.unitsPerPixel = 0.06f // view scaling more or less

		physics.rayHandler.setAmbientLight(0f, 0f, 0f, 0.1f)
		physics.rayHandler.setBlurNum(3)

		// sunlight
		val sun = DirectionalLight(physics.rayHandler, 1000, Color(0f, 0f, 0f, 1f), -90f)
		sun.isSoft = true
		sun.setContactFilter(LIGHT, 0, SUNLIGHT_BLOCKING)
		sun.setSoftnessLength(15f)
		// sun.isStaticLight = true

		TerrainHandler.loadWorld(worldId)

		//flashlight.attachToBody(player)
		flashlight.setSoftnessLength(1f)
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

		val mousePos = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
		val relative = Vector2(mousePos.x, mousePos.y).sub(player.position).nor()
		// println(relative.angleDeg())
		flashlight.setDirection(relative.angleDeg())
		flashlight.setPosition(player.position.x, player.position.y)
		flashlight.isActive = Gdx.input.isKeyPressed(Input.Keys.F)
		// flashlight.update() // updated with rayhandler.update()

		val mag = 2000f * delta
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) player.applyForceToCenter(0f, 1 * mag, true)
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) player.applyForceToCenter(0f, -1 * mag, true)
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.applyForceToCenter( -1 * mag, 0f, true)
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.applyForceToCenter( 1 * mag, 0f, true)

		if (freecamEnabled) {
			val speed = 2
			if (Gdx.input.isKeyPressed(Input.Keys.W)) camera.position.y += 0.55f * speed
			if (Gdx.input.isKeyPressed(Input.Keys.S)) camera.position.y -= 0.55f * speed
			if (Gdx.input.isKeyPressed(Input.Keys.A)) camera.position.x -= 0.55f * speed
			if (Gdx.input.isKeyPressed(Input.Keys.D)) camera.position.x += 0.55f * speed
		}
		else {
			// moved to physics tick
			// camera.position.set(player.position.x, player.position.y, camera.position.z);
		}
		camera.update() // bad idea lol
		TerrainHandler.loadVisibleChunks(camera, 1)

		// background (sky) color
		if (renderTextures || renderLighting || !renderPhysicsDebug)
			clearScreen(red = 0.5f, green = 0.6f, blue = 1f)

		if (renderTextures) {
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

			val playerRenderer = ShapeRenderer()
			playerRenderer.use(ShapeRenderer.ShapeType.Filled, camera) {
				it.circle(player.position.x, player.position.y, 1f)
			}

		}

		// render lighting
		if (renderLighting) {
			physics.rayHandler.setCombinedMatrix(camera)
			physics.rayHandler.updateAndRender()
		}

		// physics debug
		if (renderPhysicsDebug) {
			physics.debugRenderer.render(physics.world, camera.combined)
		}
	}

	override fun dispose() {
		// we aren't disposing of tile images or the sun!
		foregroundBatch.disposeSafely()
		backgroundBatch.disposeSafely()
		physics.rayHandler.disposeSafely()
		flashlight.disposeSafely()
	}
}
