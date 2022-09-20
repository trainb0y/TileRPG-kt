package io.github.trainb0y.tilerpg

import box2dLight.RayHandler
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import io.github.trainb0y.tilerpg.screen.GameScreen
import io.github.trainb0y.tilerpg.screen.GameScreen.Companion.camera
import io.github.trainb0y.tilerpg.screen.GameScreen.Companion.physics

class PhysicsHandler {
	companion object {
		const val TIME_STEP = 1 / 60f
		const val POS_ITER = 2
		const val VEL_ITER = 6
		private var accumulator = 0f
	}

	var world: World = World(Vector2(0f, -9f), true)
	val debugRenderer = Box2DDebugRenderer(true, true, true, true, true, true)
	val rayHandler = RayHandler(world)

	fun doPhysicsStep(deltaTime: Float) {
		// fixed time step
		// max frame time to avoid spiral of death (on slow devices)
		val frameTime = deltaTime.coerceAtMost(0.25f)
		accumulator += frameTime
		while (accumulator >= TIME_STEP) {
			world.step(TIME_STEP, VEL_ITER, POS_ITER)
			camera.position.set(GameScreen.player.position.x, GameScreen.player.position.y, camera.position.z);
			accumulator -= TIME_STEP
		}
	}
}