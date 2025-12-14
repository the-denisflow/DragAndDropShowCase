package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.TileBoundsMap

interface DragListener {
    fun onDragStart()
    fun onDrag(x: Float, y: Float, listBound: TileBoundsMap?, indexTileBeingDragged: Int)
    fun onDragEnded()
}