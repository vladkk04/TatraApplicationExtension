package com.flexcil.flexc.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Dp
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

sealed class CircularSpan(open val angle: Float) {
    data class Angle(override val angle: Float) : CircularSpan(angle)
    data object QuarterCircle : CircularSpan(90f)
    data object HalfCircle : CircularSpan(180f)
    data object WholeCircle: CircularSpan(360f)
}

enum class CircularPlacementDirection(val value: Int) {
    Clockwise(1),
    CounterClockwise(-1)
}

@Composable
fun CircularLayout(
    modifier: Modifier = Modifier,
    overrideRadius: Dp? = null,
    startAngle: Float = 0f,
    span: CircularSpan = CircularSpan.WholeCircle,
    direction: CircularPlacementDirection = CircularPlacementDirection.Clockwise,
    centerContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Layout(
        contents = listOf(centerContent, content),
        modifier = modifier
    ) { (centerMeasurables, contentMeasurables), constraints ->
        require(centerMeasurables.size == 1) { "Center content must be a single composable" }

        val centerPlaceable: Placeable =
            centerMeasurables.first().measure(constraints.copy(minHeight = 0, minWidth = 0))

        val listContentPlaceable: List<Placeable> =
            contentMeasurables.map { it.measure(constraints.copy(minHeight = 0, minWidth = 0)) }

        val overallRadius = overrideRadius?.roundToPx() ?: centerPlaceable.height

        val biggestChildSize = listContentPlaceable.maxOfOrNull { it.height } ?: 0

        val layoutSize = max(centerPlaceable.height, 2 * overallRadius + biggestChildSize)

        layout(layoutSize, layoutSize) {
            val middle = layoutSize / 2

            val angleIncrement = if (listContentPlaceable.size > 1) {
                span.angle / listContentPlaceable.size
            } else {
                0f
            } * direction.value

            centerPlaceable.place(
                middle - centerPlaceable.width / 2,
                middle - centerPlaceable.height / 2
            )

            listContentPlaceable.forEachIndexed { index, placeable ->
                val angle = startAngle + (index * angleIncrement)
                val radians = Math.toRadians(angle.toDouble())
                val radius = overallRadius

                placeable.place(
                    x = (middle + radius * sin(radians) - placeable.height / 2).toInt(),
                    y = (middle - radius * cos(radians) - placeable.width / 2).toInt(),
                )
            }
        }
    }
}