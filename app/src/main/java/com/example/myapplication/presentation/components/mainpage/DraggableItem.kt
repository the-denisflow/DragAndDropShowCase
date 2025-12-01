package com.example.myapplication.presentation.components.mainpage

import android.content.ClipData
import android.view.WindowInsetsAnimation
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import com.example.myapplication.presentation.components.dragndrop.DragAndDropState
import com.example.myapplication.presentation.utils.Dimens
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize

@Composable
fun DraggableItem(
    element: String,
    modifier: Modifier = Modifier,
    dragAndDropState: DragAndDropState,
    index: Int
) {
    val itemGraphicsLayer = rememberGraphicsLayer()

    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        dragAndDropState.localView.setOnDragListener(dragAndDropState)
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                if(element != dragAndDropState.currentDragKey)
                 Tile(element, itemGraphicsLayer, dragAndDropState)
                else
                 EmptyTile()
            }
        }
    }

@Composable
fun Tile(
    element: String,
    itemGraphicsLayer : GraphicsLayer,
    dragAndDropState: DragAndDropState,
) {
    var itemBounds by remember { mutableStateOf(Rect.Zero) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    var pendingDragStart by remember { mutableStateOf<Pair<androidx.compose.ui.geometry.Offset, Boolean>?>(null) }

    LaunchedEffect(pendingDragStart) {
        pendingDragStart?.let { (offset, _) ->

            val clipData = ClipData.newPlainText("element", element)
            dragAndDropState.startDrag(
                key = element,
                index = 0,
                data = DragAndDropTransferData(
                    clipData = clipData,
                    localState = element,
                    flags = 0
                ),
                dragItemLocalTouchOffset = offset,
                localBounds = itemBounds,
                itemGraphicsLayer = itemGraphicsLayer
            )
            pendingDragStart = null
        }
    }

    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                itemBounds = coordinates.boundsInParent()
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
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        isDragging = true
                        pendingDragStart = offset to true
                    },
                    onDrag = { change, _ -> change.consume() },
                    onDragEnd = { isDragging = false },
                    onDragCancel = { isDragging = false }
                )
            }
            .height(Dimens.listItemHeight)
            .padding(Dimens.listItemPadding)
            .background(Color.Green)
            .border(
                width = Dimens.listItemBorderWidth,
                color = if(isDragging) Color.Black else Color.White
            )

    ) {
        Text(
            text = element,
            color = if (isDragging) Color.Red else Color.Black,
            fontSize = Dimens.listItemTextSize,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.listItemContentHeight)
                .padding(Dimens.listItemTextPadding)
        )
    }
}

@Composable
fun EmptyTile(
) {
    Box(
        modifier = Modifier
            .height(Dimens.listItemHeight)
            .padding(Dimens.listItemPadding)
            .border(
                width = Dimens.listItemBorderWidth,
                color = Color.Black
            )
    ) {
        Text(
            text = "empty",
            color = Color.Black,
            fontSize = Dimens.listItemTextSize,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.listItemContentHeight)
                .padding(Dimens.listItemTextPadding)
        )
    }
}


