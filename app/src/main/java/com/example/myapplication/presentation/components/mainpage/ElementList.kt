package com.example.myapplication.presentation.components.mainpage

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.presentation.components.dragndrop.DragAndDropState
import com.example.myapplication.presentation.components.dragndrop.rememberDragAndDropState
import com.example.myapplication.presentation.utils.Dimens

@Composable
fun ElementList(
    elements: List<String>,
    modifier: Modifier = Modifier,
    dragAndDropState: DragAndDropState
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(elements) { index, element ->
            DraggableItem(element = element, dragAndDropState = dragAndDropState, index = index)
        }
    }
}