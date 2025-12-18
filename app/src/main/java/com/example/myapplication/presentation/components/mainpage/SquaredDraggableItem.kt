package com.example.myapplication.presentation.components.mainpage

import android.content.ClipData
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.model.TileStateData
import com.example.myapplication.presentation.components.dragndrop.DragAndDropState
import com.example.myapplication.shared.utils.AppLogger

@Composable
fun SquaredDraggableItem (
    modifier: Modifier,
    element : TileStateData,
    dragAndDropState: DragAndDropState,
    index: Int,
    logger: AppLogger
) {
    val TAG = "SquaredDraggableItem"
    val itemGraphicsLayer = rememberGraphicsLayer()
    var itemBounds by remember { mutableStateOf(Rect.Zero) }
    var size by remember { mutableStateOf(IntSize.Zero) }


    LaunchedEffect(Unit) {
        dragAndDropState.localView.setOnDragListener(dragAndDropState)
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
            .size(100.dp, 100.dp)
            .background(Color.Blue)
            .border(
                width = 2.dp,
                color = Color.Black
            )
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        logger.info(TAG, "drag start $offset")

                        val clipData = ClipData.newPlainText("element", element.label)
                        dragAndDropState.startDrag(
                            key = element.id,
                            index = index,
                            data = DragAndDropTransferData(
                                clipData = clipData,
                                localState = element,
                                flags = 0
                            ),
                            dragItemLocalTouchOffset = offset,
                            localBounds = itemBounds,
                            itemGraphicsLayer = itemGraphicsLayer,
                        )

                    },
                    onDrag = { change, _ -> change.consume() },
                    onDragEnd = { },
                    onDragCancel = { }
                )
            }
        ,
            contentAlignment = Alignment.Center

    ) {
        Text(
            text = element.label,
            color = Color.White,
            fontSize = 20.sp
        )
    }
}