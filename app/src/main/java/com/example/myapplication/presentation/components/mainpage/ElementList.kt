package com.example.myapplication.presentation.components.mainpage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.model.TileStateData
import com.example.myapplication.presentation.components.dragndrop.DragAndDropState

@Composable
fun ElementList(
    elements: List<TileStateData>,
    modifier: Modifier = Modifier,
    dragAndDropState: DragAndDropState,
) {
    var columnBounds by remember { mutableStateOf<Rect?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .onGloballyPositioned { coordinates ->
                    columnBounds = coordinates.boundsInWindow()
                }
        ) {
            itemsIndexed(
                items = elements,
                key = { _, element -> element.id }
            ) { index, element ->
                DraggableItem(
                    element = element,
                    dragAndDropState = dragAndDropState,
                    index = index,
                    listBounds = columnBounds
                )
            }
        }
    }
}



