package com.example.myapplication.domain.repository

import androidx.compose.runtime.snapshots.SnapshotStateMap

interface DragListener {
    fun onDragStart()
    fun onDrag(x: Float, y: Float, listBound: SnapshotStateMap<Int, Pair<Float, Float>>?, indexTileBeingDragged: Int)
    fun onDragEnded()
}