package com.flexcil.flexc.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    playClickSound: Boolean = true,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
) {
    val scope = rememberCoroutineScope()

    Button(
        onClick = {

        },
        colors = buttonColors,
        modifier = modifier
    ) { Text(text = text) }
}

@Composable
fun AppIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
    playClickSound: Boolean = true,
) {
    val scope = rememberCoroutineScope()


    IconButton(
        onClick = {

            onClick()
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun AppFloatingActionButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    playClickSound: Boolean = true,
    shape: Shape = FloatingActionButtonDefaults.shape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    iconSize: Dp = 24.dp,
) {
    val scope = rememberCoroutineScope()


    FloatingActionButton(
        onClick = {
            if (playClickSound) {

            }
            onClick()
        },
        shape = shape,
        containerColor = containerColor,
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun AppButtonCustomBackground(
    onClick: () -> Unit,
    backgroundResId: Int,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .paint(
                painter = painterResource(id = backgroundResId),
                contentScale = ContentScale.FillBounds
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
    }
}

@Composable
fun AppButtonCustomBackground(
    onClick: () -> Unit,
    backgroundResId: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .paint(
                painter = painterResource(id = backgroundResId),
                contentScale = ContentScale.FillBounds
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
    ) {
        Text(text = text)
    }
}
