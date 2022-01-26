package io.github.trainb0y1.tilerpg

import com.badlogic.gdx.math.Vector2
import io.github.trainb0y1.tilerpg.terrain.Position
import io.github.trainb0y1.tilerpg.terrain.tile.TileType

fun Vector2.toPosition(): Position = Position(this.x, this.y)