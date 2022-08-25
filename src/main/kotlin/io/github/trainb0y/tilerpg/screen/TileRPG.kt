package io.github.trainb0y.tilerpg.screen

import ktx.app.KtxGame
import ktx.app.KtxScreen

class TileRPG : KtxGame<KtxScreen>() {
	override fun create() {
		addScreen(MainMenuScreen(this))
		setScreen<MainMenuScreen>()
	}
}

