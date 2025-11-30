package com.example.myapplication.presentation.components.dragndrop

import android.content.ClipData
import android.graphics.Bitmap
import android.view.DragEvent
import android.view.View
import android.view.View.DragShadowBuilder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Density
import com.example.myapplication.shared.utils.AppLogger

@Composable
fun rememberDragAndDropState(
     logger: AppLogger,
     density: Density = LocalDensity.current,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current
): DragAndDropState {
    return remember {
        DragAndDropState(
            logger = logger,
            density = density,
            layoutDirection = layoutDirection)
    }
}

class DragAndDropState internal constructor(
    internal val logger: AppLogger,
    val density: Density,
    val layoutDirection: LayoutDirection
): View.OnDragListener {
    private val STATE_TAG = "DragAndDropState"
    lateinit var localView: View

    var dragShadowBuilder: DragShadowBuilder by mutableStateOf(DragShadowBuilder())
    private set

    fun startDrag(
        key: String,
        index: Int,
        data: DragAndDropTransferData,
        dragItemLocalTouchOffset: Offset = Offset.Zero,
        localBounds: Rect,
        itemGraphicsLayer: GraphicsLayer,
    ) {
        require(::localView.isInitialized){
            logger.warning(STATE_TAG,
                "Local view is not initialized")
        }
        require(itemGraphicsLayer.size.height> 0 && itemGraphicsLayer.size.width > 0){
            logger.warning(STATE_TAG,
                "Item graphics layer size is not initialized")
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
                true
            }
            DragEvent.ACTION_DROP -> {
                logger.info(
                    STATE_TAG,
                    "Action drop"
                )
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                logger.info(
                    STATE_TAG,
                    "Action drag ended"
                )
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