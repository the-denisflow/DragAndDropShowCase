package com.example.myapplication.presentation.components.mainpage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.model.TileStateData

@Composable
fun SquaredDraggableItem (
    modifier: Modifier,
    element : TileStateData
) {
    Box(
        modifier = Modifier
            .size(100.dp, 100.dp)
            .background(Color.Blue)
            .border(
                width = 2.dp,
                color = Color.Black
            ),
            contentAlignment = Alignment.Center

    ) {
        Text(
            text = element.label,
            color = Color.White,
            fontSize = 20.sp
        )
    }
}