package io.github.trainb0y1.tilerpg

import io.github.trainb0y1.tilerpg.screen.GameScreen
import io.github.trainb0y1.tilerpg.screen.MainMenuScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class TileRPG : KtxGame<KtxScreen>() {
	override fun create() {
		addScreen(MainMenuScreen(this))
		setScreen<MainMenuScreen>()
	}
}

