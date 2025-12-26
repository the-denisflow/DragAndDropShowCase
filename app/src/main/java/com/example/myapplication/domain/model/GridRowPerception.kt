package com.example.myapplication.domain.model

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.example.myapplication.shared.utils.AppLogger

/**
 * Provides spatial perception of a grid layout by calculating row boundaries.
 *
 * ## How It Works:
 * ```
 * Row 0: [top=100, bottom=200] If Y=150, returns rowIndex=0
 * Row 1: [top=210, bottom=310] If Y=250, returns rowIndex=1
 * Row 2: [top=320, bottom=420] cIf Y=50,  returns null (outside grid)
 * ``
 * ## Parameters:
 * @property itemCount Total number of items in the grid
 * @property columnCount Number of columns in the grid (e.g., 3 for a 3-column grid)
 * @property itemHeight Height of each individual item/tile
 * @property itemSpacing Vertical spacing between rows
 * @property contentPadding Padding at the top of the grid
 * @property listParentBounds The bounds of the parent container in window coordinates
 * @property density Screen density for Dp to pixel conversion
 * @property logger Logger for debugging row boundary calculations
 *
 */

data class GridRowPerception(
    private val itemCount: Int,
    private val columnCount: Int,
    private val itemHeight: Dp,
    private val itemSpacing: Dp,
    private val contentPadding: Dp,
    private val listParentBounds: Rect,
    private val density: Density,
    private val rowCount: Int,
    private val logger: AppLogger
) {
    private val STATE_TAG = "GridRowPerception"

    private val rowBounds: List<RowBounds> by lazy {
        calculateRowBounds()
    }

    /**
     * Calculates the vertical bounds for each row in the grid.
     *
     * @return List of [RowBounds] representing the vertical boundaries of each row.
     */
    private fun calculateRowBounds(): List<RowBounds> {
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

            RowBounds(
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