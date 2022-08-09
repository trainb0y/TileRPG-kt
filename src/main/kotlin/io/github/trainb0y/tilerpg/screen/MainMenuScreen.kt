package io.github.trainb0y.tilerpg.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile


/**
 * Menu where the player selects to generate a new world or load an existing one
 */
class MainMenuScreen(val game: TileRPG) : KtxScreen {
	var init = false // Dumb. Dumb. Dumb.

	// https://github.com/raeleus/skin-composer/wiki/From-the-Ground-Up%3A-Scene2D.UI-Tutorials
	private val stage = Stage(ScreenViewport())
	private var root = Table()
	private val skin = Skin("assets/ui/default/uiskin.json".toInternalFile())

	fun create() {
		Gdx.input.inputProcessor = stage
		root.setFillParent(true)
		stage.addActor(root)

		// Add widgets to the table here.
		val nameField = TextField("World Name", skin)
		root.add(nameField)
		val button = TextButton("Go", skin)
		root.add(button).pad(10f)
		button.addListener(object : ChangeListener() {
			override fun changed(event: ChangeEvent, actor: Actor) {
				if (nameField.text != "") {
					game.addScreen(GameScreen(nameField.text.replace(" ", "_", true)))
					game.setScreen<GameScreen>()
				}
			}
		})
	}

	override fun resize(width: Int, height: Int) {
		stage.viewport.update(width, height, true)
	}

	override fun render(delta: Float) {
		if (!init) {
			create()
			init = true
		}
		clearScreen(red = 0.9f, green = 0.9f, blue = 0.9f)
		// Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT) // Find out what this does!
		stage.act(Gdx.graphics.deltaTime)
		stage.draw()
	}

	override fun dispose() {
		skin.disposeSafely()
		stage.disposeSafely()
	}
}
