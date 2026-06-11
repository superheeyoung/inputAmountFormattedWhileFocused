package com.superheeyoung.core.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.superheeyoung.core.ui.appColors
import com.superheeyoung.core.ui.typography

@Composable
fun LargeHeading(
    modifier: Modifier = Modifier,
    onBackClose: () -> Unit,
    title: String
) {
    Column(Modifier.statusBarsPadding()) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackIcon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 14.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onBackClose
                        )
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.height(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = title,
                    style = typography,
                    color = appColors.gray7
                )
            }
        }
    }
}

@Composable
private fun BackIcon(
    modifier: Modifier = Modifier
) {
    val color = appColors.gray7
    Canvas(modifier = modifier) {
        val strokeWidth = 2.dp.toPx()
        drawLine(
            color = color,
            start = Offset(size.width * 0.65f, size.height * 0.2f),
            end = Offset(size.width * 0.35f, size.height * 0.5f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.35f, size.height * 0.5f),
            end = Offset(size.width * 0.65f, size.height * 0.8f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}
