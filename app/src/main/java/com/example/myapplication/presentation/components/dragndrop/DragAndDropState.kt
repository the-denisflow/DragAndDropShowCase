package com.example.myapplication.presentation.components.dragndrop

import android.annotation.SuppressLint
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
import com.example.myapplication.domain.model.DragIndexState
import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBoundsMap
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

@SuppressLint("MutableCollectionMutableState")
class DragAndDropState internal constructor(
    internal val logger: AppLogger,
    val density: Density,
    val layoutDirection: LayoutDirection,
    val dragListener: DragListener
): View.OnDragListener {
    private val STATE_TAG = "DragAndDropState"

    lateinit var localView: View

    private var gridPerception: GridRowPerception? = null

    var dragShadowBuilder: DragShadowBuilder by mutableStateOf(DragShadowBuilder())
        private set

    var currentDragKey: DragKey? by mutableStateOf(null)
        private set

    var indexTileBeingDragged: DragIndexState by mutableStateOf(DragIndexState.NotDragging)
        private set

    var currentListBounds: TileBoundsMap? by mutableStateOf(null)
        private set

    var currentTileTileTouchedTopHalf: Boolean? by mutableStateOf(null)
        private set

    var offsetFromCenter: Pair<Float, Float>? by mutableStateOf(null)
        private set

    fun startDrag(
        key: String,
        index: Int,
        data: DragAndDropTransferData,
        dragItemLocalTouchOffset: Offset = Offset.Zero,
        localBounds: Rect,
        itemGraphicsLayer: GraphicsLayer,
        listBounds: TileBoundsMap
    ) {
        currentListBounds = listBounds
        currentDragKey = DragKey(key)
        indexTileBeingDragged = DragIndexState.Dragging(index)

        require(::localView.isInitialized){
            logger.warning(STATE_TAG,
                "Local view is not initialized")
        }

        gridPerception?.let {
            dragListener.onDragStart(listBounds, it)
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

    fun updateGridPerception(gridPerception: GridRowPerception?) {
        this.gridPerception = gridPerception
        logger.info(STATE_TAG, "Grid perception updated: $gridPerception")
    }

    override fun onDrag(view: View?, event: DragEvent?): Boolean {
        return when (event?.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                if(indexTileBeingDragged is DragIndexState.Dragging) {

                    val currentDraggedTileBounds = currentListBounds?.get(
                        (indexTileBeingDragged as DragIndexState.Dragging).index
                    )

                    val tileCenterX = currentDraggedTileBounds?.centerX
                    val tileCenterY = currentDraggedTileBounds?.centerY

                   val  offsetFromCenterY = event.y - tileCenterY!!
                   val  offsetFromCenterX = event.x - tileCenterX!!

                    offsetFromCenter = (offsetFromCenterX to offsetFromCenterY)
                }
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
                offsetFromCenter?.let {
                    dragListener.onDrag(
                        x = event.x,
                        y = event.y,
                        indexTileBeingDragged = indexTileBeingDragged,
                        offsetFromCenter = it
                    )
            }
                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                logger.info(
                    STATE_TAG,
                    "Action drag ended"
                )
                currentDragKey = null
                currentTileTileTouchedTopHalf = null
                dragListener.onDragEnded()

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