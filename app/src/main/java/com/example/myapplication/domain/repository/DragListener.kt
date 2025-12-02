package com.example.myapplication.domain.repository

interface DragListener {
    fun onDragStart()
    fun onDrag(x: Float, y: Float)
    fun onDragEnded()
}