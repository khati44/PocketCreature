package com.example.pocketcreatures.presentation.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage

@Composable
fun FullScreenImage(
    imageUrl: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetY by remember { mutableStateOf(0f) }
    val animatedOffsetY by animateFloatAsState(targetValue = offsetY)
    var imageBounds by remember { mutableStateOf(androidx.compose.ui.geometry.Rect.Zero) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (!imageBounds.contains(offset)) {
                            onDismiss()
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            offsetY += dragAmount.y
                            if (offsetY > 500f || offsetY < -500f) {
                                onDismiss()
                            }
                            change.consume()
                        },
                        onDragEnd = {
                            if (offsetY <= 500f && offsetY >= -500f) {
                                offsetY = 0f
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .offset(y = animatedOffsetY.dp)
                    .onGloballyPositioned { coordinates ->
                        val position = coordinates.positionInRoot()
                        val size = coordinates.size.toSize()
                        imageBounds = androidx.compose.ui.geometry.Rect(
                            position.x,
                            position.y,
                            position.x + size.width,
                            position.y + size.height
                        )
                    }
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                    modifier = modifier
                )
            }
        }
    }
}