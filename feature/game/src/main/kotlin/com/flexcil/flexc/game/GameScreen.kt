package com.flexcil.flexc.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flexcil.flexc.feature.game.R
import com.flexcil.flexc.game.component.GameOverDialog
import com.flexcil.flexc.core.ui.component.AppImageGif
import com.flexcil.flexc.core.ui.component.VideoPlayer
import kotlin.math.roundToInt

@Composable
fun GameScreen() {
    val viewModel = hiltViewModel<GameViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Щоб прибрати "хвилі" при кліку на екран для стрибка
    val interactionSource = remember { MutableInteractionSource() }

    BackHandler { viewModel.navigateToMenu() }

    if (state.isGameOver) {
        GameOverDialog(
            score = state.score,
            onBackToMenu = viewModel::navigateToMenu,
        )
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            // Клік по всьому екрану викликає стрибок
            .clickable(interactionSource = interactionSource, indication = null) {
                viewModel.jump()
            }
    ) {
        VideoPlayer(
            R.raw.ff,
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleX = 1.4f, scaleY = 1f)
        )

        val density = LocalDensity.current
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        LaunchedEffect(Unit) {
            viewModel.initGame(widthPx, heightPx)
            while (!state.isGameOver) {
                withFrameNanos {
                    viewModel.updateGameWithFrames(widthPx, heightPx)
                }
            }
        }

        Text(
            text = "Score: ${state.score}",
            modifier = Modifier.align(Alignment.TopCenter).offset(y = 32.dp),
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        if (state.jokerWidth > 0) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(state.jokerX.roundToInt(), state.jokerY.roundToInt()) }
                    .size(
                        with(density) { state.jokerWidth.toDp() },
                        with(density) { state.jokerHeight.toDp() }
                    )
            ) {
                AppImageGif(
                    drawableResId = R.drawable.joker,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // --- ПЕРЕШКОДИ ---
        state.obstacles.forEach { obstacle ->
            Box(
                modifier = Modifier
                    .offset { IntOffset(obstacle.x.roundToInt(), obstacle.y.roundToInt()) }
                    .size(with(density) { obstacle.width.toDp() })
            ) {
                when (obstacle.type) {
                    ObstacleType.CARD -> {
                        Image(
                            painter = painterResource(id = R.drawable.card),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .rotate(obstacle.rotation) // Картка крутиться
                        )
                    }
                    ObstacleType.SAFE -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                // Клік по сейфу має пріоритет над кліком по екрану (стрибком)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    enabled = obstacle.safeState == SafeState.STATIC
                                ) {
                                    viewModel.onSafeClicked(obstacle.id)
                                }
                        ) {
                            if (obstacle.safeState == SafeState.STATIC) {
                                Image(
                                    painter = painterResource(id = R.drawable.bank_static),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                AppImageGif(
                                    drawableResId = R.drawable.bank,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Text(
                                    text = "+${obstacle.reward}",
                                    color = Color.Yellow,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}