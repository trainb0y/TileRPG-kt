package io.github.trainb0y1.tilerpg.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely


/**
 * Menu where the player selects to generate a new world or load an existing one
 */
class MainMenuScreen : KtxScreen {
	// https://github.com/raeleus/skin-composer/wiki/From-the-Ground-Up%3A-Scene2D.UI-Tutorials
	private val stage = Stage()
	private var table = Table()

	fun create() {
		Gdx.input.inputProcessor = stage
		table.setFillParent(true)
		stage.addActor(table)
		table.debug = true // This is optional, but enables debug lines for tables.

		// Add widgets to the table here.
	}

	override fun resize(width: Int, height: Int) {
		stage.getViewport().update(width, height, true)
	}

	fun render() {
		clearScreen(red = 0.3f, green = 0.5f, blue = 0.9f)
		stage.act(Gdx.graphics.deltaTime)
		stage.draw()
	}

	override fun dispose() {
		stage.disposeSafely()
	}
}