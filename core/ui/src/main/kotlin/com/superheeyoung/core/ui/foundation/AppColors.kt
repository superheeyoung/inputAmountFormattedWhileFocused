package com.superheeyoung.core.ui.foundation

import androidx.compose.ui.graphics.Color

class AppColors(
    val white: Color,
    val gray1: Color,
    val gray2: Color,
    val gray3: Color,
    val gray4: Color,
    val gray5: Color,
    val gray6: Color,
    val gray7: Color,
    val primary: Color,
    val blue: Color,
    val red: Color,
)

internal fun lightAppColors(): AppColors = AppColors(
    white = Color(0xFFFFFFFF),
    gray1 = Color(0xFFF8F9FA),
    gray2 = Color(0xFFE9ECEF),
    gray3 = Color(0xFFCED4DA),
    gray4 = Color(0xFF868E96),
    gray5 = Color(0xFF495057),
    gray6 = Color(0xFF343A40),
    gray7 = Color(0xFF212529),
    primary = Color(0xFF7950F2),
    blue = Color(0xFF4C6EF5),
    red = Color(0xFFFF6B6B),
)