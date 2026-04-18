package com.flexcil.flexc.game

import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import com.flexcil.flexc.core.audio.AppAudioManager
import com.flexcil.flexc.core.haptics.AppHaptic
import com.flexcil.flexc.core.haptics.VibrationPattern
import com.flexcil.flexc.core.navigation.Navigator
import com.flexcil.flexc.core.navigation.AppScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(
    private val appAudioManager: AppAudioManager,
    private val appHaptic: AppHaptic,
    private val navigator: Navigator
) : ViewModel() {

    // --- НАЛАШТУВАННЯ ГРИ (КОНФІГ) ---
    companion object GameConfig {
        // --- ДЖОКЕР ---
        const val JOKER_WIDTH_RATIO = 0.15f
        const val JOKER_HEIGHT_ASPECT = 1.2f
        const val JOKER_START_X_RATIO = 0.1f
        const val JOKER_GROUND_Y_RATIO = 0.95f

        // --- СЕЙФ ---
        const val SAFE_SIZE_RATIO = 0.7f
        const val SAFE_GROUND_Y_RATIO = 0.97f

        // >> ШВИДКІСТЬ СЕЙФУ <<
        const val SAFE_INITIAL_SPEED = 7f
        const val SAFE_MAX_SPEED = 13f
        const val SAFE_SPEED_INCREASE = 0.5f

        // --- КАРТКА ---
        const val CARD_SIZE_RATIO = 0.4f
        const val CARD_FLIGHT_Y_RATIO = 0.8f
        const val CARD_ROTATION_SPEED = 15f

        // >> ШВИДКІСТЬ КАРТКИ <<
        const val CARD_INITIAL_SPEED = 10f
        const val CARD_MAX_SPEED = 24f
        const val CARD_SPEED_INCREASE = 0.8f

        // --- СПАВН (ЗАТРИМКА) ---
        // Мінімальна затримка між ворогами (в кадрах) на старті
        const val SPAWN_MIN_INTERVAL = 150L
        // Максимальна затримка між ворогами (в кадрах) на старті
        const val SPAWN_MAX_INTERVAL = 300L
        // Абсолютний ліміт (щоб не спавнились частіше ніж раз в 60 кадрів навіть на складних рівнях)
        const val SPAWN_HARD_LIMIT = 120L
        // На скільки кадрів зменшувати очікування за кожен рівень складності
        const val SPAWN_DECREASE_STEP = 20L

        // --- СКЛАДНІСТЬ ТА ІНШЕ ---
        const val SPEED_INCREASE_SCORE_STEP = 300
        const val SPAWN_OFFSET_X = 100f
        const val GRAVITY_FORCE = 0.8f
        const val JUMP_FORCE = -35f

        const val HITBOX_MARGIN = 30f
        const val SCORE_DIFFICULTY_STEP = 500

        const val MIN_REWARD = 10
        const val MAX_REWARD = 50
    }

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private var jokerVelocityY = 0f
    private var jokerGroundYLimit = 0f
    private var isJumping = false

    // Таймери для спавну
    private var timeSinceLastSpawn = 0L
    private var nextSpawnDelay = 0L // Скільки треба чекати до наступного ворога

    fun navigateToMenu() {
        navigator.launchScreen(AppScreen.MenuScreen)
    }

    fun initGame(width: Float, height: Float) {
        val jokerW = width * JOKER_WIDTH_RATIO
        val jokerH = jokerW * JOKER_HEIGHT_ASPECT
        val startX = width * JOKER_START_X_RATIO

        jokerGroundYLimit = (height * JOKER_GROUND_Y_RATIO) - jokerH

        // Скидаємо таймери
        timeSinceLastSpawn = 0
        // Перший ворог з'явиться десь посередині діапазону
        nextSpawnDelay = (SPAWN_MIN_INTERVAL + SPAWN_MAX_INTERVAL) / 2

        _state.update {
            GameState(
                score = 0,
                isGameOver = false,
                obstacles = emptyList(),
                jokerX = startX,
                jokerY = jokerGroundYLimit,
                jokerWidth = jokerW,
                jokerHeight = jokerH
            )
        }
    }

    fun jump() {
        if (!isJumping && !_state.value.isGameOver) {
            jokerVelocityY = JUMP_FORCE
            isJumping = true
        }
    }

    fun updateGameWithFrames(screenWidth: Float, screenHeight: Float) {
        if (_state.value.isGameOver) return

        _state.update { currentState ->
            // 1. Фізика Джокера
            var newJokerY = currentState.jokerY + jokerVelocityY
            jokerVelocityY += GRAVITY_FORCE

            if (newJokerY >= jokerGroundYLimit) {
                newJokerY = jokerGroundYLimit
                jokerVelocityY = 0f
                isJumping = false
            }

            // 2. Спавн з рандомною затримкою
            val newObstacles = currentState.obstacles.toMutableList()
            timeSinceLastSpawn++

            // Якщо час прийшов
            if (timeSinceLastSpawn >= nextSpawnDelay) {
                timeSinceLastSpawn = 0
                newObstacles.add(spawnObstacle(screenWidth, screenHeight))

                // === РОЗРАХУНОК НАСТУПНОЇ ЗАТРИМКИ ===
                // Рівень складності по спавну
                val difficultyLevel = currentState.score / SCORE_DIFFICULTY_STEP

                // Зменшуємо час очікування залежно від рівня
                val reduction = difficultyLevel * SPAWN_DECREASE_STEP

                // Розраховуємо нові межі (але не менше HARD_LIMIT)
                val currentMin = (SPAWN_MIN_INTERVAL - reduction).coerceAtLeast(SPAWN_HARD_LIMIT)
                val currentMax = (SPAWN_MAX_INTERVAL - reduction).coerceAtLeast(currentMin + 40) // +40 щоб завжди був хоч якийсь розкид

                // Вибираємо випадкове число між Мін і Макс
                nextSpawnDelay = Random.nextLong(currentMin, currentMax)
            }

            // === РОЗРАХУНОК ПОТОЧНОЇ ШВИДКОСТІ ===
            val speedDifficultyLevel = currentState.score / SPEED_INCREASE_SCORE_STEP

            val currentSafeSpeed = (SAFE_INITIAL_SPEED + (speedDifficultyLevel * SAFE_SPEED_INCREASE))
                .coerceAtMost(SAFE_MAX_SPEED)

            val currentCardSpeed = (CARD_INITIAL_SPEED + (speedDifficultyLevel * CARD_SPEED_INCREASE))
                .coerceAtMost(CARD_MAX_SPEED)


            // 3. Обробка об'єктів
            val iterator = newObstacles.listIterator()
            var gameOver = false
            var additionalScore = 0

            val tempJokerRect = Rect(
                currentState.jokerX,
                newJokerY,
                currentState.jokerX + currentState.jokerWidth,
                newJokerY + currentState.jokerHeight
            )
            val jokerHitBox = tempJokerRect.deflate(HITBOX_MARGIN)

            while (iterator.hasNext()) {
                val obj = iterator.next()

                val objectSpeed = if (obj.type == ObstacleType.CARD) {
                    currentCardSpeed
                } else {
                    currentSafeSpeed
                }

                val newX = obj.x - objectSpeed
                val newRotation = if (obj.type == ObstacleType.CARD) obj.rotation + CARD_ROTATION_SPEED else 0f
                val updatedObj = obj.copy(x = newX, rotation = newRotation)

                iterator.set(updatedObj)

                val obstacleHitBox = updatedObj.getBounds().deflate(HITBOX_MARGIN)

                if (obstacleHitBox.overlaps(jokerHitBox)) {
                    if (updatedObj.type == ObstacleType.SAFE && updatedObj.safeState == SafeState.EXPLODING) {
                        additionalScore += updatedObj.reward
                        iterator.remove()
                        continue
                    }
                    else if (updatedObj.safeState != SafeState.DESTROYED) {
                        gameOver = true
                    }
                }

                if (newX + obj.width < 0) {
                    iterator.remove()
                }
            }

            currentState.copy(
                obstacles = newObstacles,
                isGameOver = gameOver,
                score = currentState.score + additionalScore,
                jokerY = newJokerY
            )
        }
    }

    private fun spawnObstacle(screenWidth: Float, screenHeight: Float): GameObject {
        val isCard = Random.nextBoolean()

        val sizeRatio = if (isCard) CARD_SIZE_RATIO else SAFE_SIZE_RATIO
        val size = _state.value.jokerWidth * sizeRatio

        val startY: Float

        if (isCard) {
            startY = screenHeight * CARD_FLIGHT_Y_RATIO
        } else {
            startY = (screenHeight * SAFE_GROUND_Y_RATIO) - size
        }

        return GameObject(
            type = if (isCard) ObstacleType.CARD else ObstacleType.SAFE,
            x = screenWidth + SPAWN_OFFSET_X,
            y = startY,
            width = size,
            height = size
        )
    }

    fun onSafeClicked(obstacleId: Long) {
        _state.update { currentState ->
            val newObstacles = currentState.obstacles.map { obj ->
                if (obj.id == obstacleId && obj.type == ObstacleType.SAFE && obj.safeState == SafeState.STATIC) {
                    appHaptic.vibratePattern(VibrationPattern.Hit)

                    val rewardPoints = Random.nextInt(MIN_REWARD, MAX_REWARD + 1)
                    obj.copy(safeState = SafeState.EXPLODING, reward = rewardPoints)
                } else {
                    obj
                }
            }
            currentState.copy(obstacles = newObstacles)
        }
    }
}