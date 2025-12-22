package com.example.myapplication.presentation.components.mainpage

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.model.TileBounds
import com.example.myapplication.domain.model.TileStateData
import com.example.myapplication.presentation.components.dragndrop.DragAndDropState
import com.example.myapplication.presentation.components.dragndrop.trackBoundsInMap
import com.example.myapplication.presentation.utils.Dimens
import com.example.myapplication.shared.utils.AppLogger

@Composable
fun ElementList(
        logger: AppLogger,
        elements: List<TileStateData>,
        dragAndDropState: DragAndDropState,
    ) {
       val listStructureBounds = remember { mutableStateMapOf<Int, TileBounds>() }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .border(
                        width = Dimens.listItemBorderWidth,
                        color = Color.Black
                    ),
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    count = elements.size,
                    key = { index -> elements[index].id }
                ) { index ->
                    val element = elements[index]
                    SquaredDraggableItem(
                        modifier = Modifier.trackBoundsInMap(
                            index = index,
                            boundsMap = listStructureBounds
                        ),
                        element = element,
                        index = index,
                        dragAndDropState = dragAndDropState,
                        logger = logger,
                        listBounds = listStructureBounds)
                }
            }
        }
    }





