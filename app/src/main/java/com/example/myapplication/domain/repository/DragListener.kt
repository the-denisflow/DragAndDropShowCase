package com.example.myapplication.domain.repository

import androidx.compose.ui.geometry.Rect

interface DragListener {
    fun onDragStart()
    fun onDrag(x: Float, y: Float, listBound: Rect?, indexTileBeingDragged: Int)
    fun onDragEnded()
}