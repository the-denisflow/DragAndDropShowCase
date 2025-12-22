package com.example.myapplication.presentation.components.dragndrop

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.example.myapplication.shared.utils.AppLogger

fun Modifier.dragShadowCapture(
    itemGraphicsLayer: GraphicsLayer,
    dragAndDropState: DragAndDropState,
    onBoundsChanged: (Rect) -> Unit,
    onSizeChanged: (IntSize) -> Unit,
    logger: AppLogger? = null,
    size: IntSize
): Modifier = this
    .onGloballyPositioned { coordinates ->
        onBoundsChanged(coordinates.boundsInParent())
    }
    .graphicsLayer()
    .drawWithContent {
        drawContent()
        if (size.width > 0 && size.height > 0) {
            itemGraphicsLayer.record(size) {
                this@drawWithContent.drawContent()
            }
            dragAndDropState.localView.updateDragShadow(
                dragAndDropState.dragShadowBuilder
            )
        }
    }
    .onSizeChanged { newSize ->
        onSizeChanged(newSize)
        logger?.info("DragShadowCapture", "onSizeChanged: $newSize")
    }