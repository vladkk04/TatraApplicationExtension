package com.flexcil.flexc.game

import androidx.compose.ui.geometry.Rect

enum class ObstacleType { SAFE, CARD }
enum class SafeState { STATIC, EXPLODING, DESTROYED }

data class GameObject(
    val id: Long = System.nanoTime(),
    val type: ObstacleType,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val rotation: Float = 0f,
    val safeState: SafeState = SafeState.STATIC,
    val reward: Int = 0
) {
    fun getBounds(): Rect = Rect(x, y, x + width, y + height)
}

data class GameState(
    val score: Int = 0,
    val isGameOver: Boolean = false,
    val obstacles: List<GameObject> = emptyList(),
    // Позиція джокера тепер у стані, бо Y змінюється при стрибку
    val jokerX: Float = 0f,
    val jokerY: Float = 0f,
    val jokerWidth: Float = 0f,
    val jokerHeight: Float = 0f
) {
    fun getJokerBounds(): Rect = Rect(jokerX, jokerY, jokerX + jokerWidth, jokerY + jokerHeight)
}