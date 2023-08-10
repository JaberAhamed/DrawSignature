package com.jaber.drawingapp

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.PixelCopy
import android.view.Surface
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jaber.drawingapp.ui.theme.DrawingAPPTheme
import com.jaber.drawingapp.utils.saveToCacheStorage
import kotlin.random.Random

enum class MotionEventt {
    Idle, Down, Move, Up
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrawingAPPTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val view: View = LocalView.current
                    DrawingScreen(modifier = Modifier, view, this)
                }
            }
        }
    }
}

@Composable
fun DrawingScreen(modifier: Modifier, view: View, activity: Activity) {
    val lines = remember {
        mutableStateListOf<Line>()
    }
    val imgurl = remember { mutableStateOf("") }

    val color = remember { mutableStateOf(Color.Black) }
    Column(modifier = Modifier) {
        Row(modifier = Modifier) {
            IconButton(onClick = {
                getScreenShot(view, activity)
            }) {
                Icon(
                    painter = painterResource(R.drawable.save),
                    "save your draw"
                )
            }

            IconButton(onClick = {
                if (lines.size > 0) {
                    lines.clear()
                }
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_reset),
                    "Reset"
                )
            }

            Button(
                onClick = {
                    color.value = Color.Yellow
                },
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
            ) {
            }
            Button(
                onClick = {
                    color.value = Color.Red
                },
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
            }
            Button(
                onClick = {
                    color.value = Color.White
                },
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
            }
            Button(
                onClick = {
                    color.value = Color.Black
                },
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
            }
            Button(
                onClick = {
                    color.value = Color.Blue
                },
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
            }
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val line = Line(
                            start = change.position - dragAmount,
                            end = change.position,
                            color = color.value
                        )

                        lines.add(line)
                    }
                }
        ) {
            lines.forEach { line ->

                drawLine(
                    color = line.color,
                    start = line.start,
                    end = line.end,
                    strokeWidth = line.strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color = Color.Black,
    val strokeWidth: Dp = 1.dp
)

private fun getScreenShot(view: View, activity: Activity): String {
    val current = Random.nextInt()
    var urls = ""
    captureView(view = view, window = activity.window) {
        it?.let { it ->
            val elapsedRealtime = SystemClock.elapsedRealtime()
            it.saveToCacheStorage(activity, "screenshot_$elapsedRealtime.jpg")
                ?.let { url ->
                    urls = url
                    Toast.makeText(activity, "Save image to your image file ", Toast.LENGTH_SHORT).show()
                }
        }
    }
    return urls
}

private fun captureView(
    view: View,
    window: Window,
    bitmapCallback: (Bitmap) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val bitmap = Bitmap.createBitmap(
            window.decorView.width,
            window.decorView.height,
            Bitmap.Config.ARGB_8888
        )

        PixelCopy.request(
            window,
            Rect(
                window.decorView.left,
                window.decorView.top,
                window.decorView.right,
                window.decorView.bottom

            ),
            bitmap,
            {
                if (it == PixelCopy.SUCCESS) {
                    bitmapCallback.invoke(bitmap)
                }
            },
            Handler(Looper.getMainLooper())
        )
    } else {
        val tBitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.RGB_565
        )

        val canvas = Canvas(tBitmap)
        view.draw(canvas)
        canvas.setBitmap(null)
        bitmapCallback.invoke(tBitmap)
    }
}

@Preview
@Composable
fun PreviewDrawingApp() {
    MaterialTheme {
        // DrawingScreen()
    }
}
