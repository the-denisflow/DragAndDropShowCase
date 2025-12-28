package com.example.myapplication.presentation.components.mainpage

import com.example.myapplication.R
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.presentation.components.dragndrop.DragAndDropState

@Composable
fun DraggableArea(
    dragAndDropState: DragAndDropState,
    content: @Composable () -> Unit
) {
     WallpaperBackground(
        imageUri = ImageRequest.Builder(LocalContext.current)
            .data("https://www.ourruo.com/wp-content/uploads/2021/07/Blue-Clouds-iPhone-Wallpaper-580x1030.jpg")
            .crossfade(true)
            .build(),
        blurAmount = 10.dp
    )

    AndroidView(
        factory = {
            content ->
            View(content).apply {
                dragAndDropState.localView = this
            }
        },
        modifier = Modifier.fillMaxSize())
        content()
}

@Composable
fun WallpaperBackground(
    imageUri: ImageRequest?,
    blurAmount: Dp = 0.dp
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if(
            imageUri != null
        ) {
            AsyncImage(
                model = imageUri,
                contentDescription = "wallpaper",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (blurAmount == 0.dp) {
                            Modifier
                        } else {
                            Modifier.blur(blurAmount)
                        }
                    )
            )
        } else {
            Image(
                painter = painterResource(R.drawable.defaultwallpaper),
                contentDescription = "Default wallpaper",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

