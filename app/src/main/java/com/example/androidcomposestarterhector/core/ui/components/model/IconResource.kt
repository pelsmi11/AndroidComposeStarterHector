package com.example.androidcomposestarterhector.core.ui.components.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

sealed class IconResource {
    data class ResourceIcon(val resId: Int) : IconResource()
    // Podrías agregar un ImageVectorIcon, etc.

    @Composable
    fun asPainterResource(): Painter {
        return when (this) {
            is ResourceIcon -> painterResource(id = this.resId)
        }
    }
}