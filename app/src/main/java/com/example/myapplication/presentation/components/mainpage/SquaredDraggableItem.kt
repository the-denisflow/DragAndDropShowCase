package com.example.myapplication.presentation.components.mainpage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.model.TileStateData
import com.example.myapplication.presentation.components.dragndrop.DragAndDropState

@Composable
fun SquaredDraggableItem (
    modifier: Modifier,
    element : TileStateData,
    itemGraphicsLayer: GraphicsLayer,
    dragAndDropState: DragAndDropState,
    index: Int
) {
    Box(
        modifier = Modifier
            .size(100.dp, 100.dp)
            .background(Color.Blue)
            .border(
                width = 2.dp,
                color = Color.Black
            ).pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->

                    },
                    onDrag = { change, _ -> change.consume() },
                    onDragEnd = { },
                    onDragCancel = { }
                )
            }
        ,
            contentAlignment = Alignment.Center

    ) {
        Text(
            text = element.label,
            color = Color.White,
            fontSize = 20.sp
        )
    }
}