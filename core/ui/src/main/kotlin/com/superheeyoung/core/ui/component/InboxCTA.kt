package com.superheeyoung.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.superheeyoung.core.ui.appColors
import com.superheeyoung.core.ui.spacing

@Composable
fun InboxCTA(
    modifier: Modifier,
    bottomCta: @Composable () -> Unit,
    content: @Composable (bottomPadding: Dp) -> Unit
) {
    val ctaContainerColor = appColors.white
    val topPadding = 20.dp
    val density = LocalDensity.current
    val gradientEndY = with(density) { topPadding.toPx() }
    var ctaHeight by remember { mutableStateOf(0.dp) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content(ctaHeight)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(verticalGradient(ctaContainerColor, gradientEndY))
                .onGloballyPositioned { coordinates ->
                    ctaHeight = with(density) { coordinates.size.height.toDp() }
                }
                .padding(top = topPadding, bottom = spacing.ctaBottomPadding)
        ) {
            bottomCta()
        }
    }
}

private fun verticalGradient(
    containerColor: Color,
    endY: Float
): Brush {
    return Brush.verticalGradient(
        colors = listOf(
            containerColor.copy(alpha = 0f),
            containerColor
        ),
        startY = 0f,
        endY = endY
    )
}
