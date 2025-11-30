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
    var itemBounds by remember { mutableStateOf(Rect.Zero) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        itemBounds = coordinates.boundsInParent()
                    }
                    .onSizeChanged { newSize ->
                        size = newSize
                    }
                    .drawWithContent {
                        drawContent()
                        if (size.width > 0 && size.height > 0) {
                            itemGraphicsLayer.record(size) {
                                this@drawWithContent.drawContent()
                            }
                            dragAndDropState.localView.updateDragShadow(dragAndDropState.dragShadowBuilder)

                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { offset ->
                                isDragging = true
                                println("After setting isDragging: $isDragging")
                                val clipData = ClipData.newPlainText("element", element)
                                dragAndDropState.startDrag(
                                    key = element,
                                    index = index,
                                    data = DragAndDropTransferData(
                                        clipData = clipData,
                                        localState = element,
                                        flags = 0
                                    ),
                                    dragItemLocalTouchOffset = offset,
                                    localBounds = itemBounds,
                                    itemGraphicsLayer = itemGraphicsLayer
                                )
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                isDragging = true },

                        )
                    }
            ) {
                Tile(isDragging, element)
            }
        }
    }

@Composable
fun Tile(
    isDragging: Boolean,
    element: String
) {
    Box(
        modifier = Modifier
            .height(Dimens.listItemHeight)
            .padding(Dimens.listItemPadding)
            .background(Color.Green)
            .border(
                width = Dimens.listItemBorderWidth,
                color = if (isDragging) Color.Black else Color.White
            )
    ) {
        Text(
            text = element,
            color = Color.Black,
            fontSize = Dimens.listItemTextSize,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.listItemContentHeight)
                .padding(Dimens.listItemTextPadding)
        )
    }
}

