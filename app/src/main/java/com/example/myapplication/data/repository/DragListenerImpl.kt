package com.example.myapplication.data.repository

import androidx.compose.ui.geometry.Rect
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
    override fun onDragStart() {

    }

    override fun onDrag(x: Float, y: Float, listBound: Rect?, indexTileBeingDragged: Int) {
        logger.info(
            STATE_TAG,
            "Dragging at x: $x, y: $y"
        )
        dragHelper.dragShadow(x, y)
    }

    override fun onDragEnded() {

    }
}