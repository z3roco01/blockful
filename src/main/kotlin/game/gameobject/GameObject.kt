package game.gameobject

import z3roco01.blockful.math.Transformation

/**
 * inherited by everything that has a position, rotation or scale in the world
 */
open class GameObject(transformation: Transformation) {
    constructor(): this(Transformation())

    val transformation = transformation
}