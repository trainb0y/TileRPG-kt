package io.github.trainb0y1.tilerpg

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import io.github.trainb0y1.tilerpg.FirstScreen.Companion.camera

class InputListener: InputProcessor {
	override fun keyDown(keycode: Int): Boolean {
		when (keycode) {
			Input.Keys.A -> camera.translate(-5f, 0f)
			Input.Keys.D -> camera.translate(5f, 0f)
			Input.Keys.S -> camera.translate(0f, -5f)
			Input.Keys.W -> camera.translate(0f, 5f)
		}
		camera.update()
		return true
	}

	override fun keyUp(keycode: Int): Boolean = false
	override fun keyTyped(character: Char): Boolean = false
	override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
	override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
	override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false
	override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
	override fun scrolled(amountX: Float, amountY: Float): Boolean = false
}