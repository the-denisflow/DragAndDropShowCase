package com.example.myapplication.presentation.components.mainpage

import com.example.myapplication.R
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.presentation.components.dragndrop.DragAndDropState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay

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
        blurAmount = 4.dp
    )

    AndroidView(
        factory = { context ->
            View(context).apply {
                dragAndDropState.localView = this
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    ClockDateWidget()

    content()
}

@Composable
fun ClockDateWidget(modifier: Modifier = Modifier) {
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentTime = System.currentTimeMillis()
        }
    }

    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val dateFormat = remember { SimpleDateFormat("EEEE, MMM d", Locale.getDefault()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = timeFormat.format(Date(currentTime)),
            fontSize = 72.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    blurRadius = 12f
                )
            )
        )
        Text(
            text = dateFormat.format(Date(currentTime)),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.9f),
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.6f),
                    blurRadius = 6f
                )
            )
        )
    }
}

@Composable
fun WallpaperBackground(
    imageUri: ImageRequest?,
    blurAmount: Dp = 0.dp
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (imageUri != null) {
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
