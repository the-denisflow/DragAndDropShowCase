package com.example.myapplication.data.repository

import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.myapplication.domain.repository.DragHelper
import com.example.myapplication.domain.repository.DragListener
import com.example.myapplication.shared.utils.AppLogger
import javax.inject.Inject

class DragListenerImpl @Inject constructor(
    val logger: AppLogger,
    val dragHelper: DragHelper
): DragListener {
    companion object {
        private val STATE_TAG = "DragListenerImpl"
    }

    private var currentDraggedTileIndex: Int? = null

    override fun onDragStart() {
        currentDraggedTileIndex = null
    }

    override fun onDrag(
        x: Float,
        y: Float,
        listBound: SnapshotStateMap<Int, Pair<Float, Float>>?,
        indexTileBeingDragged: Int
    ) {
        logger.info(STATE_TAG, "Dragging at x: $x, y: $y")

        require(listBound != null)

        if (currentDraggedTileIndex == null) {
            currentDraggedTileIndex = indexTileBeingDragged
        }

        val draggedOverTileIndex = listBound.entries.firstOrNull { (index, bounds) ->
            val (top, bottom) = bounds
            index != currentDraggedTileIndex && y in top..bottom
        }?.key

        if (draggedOverTileIndex != null) {
            logger.info(STATE_TAG, "Reordering: $currentDraggedTileIndex -> $draggedOverTileIndex")

            dragHelper.dragShadow("reorder", currentDraggedTileIndex!!, draggedOverTileIndex)

            currentDraggedTileIndex = draggedOverTileIndex
        }
    }

    override fun onDragEnded() {
        currentDraggedTileIndex = null
    }
}