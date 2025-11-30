package com.example.myapplication.presentation.components.mainpage

import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.presentation.components.dragndrop.DragAndDropState


@Composable
fun DraggableArea(
    dragAndDropState: DragAndDropState,
    content: @Composable () -> Unit
) {
    AndroidView(
        factory = {
            content ->
            View(content).apply {
                dragAndDropState.localView = this
            }
        },
        modifier = Modifier.fillMaxSize())
        content()
    }
