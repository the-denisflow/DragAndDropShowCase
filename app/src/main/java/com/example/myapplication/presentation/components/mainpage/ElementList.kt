package com.example.myapplication.presentation.components.mainpage

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import com.example.myapplication.domain.model.TileStateData
import com.example.myapplication.presentation.components.dragndrop.DragAndDropState
import com.example.myapplication.presentation.utils.Dimens
import com.example.myapplication.shared.utils.AppLogger


@Composable
fun ElementList(
    logger: AppLogger,
    elements: List<TileStateData>,
    dragAndDropState: DragAndDropState,
) {
    var columnBounds by remember { mutableStateOf<Rect?>(null) }

    val listStructureBounds = remember { mutableStateMapOf<Int, Pair<Float, Float>>() }


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
                }.
                border(
                    width = Dimens.listItemBorderWidth,
                    color = Color.Black)
        ) {
            itemsIndexed(
                items = elements,
                key = { _, element -> element.id }
            ) { index, element ->
                DraggableItem(
                    element = element,
                    dragAndDropState = dragAndDropState,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        val bounds = coordinates.boundsInWindow()
                        listStructureBounds[index] = bounds.top to bounds.bottom
                        logger.info("DraggableItem", "Tile[$index] measured: top=${bounds.top}, bottom=${bounds.bottom}")
                    },
                    index = index,
                    listBounds = listStructureBounds
                )
            }
        }
    }
}




