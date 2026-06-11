package com.superheeyoung.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

fun Modifier.onClick(onClick: (() -> Unit)?): Modifier = onClick
    ?.let { clickable(interactionSource = null, indication = null, onClick = it) }
    ?: this
