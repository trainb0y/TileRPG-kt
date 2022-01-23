package io.github.trainb0y1.tilerpg

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import io.github.trainb0y1.tilerpg.terrain.TileType
import io.github.trainb0y1.tilerpg.terrain.noise.OpenSimplex2
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use
import kotlin.random.Random


class TileRPG : KtxGame<KtxScreen>() {
    override fun create() {
        addScreen(FirstScreen())
        setScreen<FirstScreen>()
    }
}

class FirstScreen : KtxScreen {
    private val batch = SpriteBatch()

    companion object {
        val camera = OrthographicCamera(80f, 60f)
        val map = mutableMapOf<Vector2, TileType>()
        val tileSize = 8 // 8x8 px tiles
        val seed: Long = Random.nextLong(0, 100000)
    }

    val logger = FPSLogger()

    // Worldgen settings
    val amplitude = 8
    val wavelength = 40
    val heightBonus = 20

    init {
        Gdx.app.logLevel = LOG_DEBUG
        generateTerrain(100)
        Gdx.input.inputProcessor = InputListener()
    }

    fun generateTerrain(width: Int) {
        for (x in 0..width) {
            val maxHeight = (OpenSimplex2.noise2(seed, x.toDouble()/wavelength, 0.0) * amplitude).toInt() + heightBonus
            for (y in 0..maxHeight) {
               placeTile(x, y, TileType("stone", Texture("tiles/stone.png".toInternalFile())))
            }
        }
    }

    fun placeTile(x: Int, y: Int, type: TileType) {
        map[Vector2(x.toFloat(),y.toFloat())] = type
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.3f, green = 0.5f, blue = 0.9f)
        batch.projectionMatrix = camera.combined
        batch.use { batch ->
            map.keys.forEach{ v ->
                batch.draw(map[v]!!.tex, v.x, v.y, 1f, 1f,)
            }
        }
        logger.log()
    }

    override fun dispose() {
        // we aren't disposing of tile images!
        batch.disposeSafely()
    }
}
