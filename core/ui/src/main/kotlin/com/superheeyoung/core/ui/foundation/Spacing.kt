package com.superheeyoung.core.ui.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class Spacing(
    val screenHorizontalPadding: Dp,
    val ctaBottomPadding: Dp,
    val inputDividerHeight: Dp
)

internal fun createDefaultSpacing(): Spacing {
    return Spacing(
        screenHorizontalPadding = 20.dp,
        ctaBottomPadding = 12.dp,
        inputDividerHeight = 16.dp
    )
}