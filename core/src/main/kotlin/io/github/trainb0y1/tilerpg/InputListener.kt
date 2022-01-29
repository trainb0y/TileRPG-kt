package io.github.trainb0y1.tilerpg

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector3
import io.github.trainb0y1.tilerpg.screen.GameScreen.Companion.camera
import io.github.trainb0y1.tilerpg.terrain.Position
import io.github.trainb0y1.tilerpg.terrain.chunk.ChunkLoader
import io.github.trainb0y1.tilerpg.terrain.tile.TileData
import io.github.trainb0y1.tilerpg.terrain.tile.Tiles
import ktx.log.info

class InputListener : InputProcessor {
	override fun keyDown(keycode: Int): Boolean {
		when (keycode) {
			Input.Keys.A -> camera.translate(-5f, 0f)
			Input.Keys.D -> camera.translate(5f, 0f)
			Input.Keys.S -> camera.translate(0f, -5f)
			Input.Keys.W -> camera.translate(0f, 5f)
		}
		camera.update()
		ChunkLoader.loadVisibleChunks(camera)
		return true
	}

	override fun keyUp(keycode: Int): Boolean = false
	override fun keyTyped(character: Char): Boolean = false
	override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
		val cameraPos = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(),0f))
		val pos = Position(cameraPos.x, cameraPos.y)
		when (button) {
			Input.Buttons.RIGHT -> {
				pos.setTile(TileData(Tiles.STONE))
				info {"stone (${pos.x},${pos.y})"}
			}
			Input.Buttons.LEFT -> {
				pos.setTile(null)
				info {"null (${pos.x},${pos.y})"}
			}
		}
		return true
	}
	override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
	override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false
	override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
	override fun scrolled(amountX: Float, amountY: Float): Boolean = false
}