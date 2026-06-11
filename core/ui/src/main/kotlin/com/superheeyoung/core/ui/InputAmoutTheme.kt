package com.superheeyoung.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.superheeyoung.core.ui.foundation.AppColors
import com.superheeyoung.core.ui.foundation.DefaultTypogrphy
import com.superheeyoung.core.ui.foundation.createDefaultSpacing
import com.superheeyoung.core.ui.foundation.lightAppColors

@Composable
fun InputAmountTheme(
    appColors: AppColors = lightAppColors(),
    provides: List<ProvidedValue<*>> = emptyList(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAppColor provides appColors,
        LocalTypography provides DefaultTypogrphy(),
        LocalSpacing provides createDefaultSpacing(),
        * provides.toTypedArray()
    ) {
        MaterialTheme(
            colorScheme = androidx.compose.material3.lightColorScheme(
                background = appColors.white
            ),
            content = content
        )
    }
}

val LocalAppColor = staticCompositionLocalOf { lightAppColors() }
val appColors: AppColors @Composable @ReadOnlyComposable get() = LocalAppColor.current

val LocalTypography = staticCompositionLocalOf { DefaultTypogrphy() }
val typography @Composable @ReadOnlyComposable get() = LocalTypography.current

val LocalSpacing = staticCompositionLocalOf { createDefaultSpacing() }
val spacing @Composable @ReadOnlyComposable get() = LocalSpacing.current
