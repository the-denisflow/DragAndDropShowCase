package com.example.myapplication.presentation.components.dragndrop

import android.content.ClipData
import android.view.DragEvent
import android.view.View
import android.view.View.DragShadowBuilder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer

@Composable
fun rememberDragAndDropState(
    graphicsLayer: GraphicsLayer = rememberGraphicsLayer()
): DragAndDropState {
    return remember {
        DragAndDropState(graphicsLayer = graphicsLayer)
    }
}


class DragAndDropState internal constructor(
    internal val graphicsLayer: GraphicsLayer
): View.OnDragListener {
    lateinit var localView: View

    val dragShadowBuilder: DragShadowBuilder by mutableStateOf(DragShadowBuilder())

    fun startDrag(
        data: String
    ) {
        val clipData = ClipData.newPlainText("element", data)
        localView.startDragAndDrop(clipData, dragShadowBuilder, null, 0)
    }

    override fun onDrag(view: View?, event: DragEvent?): Boolean {
        return when (event?.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                println("Drag started")
                true
            }
            DragEvent.ACTION_DROP -> {
                val data = event.clipData?.getItemAt(0)?.text
                println("Dropped: $data")
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                println("Drag ended")
                true
            }
            else -> false
        }
    }
}