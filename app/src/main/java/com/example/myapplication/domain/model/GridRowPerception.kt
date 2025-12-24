package com.example.myapplication.domain.model

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.example.myapplication.shared.utils.AppLogger
import kotlin.math.ceil

data class GridRowPerception(
    private val itemCount: Int,
    private val columnCount: Int,
    private val itemHeight: Dp,
    private val itemSpacing: Dp,
    private val contentPadding: Dp,
    private val listParentBounds: Rect,
    private val density: Density,
    private val logger: AppLogger
) {
    private val STATE_TAG = "GridRowPerception"
    private val rowCount: Int = ceil(itemCount.toDouble() / columnCount).toInt()

    private val rowBounds: List<TileBounds> by lazy {
        calculateRowBounds()
    }

    private fun calculateRowBounds(): List<TileBounds> {
        val rowHeightPx = with(density) {
            (itemHeight + itemSpacing).toPx()
        }
        val startOffsetPx = with(density) {
            listParentBounds.top + contentPadding.toPx()
        }
        val itemHeightPx = with(density) {
            itemHeight.toPx()
        }

        return (0 until rowCount).map { rowIndex ->
            val top = startOffsetPx + (rowIndex * rowHeightPx)
            val bottom = top + itemHeightPx

            logger.info(STATE_TAG, "Row $rowIndex: top=$top, bottom=$bottom")

            TileBounds(
                top = top,
                bottom = bottom,
                left = listParentBounds.left,
                right = listParentBounds.right,
            )
        }
    }


    /**
     * Returns which row the given Y coordinate falls into
     * @param y The Y coordinate (can be touch point or center point)
     * @return row index (0-based) or null if outside grid
     */
    fun getRowIndexForY(y: Float): Int? {
        return rowBounds.indexOfFirst { it.contains(y) }
            .takeIf { it >= 0 }
    }
}