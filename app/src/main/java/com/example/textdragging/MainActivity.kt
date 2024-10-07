package com.example.textdragging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.textdragging.ui.theme.TextDraggingTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextDraggingTheme {
                DraggableTextWithTemporaryLine()
            }
        }
    }
}

@Composable
fun DraggableTextWithTemporaryLine() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var shouldDrawLine by remember { mutableStateOf(false) }
    var textSize by remember { mutableStateOf(IntSize.Zero) }
    var textPosition by remember { mutableStateOf(Offset.Zero) }
    var undefinedText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        TextField(
            value = undefinedText,
            onValueChange = { undefinedText = it },
//            fontSize = 20.sp,
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            shouldDrawLine = true
                        },
                        onDragEnd = {
                            shouldDrawLine = false
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
                .onGloballyPositioned { coordinates ->
                    textSize = coordinates.size // Text의 사이즈 추적
                    textPosition = coordinates.positionInWindow() // Text의 위치 추적
                }
                .drawBehind {
                    if (shouldDrawLine) { // 조건에 따라 선을 그림
                        drawLine(
                            color = Color.Red,
                            start = Offset(0f, textSize.height.toFloat()), // 텍스트 하단에 선 시작
                            end = Offset(
                                textSize.width.toFloat(),
                                textSize.height.toFloat()
                            ), // 텍스트 끝까지 선 그림
                            strokeWidth = 5f
                        )
                    }
                }
        )
    }
}





