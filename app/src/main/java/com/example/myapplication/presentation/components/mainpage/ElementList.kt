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
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBounds
import com.example.myapplication.domain.model.TileStateData
import com.example.myapplication.presentation.components.dragndrop.DragAndDropState
import com.example.myapplication.presentation.components.dragndrop.trackBoundsInMap
import com.example.myapplication.presentation.utils.Dimens
import com.example.myapplication.presentation.utils.ListValues
import com.example.myapplication.presentation.utils.SquaredItemDimens
import com.example.myapplication.shared.utils.AppLogger

@Composable
fun ElementList(
    logger: AppLogger,
    elements: List<TileStateData>,
    dragAndDropState: DragAndDropState,
) {
    var listParentBounds by remember { mutableStateOf(Rect.Zero) }
    val listStructureBounds = remember { mutableStateMapOf<Int, TileBounds>() }
    val density = LocalDensity.current

    val gridPerception = remember(listParentBounds, elements.size) {
        if (listParentBounds != Rect.Zero) {
            GridRowPerception(
                itemCount = elements.size,
                columnCount = ListValues.COLUMN_COUNT,
                itemHeight = SquaredItemDimens.itemSize,
                itemSpacing = ListValues.itemSpacing,
                contentPadding = ListValues.contentPadding,
                listParentBounds = listParentBounds,
                logger = logger,
                density = density
            )
        } else null
    }

    LaunchedEffect(gridPerception) {
        dragAndDropState.updateGridPerception(gridPerception)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(ListValues.COLUMN_COUNT),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(
                    width = Dimens.listItemBorderWidth,
                    color = Color.Black
                )
                .onGloballyPositioned { coordinates ->
                    listParentBounds = coordinates.boundsInParent()
                },
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                count = elements.size,
                key = { index -> elements[index].id }
            ) { index ->
                SquaredDraggableItem(
                    modifier = Modifier.trackBoundsInMap(
                        index = index,
                        boundsMap = listStructureBounds,
                    ),
                    element = elements[index],
                    index = index,
                    dragAndDropState = dragAndDropState,
                    logger = logger,
                    listBounds = listStructureBounds
                )
            }
        }
    }
}