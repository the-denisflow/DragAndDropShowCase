package com.example.myapplication.presentation.components.mainpage

import android.content.ClipData
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.model.TileBounds
import com.example.myapplication.domain.model.TileIndex
import com.example.myapplication.domain.model.TileStateData
import com.example.myapplication.presentation.components.dragndrop.DragAndDropState
import com.example.myapplication.presentation.components.dragndrop.dragShadowCapture
import com.example.myapplication.presentation.utils.SquaredItemDimens
import com.example.myapplication.shared.utils.AppLogger

@Composable
fun SquaredDraggableItem(
    modifier: Modifier,
    element: TileStateData,
    dragAndDropState: DragAndDropState,
    index: Int,
    logger: AppLogger,
    listBounds: Map<TileIndex, TileBounds>
) {
    val itemGraphicsLayer = rememberGraphicsLayer()

    LaunchedEffect(Unit) {
        dragAndDropState.localView.setOnDragListener(dragAndDropState)
    }

    val isBeingDragged = element.id == dragAndDropState.currentDragKey?.value

    if (!isBeingDragged) {
        DraggableSquaredTile(
            listBounds = listBounds,
            modifier = modifier,
            element = element,
            dragAndDropState = dragAndDropState,
            index = index,
            itemGraphicsLayer = itemGraphicsLayer,
            logger = logger
        )
    }
}

@Composable
fun DraggableSquaredTile(
    listBounds: Map<TileIndex, TileBounds>,
    modifier: Modifier,
    element: TileStateData,
    dragAndDropState: DragAndDropState,
    index: Int,
    itemGraphicsLayer: GraphicsLayer,
    logger: AppLogger
) {
    var itemBounds by remember { mutableStateOf(Rect.Zero) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    var pendingDragStart by remember { mutableStateOf<Pair<Offset, Boolean>?>(null) }

    LaunchedEffect(pendingDragStart) {
        pendingDragStart?.let { (offset,_) ->
            startDragOperation(
                element = element,
                index = index,
                offset = offset,
                itemBounds = itemBounds,
                itemGraphicsLayer = itemGraphicsLayer,
                dragAndDropState = dragAndDropState,
                listBounds = listBounds
            )
            pendingDragStart = null
        }
    }

    Box(
        modifier = modifier
            .onSizeChanged { newSize ->
                size = newSize
                logger.info(TAG, "onSizeChanged: $newSize")
            }
            .dragShadowCapture(
                itemGraphicsLayer = itemGraphicsLayer,
                dragAndDropState = dragAndDropState,
                onBoundsChanged = { itemBounds = it },
                onSizeChanged = { size = it },
                logger = logger,
                size = size
            )
            .pointerInput(element.id) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        logger.info(TAG, "Drag started at offset: $offset")
                        isDragging = true
                        pendingDragStart = (offset to true)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                    },
                    onDragEnd = {
                        logger.info(TAG, "Drag ended")
                        isDragging = false

                    },
                    onDragCancel = {
                        logger.info(TAG, "Drag cancelled")
                        isDragging = false
                    }
                )
            }
            .height(SquaredItemDimens.itemSize)
            .width(SquaredItemDimens.itemSize),
        contentAlignment = Alignment.Center
    ) {
       AppTile(element)
    }
}

@Composable
fun AppTile(element: TileStateData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2D3561)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(element.resourceId),
                    contentDescription = "appName",
                    modifier = Modifier.size(45.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Text(
            text = element.label,
            fontSize = 13.sp,
            color = Color.White
        )
    }
}

private fun startDragOperation(
    listBounds: Map<TileIndex, TileBounds>,
    element: TileStateData,
    index: Int,
    offset: Offset,
    itemBounds: Rect,
    itemGraphicsLayer: GraphicsLayer,
    dragAndDropState: DragAndDropState
) {
    val clipData = ClipData.newPlainText(element.label, element.label)
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
        listBounds = listBounds
    )
}

private const val TAG = "SquaredDraggableItem"