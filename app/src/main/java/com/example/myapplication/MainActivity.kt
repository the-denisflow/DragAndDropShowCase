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
import com.example.myapplication.domain.repository.DragListener
import com.example.myapplication.presentation.components.dragndrop.rememberDragAndDropState
import com.example.myapplication.presentation.components.mainpage.DraggableArea
import com.example.myapplication.presentation.components.mainpage.ElementList
import com.example.myapplication.presentation.viewmodel.TilesViewModel
import com.example.myapplication.shared.utils.AppLogger
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var dragListener: DragListener
    @Inject
    lateinit var logger: AppLogger

    private val tilesViewModel: TilesViewModel by viewModels()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val dragAndDropState = rememberDragAndDropState(logger = logger, dragListener = dragListener)
                 val tilesList = tilesViewModel.tiles.collectAsState()
                        DraggableArea(dragAndDropState) {
                            ElementList(
                                logger = logger,
                                dragAndDropState = dragAndDropState,
                                elements = tilesList.value,
                            )
                        }
                    }
                }
            }
}

