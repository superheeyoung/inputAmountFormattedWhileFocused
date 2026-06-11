package com.superheeyoung.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

sealed class ImageAsset {
    data class Image(@param:DrawableRes val resId: Int) : ImageAsset()
}

@Composable
fun Image(
    modifier: Modifier = Modifier,
    asset: ImageAsset?,
    contentDescription: String? = null,
    iconTint: Color = Color.Unspecified,
    alpha: Float = 1f
) {
    when (asset) {
        is ImageAsset.Image -> Image(
            modifier = modifier,
            painter = painterResource(id = asset.resId),
            contentDescription = contentDescription,
            alpha = alpha
        )

        null -> Box(modifier)
    }
}
