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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.presentation.components.dragndrop.rememberDragAndDropState
import com.example.myapplication.presentation.utils.Dimens

@Composable
fun ElementList(
    elements: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(elements) { element ->
            ElementItem(element = element)
        }
    }
}

@Composable
fun ElementItem(
    element: String,
    modifier: Modifier = Modifier
) {
    val dragAndDropState = rememberDragAndDropState()

    AndroidView(
        factory = { context ->
            View(context).apply {
                dragAndDropState.localView = this
                setOnLongClickListener {
                    dragAndDropState.startDrag(element)
                    true
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.listItemHeight)
            .padding(Dimens.listItemPadding)
            .background(Color.Green)
            .border(Dimens.listItemBorderWidth, Color.Black)
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress {
                    change , dragAmount ->
                    change.consume()
                    println("DragDetected: $dragAmount")
                }
            }
    ) {

        Text(
            text = element,
            color = Color.Black,
            fontSize = Dimens.listItemTextSize,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.listItemContentHeight)
                .padding(Dimens.listItemTextPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ElementListPreview() {
    ElementList(
        elements = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5"),
        modifier = Modifier.fillMaxSize()
    )
}