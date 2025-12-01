package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.myapplication.presentation.components.dragndrop.rememberDragAndDropState
import com.example.myapplication.presentation.components.mainpage.DraggableArea
import com.example.myapplication.presentation.components.mainpage.ElementList
import com.example.myapplication.presentation.viewmodel.TilesViewModel
import com.example.myapplication.shared.utils.AppLogger
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val logger = AppLogger
    private val tilesViewModel: TilesViewModel by viewModels()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val dragAndDropState = rememberDragAndDropState(logger = logger)
                 val tilesList = tilesViewModel.tiles.collectAsState()
                        DraggableArea(dragAndDropState) {
                            ElementList(
                                dragAndDropState = dragAndDropState,
                                elements = tilesList.value,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
}

