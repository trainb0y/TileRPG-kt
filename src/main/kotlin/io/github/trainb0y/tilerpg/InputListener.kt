package io.github.trainb0y.tilerpg

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector3
import io.github.trainb0y.tilerpg.screen.GameScreen.Companion.camera
import io.github.trainb0y.tilerpg.terrain.TilePosition
import io.github.trainb0y.tilerpg.terrain.tile.Tile
import io.github.trainb0y.tilerpg.terrain.tile.TileData
import io.github.trainb0y.tilerpg.terrain.tile.TileLayer
import ktx.log.info
import kotlin.math.roundToInt

class InputListener : InputProcessor {
	override fun keyDown(keycode: Int): Boolean = false
	override fun keyUp(keycode: Int): Boolean = false
	override fun keyTyped(character: Char): Boolean = false
	override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
		val cameraPos = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
		val pos = TilePosition(cameraPos.x.roundToInt(), cameraPos.y.roundToInt())
		when (button) {
			Input.Buttons.RIGHT -> {
				pos.setTile(TileData(Tile.SAND), layer=TileLayer.FOREGROUND)
				info { "placed (${pos.x},${pos.y})" }
			}

			Input.Buttons.LEFT -> {
				pos.setTile(null, layer=TileLayer.FOREGROUND)
				info { "removed (${pos.x},${pos.y})" }
			}
		}
		return true
	}

	override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
	override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false
	override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
	override fun scrolled(amountX: Float, amountY: Float): Boolean = false
}
