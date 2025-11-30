package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.presentation.components.dragndrop.rememberDragAndDropState
import com.example.myapplication.presentation.components.mainpage.DraggableArea
import com.example.myapplication.presentation.components.mainpage.ElementList
import com.example.myapplication.presentation.viewmodel.TilesViewModel
import com.example.myapplication.shared.utils.AppLogger
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.logging.Logger

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val logger = AppLogger
    private val tilesViewModel: TilesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val dragAndDropState = rememberDragAndDropState(logger = logger)
                DraggableArea(dragAndDropState) {
                    ElementList(
                        dragAndDropState = dragAndDropState,
                        elements = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5"),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            }
        }
    }
