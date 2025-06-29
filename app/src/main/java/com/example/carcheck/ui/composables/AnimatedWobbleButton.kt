package com.example.carcheck.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedWobbleButton(
    index: Int,
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    color: Color = Color(0xFFFF9800),
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wobble")

    val direction = if (index % 2 == 0) 1f else -1f

    val offsetX by infiniteTransition.animateFloat(
        initialValue = -2f * direction,
        targetValue = 2f * direction,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetX"
    )

    val buttonColor = if (isSelected) Color(0xFF4CAF50) else color

    // Волна 1
    val waveScale1 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing, delayMillis = 0),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveScale1"
    )
    val waveAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing, delayMillis = 0),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveAlpha1"
    )

    // Волна 2
    val waveScale2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing, delayMillis = 600),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveScale2"
    )
    val waveAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing, delayMillis = 600),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveAlpha2"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(32.dp)
            .graphicsLayer {
                translationX = offsetX
            }
    ) {
        // Анимированные волны
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = waveScale1
                    scaleY = waveScale1
                    alpha = waveAlpha1
                }
                .background(Color(0xFFDC9E47), shape = CircleShape)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = waveScale2
                    scaleY = waveScale2
                    alpha = waveAlpha2
                }
                .background(color.copy(alpha = 0.4f), shape = CircleShape)
        )

        // Кнопка
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(buttonColor, buttonColor.copy(alpha = 0.8f)),
                        radius = 100f
                    )
                )
                .clickable { onClick() }
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
