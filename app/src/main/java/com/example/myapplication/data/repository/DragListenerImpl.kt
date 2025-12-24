package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.DragIndexState
import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBoundsMap
import com.example.myapplication.domain.repository.DragHelper
import com.example.myapplication.domain.repository.DragListener
import com.example.myapplication.presentation.utils.ListValues
import com.example.myapplication.shared.utils.AppLogger
import javax.inject.Inject

class DragListenerImpl @Inject constructor(
    val logger: AppLogger,
    val dragHelper: DragHelper
): DragListener {
    companion object {
        private val STATE_TAG = "DragListenerImpl"
    }

    private var itemCount =  0
    private var rowCount = 0

    private var currentDraggedTileIndex: Int? = null

    private var gridRowPerception : GridRowPerception? = null
    private var draggedTileTouchedHalfTop: Boolean? = null


    override fun onDragStart() {
        currentDraggedTileIndex = null
    }

    override fun onDrag(
        x: Float,
        y: Float,
        listBound: TileBoundsMap?,
        indexTileBeingDragged: DragIndexState,
        offsetFromCenter: Pair<Float, Float>
    ) {
       if (indexTileBeingDragged is DragIndexState.Dragging) {
           logger.info(STATE_TAG, "Dragging at x: $x, y: $y")

        if (currentDraggedTileIndex == null) {
            currentDraggedTileIndex = indexTileBeingDragged.index
        }
           val centerX = x - offsetFromCenter.first
           val centerY = y - offsetFromCenter.second
              when (gridRowPerception?.getRowIndexForY(
                  centerY
               )) {
                   0 -> {
                       logger.info(STATE_TAG, "currentRow : 0")

                   }

                   1 -> {
                       logger.info(STATE_TAG, "currentRow : 1")
                   }
               }


           /*

        val draggedOverTileIndex = listBound.entries.firstOrNull { (index, bounds) ->
            index != currentDraggedTileIndex && bounds.contains(y)
        }?.key

        if (draggedOverTileIndex != null) {
            logger.info(STATE_TAG, "Reordering: $currentDraggedTileIndex -> $draggedOverTileIndex")

            dragHelper.dragShadow("reorder", currentDraggedTileIndex!!, draggedOverTileIndex)

            currentDraggedTileIndex = draggedOverTileIndex
         }
            */
       } else {
           logger.warning(STATE_TAG, "drag is not being initialized")
           return
       }
    }

    override fun onDragEnded() {
        currentDraggedTileIndex = null
    }

    override fun initializeListPerception(gridRowPerception: GridRowPerception?) {
       this.gridRowPerception  = gridRowPerception
    }

    override fun draggedTileTouchedHalfTop(yesOrNot: Boolean?) {
        draggedTileTouchedHalfTop = yesOrNot
    }

    private fun processTableProperties(listBound : TileBoundsMap?) {
        require(listBound != null)
        itemCount = listBound.size
        rowCount = (itemCount / ListValues.COLUMN_COUNT).toInt()

    }
}

