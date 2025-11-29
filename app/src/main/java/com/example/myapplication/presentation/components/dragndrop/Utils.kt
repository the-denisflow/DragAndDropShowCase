package com.example.myapplication.presentation.components.dragndrop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent

@Composable
fun Modifier.dragShadow(
    dragAndDropState: DragAndDropState
) {
    this.modifyNotNull(dragAndDropState) { state ->
        drawWithContent {
            state.graphicsLayer.record {
                this@drawWithContent.drawContent()
            }
            state.localView.updateDragShadow(state.dragShadowBuilder)
        }
    }
}

@Composable
fun<T> Modifier.modifyNotNull(nullable: T?, block:  @Composable Modifier.(T) -> Modifier): Modifier =
    nullable?.let {
        this.block(it)
    }?: this



