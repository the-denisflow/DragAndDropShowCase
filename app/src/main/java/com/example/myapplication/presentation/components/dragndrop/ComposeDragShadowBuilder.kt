package com.example.myapplication.presentation.components.dragndrop

import android.graphics.Point
import android.view.View
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.toSize

/**
 * Custom DragShadowBuilder that renders Compose content as a drag shadow.
 *
 * This class bridges Android's View-bases drag and drop system with Compose content
 * that is rendered in a GraphicsLayer
 *
 * @param graphicsLayer The graphics layer containing the rendered composable
 * to display as the drag shadow.
 * @param density The screen density for converting between pixels and dp
 * @param layoutDirection The layout direction of the content being rendered.
 * @param touchPosition The position where the user touched the item.
 * @param size The size of the content being rendered.
 */
class ComposeDragShadowBuilder (
    private val graphicsLayer: GraphicsLayer,
    private val density: Density,
    private val layoutDirection: LayoutDirection,
    private val touchPosition: Offset,
    private val size: Size,
) : View.DragShadowBuilder() {
    // Defines the drag shadow metrics: the size where the user touched it.
    // TODO: extend documentation
    override fun onProvideShadowMetrics(
        outShadowSize: Point,
        outShadowTouchPoint: Point
    ) = with(density) {
        outShadowSize.set(
            size.width.toDp().roundToPx(),
            size.height.toDp().roundToPx()
        )

        outShadowTouchPoint.set(
            touchPosition.x.toDp().roundToPx(),
            touchPosition.y.toDp().roundToPx()
        )
    }

   // Draws the drag shadow in the canvas
    override fun onDrawShadow(canvas: android.graphics.Canvas) {
        CanvasDrawScope().draw(
            density = density,
            size = graphicsLayer.size.toSize(),
            layoutDirection = layoutDirection,
            canvas = Canvas(canvas)
        ) {
            drawLayer(graphicsLayer)
        }
    }
}