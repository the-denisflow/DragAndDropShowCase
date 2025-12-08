package com.example.myapplication.presentation.components.dragndrop

import android.view.DragEvent
import android.view.View
import android.view.View.DragShadowBuilder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Density
import com.example.myapplication.domain.repository.DragListener
import com.example.myapplication.shared.utils.AppLogger

@JvmInline
value class DragKey(val value: String)

@Composable
fun rememberDragAndDropState(
     logger: AppLogger,
     density: Density = LocalDensity.current,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
     dragListener: DragListener
): DragAndDropState {
    return remember {
        DragAndDropState(
            logger = logger,
            density = density,
            layoutDirection = layoutDirection,
            dragListener = dragListener
        )
    }
}

class DragAndDropState internal constructor(
    internal val logger: AppLogger,
    val density: Density,
    val layoutDirection: LayoutDirection,
    val dragListener: DragListener
): View.OnDragListener {
    private val STATE_TAG = "DragAndDropState"
    lateinit var localView: View

    var dragShadowBuilder: DragShadowBuilder by mutableStateOf(DragShadowBuilder())
    private set

    var currentDragKey: DragKey? by mutableStateOf(null)
    private set

    var indexTileBeingDragged: Int? by mutableStateOf(null)
    private set

    var currentListBounds: SnapshotStateMap<Int, Pair<Float, Float>>? by mutableStateOf(null)

    fun startDrag(
        key: String,
        index: Int,
        data: DragAndDropTransferData,
        dragItemLocalTouchOffset: Offset = Offset.Zero,
        localBounds: Rect,
        itemGraphicsLayer: GraphicsLayer,
        listBounds: SnapshotStateMap<Int, Pair<Float, Float>>
    ) {
        currentDragKey = DragKey(key)
        indexTileBeingDragged = index
        currentListBounds = listBounds

        require(::localView.isInitialized){
            logger.warning(STATE_TAG,
                "Local view is not initialized")
        }

        require(listBounds != null) {
            logger.warning(STATE_TAG,
                "List bound is null")
        }

        logger.info(
            STATE_TAG,
            "Start drag local bounds: $localBounds"
        )

        dragShadowBuilder = ComposeDragShadowBuilder(
            graphicsLayer = itemGraphicsLayer,
            density = density,
            layoutDirection = layoutDirection,
            touchPosition = dragItemLocalTouchOffset,
            size = localBounds.size
        )

        localView.startDragAndDrop(
            data.clipData,
            dragShadowBuilder,
            data.localState,
            data.flags
        )

    }

    override fun onDrag(view: View?, event: DragEvent?): Boolean {
        return when (event?.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                logger.info(
                    STATE_TAG,
                    "Action drag"
                )
                dragListener.onDragStart()
                true
            }
            DragEvent.ACTION_DROP -> {
                logger.info(
                    STATE_TAG,
                    "Action drop"
                )
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                val x = event.x
                val y = event.y
                dragListener.onDrag( x, y,currentListBounds, indexTileBeingDragged!!)
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                logger.info(
                    STATE_TAG,
                    "Action drag ended"
                )
                dragListener.onDragEnded()
                currentDragKey = null

                true
            }
            else -> {
                logger.info(
                    STATE_TAG,
                    "Action else"
                )
                false
            }
        }
    }
}