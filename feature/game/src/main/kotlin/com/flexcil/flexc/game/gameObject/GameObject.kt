package com.flexcil.flexc.game.gameObject

interface GameObject {
    val x: Float
    val y: Float
    val width: Float
    val height: Float

    fun isCollidingWith(other: GameObject): Boolean {
        val shrinkFactor = 0.15

        val myInsetX = width * shrinkFactor
        val myInsetY = height * shrinkFactor

        val otherInsetX = other.width * shrinkFactor
        val otherInsetY = other.height * shrinkFactor

        return (x + myInsetX) < (other.x + other.width - otherInsetX) &&
                (x + width - myInsetX) > (other.x + otherInsetX) &&
                (y + myInsetY) < (other.y + other.height - otherInsetY) &&
                (y + height - myInsetY) > (other.y + otherInsetY)
    }
}